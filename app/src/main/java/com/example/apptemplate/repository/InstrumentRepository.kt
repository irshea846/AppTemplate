package com.example.apptemplate.repository

import com.example.apptemplate.datastore.remote.ApiService
import com.example.apptemplate.datastore.remote.Instrument
import com.example.apptemplate.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InstrumentRepository(
    private val apiService: ApiService
) {
    private val instrumentList: MutableList<Instrument> = mutableListOf()

    suspend fun fetchInstrumentList(): Flow<DataState<List<Instrument>>> = flow {

        emit(DataState.Loading)
        try {
            val value: Any? = fetchInstrumentFromRemote()
            if (value is MutableList<*>) {
                instrumentList.addAll(value.filterIsInstance<Instrument>())
            } else if (value is String) {
                throw Exception(value)
            }
            emit(DataState.Success(instrumentList))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    private suspend fun fetchInstrumentFromRemote(): Any? {
        return suspendCoroutine { continuation ->
            val call: Call<List<Instrument>> = apiService.getInstruments()
            call.enqueue(object : Callback<List<Instrument>?> {
                override fun onResponse(
                    call: Call<List<Instrument>?>,
                    response: Response<List<Instrument>?>
                ) {
                    if (response.isSuccessful) {
                        val remoteInstrumentList: MutableList<Instrument> = mutableListOf()
                        response.body()!!.forEach {
                            remoteInstrumentList.add(it)
                        }
                        continuation.resume(remoteInstrumentList)
                    } else {
                        continuation.resume(response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<List<Instrument>?>, t: Throwable) {
                    val failedMsg = t.message
                    continuation.resume(failedMsg)
                }
            })
        }
    }


//Asynchronous Operations:  Handling Callbacks — High-order functions aid in managing asynchronous
//                          operations, encapsulating callback logic for better readability.

//    private fun fetchInstrumentFromServer(onDataReceived: () -> MutableLiveData<DataState<List<Instrument>>>) {
//        val call: Call<List<Instrument>> = apiService.getInstruments()
//        call.enqueue(object : Callback<List<Instrument>?> {
//            override fun onResponse(
//                call: Call<List<Instrument>?>,
//                response: Response<List<Instrument>?>
//            ) {
//                if (response.isSuccessful) {
//                    val remoteInstrumentList: MutableList<Instrument> = mutableListOf()
//                    response.body()!!.forEach {
//                        remoteInstrumentList.add(it)
//                    }
//                    onDataReceived(remoteInstrumentList)
//                } else {
//                    onDataReceived(response.errorBody().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<List<Instrument>?>, t: Throwable) {
//                val failedMsg = t.message
//                onDataReceived(failedMsg)
//            }
//        })
//    }
}