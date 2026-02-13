package com.ngallazzi.places.presentation

import com.ngallazzi.places.domain.PlaceDetails

sealed class AddressResult {

    data class FromPlaces(
        val details: PlaceDetails
    ) : AddressResult()

    data class Manual(
        val text: String
    ) : AddressResult()
}
