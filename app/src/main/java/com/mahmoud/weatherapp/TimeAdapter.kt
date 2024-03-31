package com.mahmoud.weatherapp


import android.os.Build
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Locale

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

        if(isMorning(item.dt,sunrise,sunset)){
            holder.binding.icon.setImageResource(morningIconMap[item.weather?.get(0)?.main]!!)
        }
        else{
        holder.binding.icon.setImageResource(nightIconMap[item.weather?.get(0)?.main]!!)

        }
    }
    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun isMorning(dt: Int, sunrise:Long, sunset:Long):Boolean{
        val now =MainActivity.formatTime(dt.toInt())
        val sunriseTime = MainActivity.formatTime(sunrise.toInt())
        val sunsetTime = MainActivity.formatTime(sunset.toInt())
        Log.d("timeadapter==","now is $now and sunrise is $sunriseTime and sunset is $sunsetTime")
        return isTimeInRange(sunriseTime, sunsetTime, now)
    }
    fun isTimeInRange(startTime: String, endTime: String, targetTime: String): Boolean {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val start = dateFormat.parse(startTime)
        val end = dateFormat.parse(endTime)
        val target = dateFormat.parse(targetTime)

        return target in start..end
    }
}
