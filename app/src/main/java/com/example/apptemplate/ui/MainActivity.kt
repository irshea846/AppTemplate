package com.example.apptemplate.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.apptemplate.databinding.ActivityMainBinding
import com.example.apptemplate.datastore.remote.Instrument
import com.example.apptemplate.util.DataState

class MainActivity : AppCompatActivity() {

    private lateinit var instrumentRecyclerViewAdapter: InstrumentRecyclerViewAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ActivityViewModel by viewModels()
    private lateinit var instrumentList: MutableList<Instrument>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObserver()
        viewModel.fetchUniversityList()

        binding.instrumentTypeInput.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    val filteredInstrumentList = instrumentList.filter {
                        s?.let { str ->
                            val instrument = it.instrumentType
                            instrument?.contains(str)
                        } ?: false
                    }
                    instrumentRecyclerViewAdapter.resetInstrumentList(filteredInstrumentList)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )
    }

    private fun subscribeObserver() {
        viewModel.uiState.observe(this) {
            when (it) {
                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    instrumentList = it.data.toMutableList()
                    initRecyclerReview(binding, instrumentList)
                }
                is DataState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_LONG).show()
                }
                is DataState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initRecyclerReview(binding: ActivityMainBinding, list: List<Instrument>) {
        binding.instrumentList.apply {
            instrumentRecyclerViewAdapter = InstrumentRecyclerViewAdapter(list)
            addItemDecoration(DividerItemDecoration(this@MainActivity,
                DividerItemDecoration.VERTICAL))
            adapter = instrumentRecyclerViewAdapter
        }
    }

}