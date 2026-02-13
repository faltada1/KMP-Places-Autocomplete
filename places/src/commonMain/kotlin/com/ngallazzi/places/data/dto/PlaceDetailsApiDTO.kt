package com.ngallazzi.places.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PlaceDetailsApiDTO(
    val result: DTOResult,
    val status: String,
)

@Serializable
internal data class DTOResult(
    @SerialName("address_components") val addressComponents: List<AddressComponents> = emptyList(),
    @SerialName("formatted_address") val formattedAddress: String? = null,
    @SerialName("name") val name: String? = null
)
