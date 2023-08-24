package com.example.apptemplate.datastore

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET ("/search?country=United+States")
    fun getUniversities(): Call<List<University>>

}