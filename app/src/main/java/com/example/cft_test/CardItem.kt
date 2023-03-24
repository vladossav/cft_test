package com.example.cft_test


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardItem(
    @Json(name = "bank")
    var bank: Bank? = Bank(),
    @Json(name = "brand")
    var brand: String? = "?",
    @Json(name = "country")
    var country: Country? = Country(),
    @Json(name = "number")
    var number: Number? = Number(),
    @Json(name = "prepaid")
    var prepaid: Boolean? = false,
    @Json(name = "scheme")
    var scheme: String? = "?",
    @Json(name = "type")
    var type: String? = "?"
) {
    @JsonClass(generateAdapter = true)
    data class Bank(
        @Json(name = "city")
        val city: String? = "?",
        @Json(name = "name")
        val name: String? = "?",
        @Json(name = "phone")
        val phone: String? = "?",
        @Json(name = "url")
        val url: String? = "?"
    )

    @JsonClass(generateAdapter = true)
    data class Country(
        @Json(name = "alpha2")
        val alpha2: String? = "",
        @Json(name = "currency")
        val currency: String? = "",
        @Json(name = "emoji")
        val emoji: String? = "",
        @Json(name = "latitude")
        val latitude: Float? = 0.0f,
        @Json(name = "longitude")
        val longitude: Float? = 0.0f,
        @Json(name = "name")
        val name: String? = "?",
        @Json(name = "numeric")
        val numeric: String? = "?"
    )

    @JsonClass(generateAdapter = true)
    data class Number(
        @Json(name = "length")
        val length: Int? = null,
        @Json(name = "luhn")
        val luhn: Boolean? = null
    )
}