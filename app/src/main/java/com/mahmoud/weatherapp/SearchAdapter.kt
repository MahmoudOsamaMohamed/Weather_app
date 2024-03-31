package com.mahmoud.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.weatherapp.databinding.CityItemBinding
import com.mahmoud.weatherapp.databinding.SearchItemBinding
import com.mahmoud.weatherapp.model.Pojos.Features
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.result.LocaleResult

class SearchDiffUtil: DiffUtil.ItemCallback<Features>() {
    override fun areItemsTheSame(oldItem: Features, newItem: Features): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Features, newItem: Features): Boolean {
        return oldItem == newItem
    }
}

class SearchAdapter(var goToResult: (Features) -> Unit): ListAdapter<Features, SearchAdapter.SearchViewHolder>(SearchDiffUtil()) {
    class SearchViewHolder(val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
           SearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.cityName.text = getItem(position).properties.name
        holder.binding.country.text = getItem(position).properties.country
        holder.binding.cityItem.setOnClickListener {
            goToResult(getItem(position))
        }

    }}