package com.ngallazzi.places.data

import com.ngallazzi.places.domain.Address
import com.ngallazzi.places.domain.City
import com.ngallazzi.places.domain.Country
import com.ngallazzi.places.domain.PlacesDataSource
import com.ngallazzi.places.domain.SuggestionsInteractor

internal class SuggestionsInteractorImpl(
    private val placesDataSource: PlacesDataSource,
) : SuggestionsInteractor {
    override suspend fun getCountrySuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<Country>> {
        return runCatching {
            val response = placesDataSource.searchCountry(
                sessionToken = sessionToken,
                name = search,
                languageCode
            ).getOrThrow()
            val countries = response.predictions.map {
                Country(
                    id = it.placeId,
                    name = it.structuredFormatting.mainText,
                    extendedName = it.description
                )
            }
            countries
        }
    }

    override suspend fun getCitySuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<City>> {
        return runCatching {
            val response =
                placesDataSource.searchCity(
                    sessionToken = sessionToken,
                    name = search,
                    languageCode
                ).getOrThrow()
            val cities =
                response.predictions.map {
                    City(
                        id = it.placeId,
                        name = it.structuredFormatting.mainText,
                        extendedName = it.description
                    )
                }
            cities
        }
    }

    override suspend fun getAddressSuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<Address>> {
        return runCatching {
            val response =
                placesDataSource.searchAddress(
                    sessionToken = sessionToken,
                    address = search,
                    languageCode
                )
                    .getOrThrow()
            val predictions =
                response.predictions.map {
                    Address(
                        id = it.placeId,
                        value = it.structuredFormatting.mainText,
                        extendedValue = it.description,
                    )
                }
            predictions
        }
    }
}
