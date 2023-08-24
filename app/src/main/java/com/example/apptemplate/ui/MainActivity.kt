package com.example.apptemplate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.apptemplate.Util.DataState
import com.example.apptemplate.databinding.ActivityMainBinding
import com.example.apptemplate.databinding.ItemBinding
import com.example.apptemplate.datastore.University

class MainActivity : AppCompatActivity() {
    private lateinit var universityRecyclerViewAdapter: UniversityRecyclerViewAdapter


    private lateinit var binding: ActivityMainBinding
    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeObservers()
        viewModel.fetchUniversityList()
    }

    private fun subscribeObservers() {
        viewModel.uiState.observe(this) {
            when (it) {
                is DataState.Success<List<University>> -> {
                    displayProgressBar(binding, false)
                    initRecyclerView(binding, it.data)
                }

                is DataState.Error -> {
                    displayProgressBar(binding, false)
                    displayError(it.exception.message)
                }

                is DataState.Loading -> {
                    displayProgressBar(binding, true)
                }
            }
        }
    }

    private fun initRecyclerView(binding: ActivityMainBinding, universityList: List<University>) {
        binding.universityList.apply {
            universityRecyclerViewAdapter = UniversityRecyclerViewAdapter(universityList)
            addItemDecoration(DividerItemDecoration(this@MainActivity,
                DividerItemDecoration.VERTICAL))
            adapter = universityRecyclerViewAdapter
        }
    }

    private fun displayError(message: String?) {
        Toast.makeText(this, message ?: "Unknown Problem", Toast.LENGTH_LONG).show()
    }

    private fun displayProgressBar(binding: ActivityMainBinding, isDisplayed: Boolean){
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}

class UniversityRecyclerViewAdapter(
    universityList: List<University>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = universityList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val universityViewBinding: ItemBinding =
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(universityViewBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: University = items[position]
        when (holder) {
            is UniversityViewHolder -> holder.bind(item)
        }
    }

    class UniversityViewHolder(
        itemBinding: ItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val binding = itemBinding
        fun bind(university: University) {
            binding.name.text = university.name
            binding.country.text = university.country
        }
    }

}
