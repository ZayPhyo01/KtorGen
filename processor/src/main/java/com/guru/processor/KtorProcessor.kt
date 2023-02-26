package com.guru.processor

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import com.guru.annonation.Ktor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

data class ClientAnnotation(
    val method: ClassName,
    val url: String
)


private fun appendUrlQueryIfExist(valueParam: List<KSValueParameter>): String {
    return valueParam.filter {
        it.annotations.first().shortName.asString() == "Query"
    }.toList().run {
        if (this.isNotEmpty()) {
            var parms = ""
            this.forEach {
                parms += "parameters.append(${it.annotations.first().arguments.first().value},${
                    it.name?.asString().toString()
                })\n"
            }
            "url { $parms }"
        } else {
            ""
        }
    }
}


private fun KSAnnotation.toHttpMethodName() =
    if (this.shortName.asString() == "POST") {
        "Post"
    } else "Get"


class KtorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        val symbolAnnotation = resolver.getSymbolsWithAnnotation(
            Ktor::class.qualifiedName.toString()
        )
        val symbolClasses = symbolAnnotation.filterIsInstance(KSClassDeclaration::class.java)
            .filter { it.classKind == ClassKind.INTERFACE }
        symbolClasses.forEach { classes ->

            val primary = FunSpec.constructorBuilder()
                .addParameter("client", ClassName("io.ktor.client", "HttpClient"))
                .build()

            val propInit = PropertySpec.builder("client", ClassName("io.ktor.client", "HttpClient"))
                .initializer("client")
                .addModifiers(KModifier.PRIVATE)
                .build()

            FileSpec.builder(
                packageName = classes.packageName.asString(),
                fileName = classes.simpleName.asString() + "Impl"
            ).addImport(
                "io.ktor.client.request", "request"
            ).addImport("io.ktor.client.call", "body")
                .addImport("io.ktor.client.request", "setBody")
                .addType(
                    TypeSpec.classBuilder(classes.simpleName.asString() + "Impl")
                        .addSuperinterface(
                            classes.toClassName()
                        )
                        .primaryConstructor(
                            primary
                        ).addProperty(propInit)
                        .addFunctions(
                            classes.getDeclaredFunctions()
                                .map { func ->
                                    val httpMethod = func.annotations.first().let {
                                        ClientAnnotation(
                                            url = it.arguments.first().value.toString(),
                                            method = ClassName(
                                                "io.ktor.http",
                                                "HttpMethod.${it.toHttpMethodName()}"
                                            )
                                        )
                                    }
                                    FunSpec.builder(func.simpleName.asString())
                                        .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
                                        .addParameters(
                                            func.parameters.map {
                                                ParameterSpec.builder(
                                                    name = it.name?.asString().toString(),
                                                    it.type.resolve().toTypeName()
                                                )
                                                    .build()
                                            }
                                        )
                                        .returns(
                                            func.returnType?.resolve()?.toTypeName()!!
                                        ).addStatement(
                                            "val url = %S", httpMethod.url
                                        ).addStatement("val httpMethod = %L", httpMethod.method)
                                        .addStatement(
                                            "return %L",
                                            "client.request(url) { " +
                                                    "method = httpMethod\n" +
                                                    func.parameters.firstOrNull {
                                                        it.annotations.first().shortName.asString() == "Body"
                                                    }.run {
                                                        if (this != null)
                                                            "setBody(${
                                                                this.name?.asString().toString()
                                                            })"
                                                        else ""
                                                    } +
                                                    appendUrlQueryIfExist(func.parameters)
                                                    + "}.body()"
                                        )
                                        .build()
                                }.toList()
                        )
                        .build()

                )
                .build().writeTo(
                    codeGenerator = codeGenerator,
                    dependencies = dependencies
                )
        }


        return symbolAnnotation.filterNot {
            it.validate()
        }.toList()
    }
}
