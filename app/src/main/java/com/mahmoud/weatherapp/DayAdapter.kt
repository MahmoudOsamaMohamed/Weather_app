package com.mahmoud.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.weatherapp.databinding.DayItemBinding
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Pojos.Daily
import com.mahmoud.weatherapp.model.TempUnit

class DayDiffUtil:DiffUtil.ItemCallback<Daily>(){
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }

}
class DayAdapter(val tempUnit:String,val lang:String): ListAdapter<Daily, DayAdapter.DayViewHolder>(DayDiffUtil()){
    class DayViewHolder(val binding: DayItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            DayItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = getItem(position)

        if(lang==Language.EN.name)
        holder.binding.day.text = MainActivity.getDayOfWeek(item.dt.toLong())
        else
        holder.binding.day.text = MainActivity.arabicDays[MainActivity.getDayOfWeek(item.dt.toLong())]

        when(tempUnit){
            TempUnit.C.name -> {
                holder.binding.degree.text = item.temp.day.toInt().toString()+ " °C"
            }
            TempUnit.F.name -> {
                holder.binding.degree.text = celsiusToFahrenheit(item.temp.day).toInt().toString()+ " °F"
            }
            TempUnit.K.name -> {
                holder.binding.degree.text = celsiusToKelvin(item.temp.day).toInt().toString()+ " K"
            }
        }

        holder.binding.icon.setImageResource(MainActivity.morningIconMap[item.weather?.get(0)?.main]!!)

    }
    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }
}