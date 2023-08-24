package com.example.apptemplate.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.apptemplate.Util.DataState
import com.example.apptemplate.datastore.University
import com.example.apptemplate.di.RepositoryModule
import com.example.apptemplate.di.RetrofitModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActivityViewModel(
    app: Application
) : AndroidViewModel(app) {

    companion object {
        const val baseUrl = "http://universities.hipolabs.com/"
    }

    private val _uiState: MutableLiveData<DataState<List<University>>> = MutableLiveData()

    val uiState: LiveData<DataState<List<University>>>
            get() = _uiState

    private var job: Job? = null

    fun fetchUniversityList() {
        job?.cancel()
        job = viewModelScope.launch {
            RepositoryModule.provideUniversityRepository(
                RetrofitModule.provideRemoteService(
                    RetrofitModule.provideRetrofitBuilder(
                        baseUrl,
                        RetrofitModule.provideMoshiBuilder()
                    )
                )
            ).fetchUniversityList().onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
        }
    }
}