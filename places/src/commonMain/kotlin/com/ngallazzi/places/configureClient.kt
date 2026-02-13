package com.ngallazzi.places

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "maps.googleapis.com"

fun HttpClientConfig<*>.configureClient(apiKey: String) {
    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = BASE_URL
            parameters["key"] = apiKey
        }
    }

    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }
}
