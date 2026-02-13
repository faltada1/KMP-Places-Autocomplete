package com.ngallazzi.places.domain

import com.ngallazzi.places.data.dto.AutocompleteDTO
import com.ngallazzi.places.data.dto.PlaceDetailsApiDTO

internal interface PlacesDataSource {
    suspend fun searchCity(
        sessionToken: String,
        name: String,
        languageCode: String
    ): Result<AutocompleteDTO>

    suspend fun searchCountry(
        sessionToken: String,
        name: String,
        languageCode: String
    ): Result<AutocompleteDTO>

    suspend fun searchAddress(
        sessionToken: String,
        address: String,
        languageCode: String
    ): Result<AutocompleteDTO>

    suspend fun getPlaceDetails(
        sessionToken: String,
        placeId: String,
        languageCode: String
    ): Result<PlaceDetailsApiDTO>
}
