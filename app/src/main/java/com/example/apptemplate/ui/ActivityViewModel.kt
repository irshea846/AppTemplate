package com.example.apptemplate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.apptemplate.datastore.remote.Instrument
import com.example.apptemplate.di.RepositoryModule
import com.example.apptemplate.di.RetrofitModule
import com.example.apptemplate.util.DataState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActivityViewModel(
    app: Application
) : AndroidViewModel(app) {

    companion object {
        const val baseUrl = "https://gist.githubusercontent.com"
    }

    private val _uiState: MutableLiveData<DataState<List<Instrument>>> = MutableLiveData()

    val uiState: LiveData<DataState<List<Instrument>>>
        get() = _uiState

    private var job: Job? = null

    fun fetchUniversityList() {
        job?.cancel()
        job = viewModelScope.launch {
            RepositoryModule.provideInstrumentRepository(
                RetrofitModule.provideRemoteService(
                    RetrofitModule.provideRetrofitBuilder(
                        baseUrl,
                        RetrofitModule.provideMoshiBuilder()
                    )
                )
            ).fetchInstrumentList().onEach {
               _uiState.value = it
            }.launchIn(viewModelScope)
        }
    }

}