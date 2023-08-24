package com.example.apptemplate.repository

import com.example.apptemplate.Util.DataState
import com.example.apptemplate.datastore.ApiService
import com.example.apptemplate.datastore.University
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UniversityRepository(
    private val apiService: ApiService,
) {
    private val universityList: MutableList<University> = mutableListOf()

    suspend fun fetchUniversityList(): Flow<DataState<List<University>>> = flow {
        emit(DataState.Loading)
        try {
            val value: Any? = fetchUniversityFromRemote()
            if (value is MutableList<*>) {
                universityList.addAll(value.filterIsInstance<University>())
            } else if (value is String) {
                throw Exception(value)
            }
            emit(DataState.Success(universityList))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    private suspend fun fetchUniversityFromRemote(): Any? {
        return suspendCoroutine { continuation ->
            val call: Call<List<University>> = apiService.getUniversities()
            call.enqueue(object : Callback<List<University>?> {
                override fun onResponse(
                    call: Call<List<University>?>,
                    response: Response<List<University>?>
                ) {
                    if (response.isSuccessful) {
                        val remoteUniversityList: MutableList<University> = mutableListOf()
                        response.body()!!.forEach {
                            remoteUniversityList.add(it)
                        }
                        continuation.resume(remoteUniversityList)
                    } else {
                        continuation.resume(response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<List<University>?>, t: Throwable) {
                    val failedMsg = t.message
                    continuation.resume(failedMsg)
                }
            })
        }

    }

}
