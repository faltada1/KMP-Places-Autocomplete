package com.ngallazzi.places

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun provideHttpClient(apiKey: String): HttpClient {
    return HttpClient(Darwin) {
        configureClient(apiKey)
    }
}
