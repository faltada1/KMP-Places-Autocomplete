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
import com.ngallazzi.places.provideHttpClient

internal class PlacesHelper(apiKey: String) :
    SuggestionsInteractor,
    PlaceDetailsInteractor {
    private val httpClient = provideHttpClient(apiKey)
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
