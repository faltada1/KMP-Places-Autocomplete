package com.ngallazzi.places.domain

internal interface PlaceDetailsInteractor {
    suspend fun getPlaceDetails(
        sessionToken: String,
        placeId: String, languageCode: String): Result<PlaceDetails>
}
