package com.mahmoud.weatherapp


import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.weatherapp.MainActivity.Companion.morningIconMap
import com.mahmoud.weatherapp.MainActivity.Companion.nightIconMap
import com.mahmoud.weatherapp.databinding.HoursItemBinding
import com.mahmoud.weatherapp.model.Pojos.Hourly
import com.mahmoud.weatherapp.model.TempUnit

class TimeDiffUtil: DiffUtil.ItemCallback<Hourly>() {
    override fun areItemsTheSame(oldItem:Hourly, newItem: Hourly): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem:Hourly, newItem:Hourly): Boolean {
        return oldItem == newItem
    }
}
class TimeAdapter(var tempUnit:String,val sunrise:Long,val sunset:Long) : ListAdapter<Hourly, TimeAdapter.TimeViewHolder>(TimeDiffUtil()) {
    class TimeViewHolder(val binding: HoursItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {

        return TimeViewHolder(
            HoursItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.time.text = MainActivity.formatTime(item.dt)

        when(tempUnit){
            TempUnit.C.name -> {
                holder.binding.degree.text = item.temp.toInt().toString()+ " °C"
            }
            TempUnit.F.name -> {
                holder.binding.degree.text = celsiusToFahrenheit(item.temp).toInt().toString()+ " °F"
            }
            TempUnit.K.name -> {
                holder.binding.degree.text = celsiusToKelvin(item.temp).toInt().toString()+ " K"
            }
        }
        if(item.dt<sunset&&item.dt>sunrise) {
            holder.binding.icon.setImageResource(morningIconMap[item.weather?.get(0)?.main]!!)
        }
        holder.binding.icon.setImageResource(nightIconMap[item.weather?.get(0)?.main]!!)

        }
    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }
}
