package com.example.apptemplate.datastore

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class University(

    val country: String? = "",

    val domain: List<String>? = mutableListOf(),

    @Json(name = "alpha_two_code")
    val alphaTwoCode: String? = "",

    @Json(name = "state-province")
    val stateProvince: String? = null,

    @Json(name = "web_pages")
    val webPage: List<String>? = mutableListOf(),

    val name: String? = "",

)

