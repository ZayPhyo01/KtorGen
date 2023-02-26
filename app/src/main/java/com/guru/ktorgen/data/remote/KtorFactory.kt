package com.guru.ktorgen.data.remote

import com.guru.ktorgen.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

object KtorFactory {
    fun create() = HttpClient(Android) {
        installLogging()
        install(ContentNegotiation) { json() }
        install(DefaultRequest) {
            header("Content-Type", "application/json; charset=UTF-8")
        }
    }
}

private fun HttpClientConfig<AndroidEngineConfig>.installLogging() {
    if (BuildConfig.DEBUG) {
        install(Logging) {
            level = LogLevel.BODY
        }
    }
}
