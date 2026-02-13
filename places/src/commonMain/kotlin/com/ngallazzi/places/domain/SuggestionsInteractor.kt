package com.ngallazzi.places.domain

internal interface SuggestionsInteractor {
    suspend fun getCountrySuggestions(
        sessionToken: String,
        search: String, languageCode: String
    ): Result<List<Country>>

    suspend fun getCitySuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<City>>

    suspend fun getAddressSuggestions(
        sessionToken: String,
        search: String,
        languageCode: String
    ): Result<List<Address>>
}
