package com.ngallazzi.places

import io.ktor.client.HttpClient

expect fun provideHttpClient(apiKey: String): HttpClient
