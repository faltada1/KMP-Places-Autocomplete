package com.ngallazzi.places.data

import com.ngallazzi.places.data.dto.AddressComponents
import com.ngallazzi.places.domain.PlaceDetails
import com.ngallazzi.places.domain.PlaceDetailsInteractor
import com.ngallazzi.places.domain.PlacesDataSource

internal class PlaceDetailsInteractorImpl(
    private val placesDataSource: PlacesDataSource,
) : PlaceDetailsInteractor {
    override suspend fun getPlaceDetails(
        sessionToken: String,
        placeId: String, languageCode: String
    ): Result<PlaceDetails> {
        return runCatching {
            val response =
                placesDataSource.getPlaceDetails(sessionToken = sessionToken, placeId = placeId, languageCode).getOrThrow()
            val formattedAddress = response.result.formattedAddress
            val addressComponents = response.result.addressComponents
            val postalCode = findByTypeShort(addressComponents, "postal_code").orEmpty()
            val countryName = findByTypeLong(addressComponents, "country").orEmpty()
            val cityName = extractCitySmart(addressComponents).orEmpty()
            return Result.success(
                PlaceDetails(
                    id = placeId,
                    formattedAddress = formattedAddress.orEmpty(),
                    postalCode = postalCode,
                    country = countryName,
                    city = cityName,
                    shortAddress = response.result.name.orEmpty()
                )
            )
        }
    }

    private fun extractCitySmart(components: List<AddressComponents>): String? {
        // Primary city candidates
        val primary = listOf("locality", "postal_town")

        primary.firstNotNullOfOrNull { t -> findByTypeLong(components, t) }
            ?.let { return it }

        // District-like names
        val districts = listOf("sublocality_level_1", "sublocality")

        districts.firstNotNullOfOrNull { t -> findByTypeLong(components, t) }
            ?.let { return it }

        // Administrative fallbacks
        val admin = listOf(
            "administrative_area_level_3",
            "administrative_area_level_2",
            "administrative_area_level_1"
        )

        admin.firstNotNullOfOrNull { t -> findByTypeLong(components, t) }
            ?.let { return it }

        // Last fallback (sometimes neighborhood works)
        val loose = listOf("neighborhood", "route")

        loose.firstNotNullOfOrNull { t -> findByTypeLong(components, t) }
            ?.let { return it }

        return null
    }

    private fun findByType(list: List<AddressComponents>, type: String): AddressComponents? =
        list.firstOrNull { it.types.contains(type) }

    private fun findByTypeLong(list: List<AddressComponents>, type: String): String? =
        findByType(list, type)?.longName

    private fun findByTypeShort(list: List<AddressComponents>, type: String): String? =
        findByType(list, type)?.shortName
}

