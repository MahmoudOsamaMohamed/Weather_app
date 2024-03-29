package com.mahmoud.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.weatherapp.databinding.CityItemBinding
import com.mahmoud.weatherapp.model.db.Favourate

class FavDelDiffUtil: DiffUtil.ItemCallback<Favourate>() {
    override fun areItemsTheSame(oldItem: Favourate, newItem: Favourate): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Favourate, newItem: Favourate): Boolean {
        return oldItem == newItem
    }
}

class FavAdapter(var deleteFavorite: (Favourate) -> Unit, var goToFavorite: (Favourate) -> Unit): ListAdapter<Favourate, FavAdapter.FavViewHolder>(FavDelDiffUtil()) {
    class FavViewHolder(val binding: CityItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        return FavViewHolder(
            CityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.binding.cityName.text = getItem(position).cityName
        holder.binding.delete.setOnClickListener {
            deleteFavorite(getItem(position))
        }
        holder.binding.cityItem.setOnClickListener {
            goToFavorite(getItem(position))
        }
    }
}