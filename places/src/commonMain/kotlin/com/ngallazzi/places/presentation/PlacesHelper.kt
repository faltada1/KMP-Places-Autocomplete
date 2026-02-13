package com.ngallazzi.places.presentation

import com.ngallazzi.places.data.PlaceDetailsInteractorImpl
import com.ngallazzi.places.data.PlacesRemoteDataSource
import com.ngallazzi.places.data.SuggestionsInteractorImpl
import com.ngallazzi.places.domain.Address
import com.ngallazzi.places.domain.City
import com.ngallazzi.places.domain.Country
import com.ngallazzi.places.domain.PlaceDetails
import com.ngallazzi.places.domain.PlaceDetailsInteractor
import com.ngallazzi.places.domain.SuggestionsInteractor
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "maps.googleapis.com"

internal class PlacesHelper(private val apiKey: String) : SuggestionsInteractor,
    PlaceDetailsInteractor {
    private val httpClient = HttpClient {
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

        // uncomment to enable logging
        /*
             install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("HTTP LOG → $message")
                        }
                    }
                    level = LogLevel.ALL
                }*/
    }

    private val suggestionsInteractor: SuggestionsInteractor = SuggestionsInteractorImpl(
        PlacesRemoteDataSource(
            httpClient
        )
    )

    private val detailsInteractor: PlaceDetailsInteractor = PlaceDetailsInteractorImpl(
        PlacesRemoteDataSource(
            httpClient
        )
    )

    override suspend fun getCountrySuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<Country>> {
        return suggestionsInteractor.getCountrySuggestions(sessionToken, search, languageCode)
    }

    override suspend fun getCitySuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<City>> {
        return suggestionsInteractor.getCitySuggestions(sessionToken, search, languageCode)
    }

    override suspend fun getAddressSuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<Address>> {
        return suggestionsInteractor.getAddressSuggestions(sessionToken, search, languageCode)
    }

    override suspend fun getPlaceDetails(
        sessionToken: String,
        placeId: String,
        languageCode: String
    ): Result<PlaceDetails> {
        return detailsInteractor.getPlaceDetails(sessionToken, placeId, languageCode)
    }
}
