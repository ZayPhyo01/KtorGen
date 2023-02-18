package com.guru.ktorgen.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

object KtorFactory {
    fun create() = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json()
        }
        install(DefaultRequest) {
            header("Content-Type","application/json; charset=UTF-8")
        }

    }
}