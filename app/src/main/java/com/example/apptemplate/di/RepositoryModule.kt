package com.example.apptemplate.di

import com.example.apptemplate.datastore.ApiService
import com.example.apptemplate.repository.UniversityRepository

object RepositoryModule{
    fun provideUniversityRepository(
        apiService: ApiService
    ) : UniversityRepository =
        UniversityRepository(apiService)

}