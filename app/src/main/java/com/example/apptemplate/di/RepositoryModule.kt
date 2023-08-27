package com.example.apptemplate.di

import com.example.apptemplate.repository.InstrumentRepository
import com.example.apptemplate.datastore.remote.ApiService

object RepositoryModule {
    fun provideInstrumentRepository(
        apiService: ApiService
    ) : InstrumentRepository =
        InstrumentRepository(apiService)

}