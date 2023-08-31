package com.example.apptemplate.datastore.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
"id": "cead5cf4-9f22-11ec-b909-0242ac120002",
"name": "Apple",
"ticker": "AAPL",
"instrument_type": "stock",
"current_price": 142.70,
"previous_price": 141.70,
"description": "Apple is a blah blah blah...
*/

@JsonClass(generateAdapter = true)
data class Instrument(

    val id: String? = "",

    val name: String? = "",

    val ticker: String? = "",

    @Json(name = "instrument_type")
    val instrumentType: String? = "",

    @Json(name = "current_price")
    val currentPrice: Float? = 0.0f,

    @Json(name = "previous_price")
    val previousPrice: Float? = 0.0f,

    val description: String? = "",

)
