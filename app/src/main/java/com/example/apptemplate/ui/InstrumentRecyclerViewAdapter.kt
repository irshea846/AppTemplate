package com.example.apptemplate.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apptemplate.databinding.ItemBinding
import com.example.apptemplate.datastore.remote.Instrument

class InstrumentRecyclerViewAdapter(
    instrumentList: List<Instrument>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Instrument> = instrumentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return InstrumentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: Instrument = items[position]
        when (holder) {
            is InstrumentViewHolder -> holder.bind(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetInstrumentList(instrumentItems: List<Instrument>) {
        items = instrumentItems
        notifyDataSetChanged()
    }

    class InstrumentViewHolder(
        private val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Instrument) {
            binding.name.text = item.name
        }
    }

}
