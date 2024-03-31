package com.mahmoud.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.weatherapp.databinding.AlertItemBinding
import com.mahmoud.weatherapp.databinding.CityItemBinding
import com.mahmoud.weatherapp.model.db.Favourate
import com.plcoding.alarmmanagerguide.AlarmItem


class AlertsDelDiffUtil: DiffUtil.ItemCallback<AlarmItem>() {
    override fun areItemsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AlarmItem, newItem:AlarmItem): Boolean {
        return oldItem == newItem
    }
}

class AlertsAdapter(var deleteAlert: (AlarmItem) -> Unit): ListAdapter<AlarmItem, AlertsAdapter.AlertViewHolder>(AlertsDelDiffUtil()) {
    class AlertViewHolder(val binding: AlertItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
           AlertItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.binding.cityName.text = getItem(position).city
        holder.binding.country.text = getItem(position).day.toString()+" days "+getItem(position).hour+" hours "+getItem(position).minute+" minutes"
        holder.binding.delete.setOnClickListener {
            deleteAlert(getItem(position))
        }

    }
}