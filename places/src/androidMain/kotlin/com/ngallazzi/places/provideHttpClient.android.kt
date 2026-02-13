package com.ngallazzi.places

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun provideHttpClient(apiKey: String): HttpClient {
    return HttpClient(OkHttp) {
        configureClient(apiKey)
    }
}
