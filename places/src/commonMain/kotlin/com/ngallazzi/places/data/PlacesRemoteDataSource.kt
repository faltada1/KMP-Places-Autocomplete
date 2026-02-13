package com.ngallazzi.places.data

import com.ngallazzi.places.data.dto.AutocompleteDTO
import com.ngallazzi.places.data.dto.PlaceDetailsApiDTO
import com.ngallazzi.places.domain.PlacesDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLQueryComponent

internal class PlacesRemoteDataSource(
    private val httpClient: HttpClient
) : PlacesDataSource {

    override suspend fun searchCity(
        sessionToken: String,
        name: String,
        languageCode: String
    ): Result<AutocompleteDTO> {

        val url = buildString {
            append("maps/api/place/autocomplete/json?")
            append("input=${name.encodeURLQueryComponent()}")
            append("&types=(cities)")
            append("&language=${languageCode.encodeURLQueryComponent()}")
            append("&sessiontoken=${sessionToken.encodeURLQueryComponent()}")
        }

        return handleAutocompleteCall(url)
    }

    override suspend fun searchCountry(
        sessionToken: String,
        name: String,
        languageCode: String
    ): Result<AutocompleteDTO> {

        val url = buildString {
            append("maps/api/place/autocomplete/json?")
            append("input=${name.encodeURLQueryComponent()}")
            append("&types=(regions)")
            append("&language=${languageCode.encodeURLQueryComponent()}")
            append("&sessiontoken=${sessionToken.encodeURLQueryComponent()}")
        }

        return handleAutocompleteCall(url)
    }

    override suspend fun searchAddress(
        sessionToken: String,
        address: String,
        languageCode: String
    ): Result<AutocompleteDTO> {
        val url = buildString {
            append("maps/api/place/autocomplete/json?")
            append("input=${address.encodeURLQueryComponent()}")
            append("&types=address")
            append("&locationbias=circle:300000@49.8,15.5")
            append("&language=${languageCode.encodeURLQueryComponent()}")
            append("&sessiontoken=${sessionToken.encodeURLQueryComponent()}")
        }
        return handleAutocompleteCall(url)
    }

    override suspend fun getPlaceDetails(
        sessionToken: String,
        placeId: String,
        languageCode: String
    ): Result<PlaceDetailsApiDTO> {

        val url = buildString {
            append("maps/api/place/details/json?")
            append("place_id=${placeId.encodeURLQueryComponent()}")
            append("&language=${languageCode.encodeURLQueryComponent()}")
            append("&fields=name,address_components,formatted_address")
            append("&sessiontoken=${sessionToken.encodeURLQueryComponent()}")
        }

        return handlePlaceDetailsCall(url)
    }

    private suspend fun handleAutocompleteCall(url: String): Result<AutocompleteDTO> {
        return try {
            val result = httpClient.get(url) {
                contentType(ContentType.Application.Json)
            }

            when (result.status) {
                HttpStatusCode.OK -> {
                    val response = result.body<AutocompleteDTO>()
                    when (response.status) {
                        "OK" -> Result.success(response)
                        "REQUEST_DENIED" -> Result.failure(Exception("Invalid api key"))
                        else -> Result.failure(Exception(response.status))
                    }
                }
                else -> Result.failure(Exception(result.status.toString()))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun handlePlaceDetailsCall(url: String): Result<PlaceDetailsApiDTO> {
        return try {
            val result = httpClient.get(url) {
                contentType(ContentType.Application.Json)
            }

            when (result.status) {
                HttpStatusCode.OK -> {
                    val response = result.body<PlaceDetailsApiDTO>()
                    when (response.status) {
                        "OK" -> Result.success(response)
                        "REQUEST_DENIED" -> Result.failure(Exception("Invalid api key"))
                        else -> Result.failure(Exception(response.status))
                    }
                }
                else -> Result.failure(Exception(result.status.toString()))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
