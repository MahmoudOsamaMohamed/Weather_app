package com.mahmoud.weatherapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mahmoud.weatherapp.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.provider.Settings
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    companion object {

        val morningBackgroundMap = mapOf(
            "Thunderstorm" to R.drawable.thunder_storm,
            "Rain" to R.drawable.rain_bg,
            "Snow" to R.drawable.snow_bg,
            "Mist" to R.drawable.mist_bg,
            "Fog" to R.drawable.fog_bg,
            "Smoke" to R.drawable.smoke_bg,
            "Haze" to R.drawable.haze_bg,
            "Clear" to R.drawable.clear_sky_bg,
            "Clouds" to R.drawable.broken_cloud_bj,
            "Dust" to R.drawable.dust_bg,
            "Sand" to R.drawable.sand_bg,
            "Ash" to R.drawable.ash_bg,
            "Squall" to R.drawable.storm_bg,
            "Tornado" to R.drawable.tornado_bg,
            "Drizzle" to R.drawable.drizzle_bg
        )
        val nightBackgroundMap = mapOf(
            "Thunderstorm" to R.drawable.thunder_storm_bg_nt,
            "Rain" to R.drawable.rain_bg_nt,
            "Snow" to R.drawable.snow_bg_nt,
            "Mist" to R.drawable.mist_bg_nt,
            "Fog" to R.drawable.mist_bg_nt,
            "Smoke" to R.drawable.mist_bg_nt,
            "Haze" to R.drawable.mist_bg_nt,
            "Clear" to R.drawable.clear_pg_nt,
            "Clouds" to R.drawable.cloud_bg_nt,
            "Dust" to R.drawable.mist_bg_nt,
            "Sand" to R.drawable.sand_bg,
            "Ash" to R.drawable.ash_bg,
            "Squall" to R.drawable.storm_bg,
            "Tornado" to R.drawable.tornado_bg,
            "Drizzle" to R.drawable.drizzle_bg_nt
        )
        val morningIconMap = mapOf(
            "Thunderstorm" to R.drawable.thunder_strom,
            "Rain" to R.drawable.rain_mg,
            "Snow" to R.drawable.snow,
            "Mist" to R.drawable.mist_mg,
            "Fog" to R.drawable.mist_mg,
            "Smoke" to R.drawable.mist_mg,
            "Haze" to R.drawable.mist_mg,
            "Clear" to R.drawable.clear_mg,
            "Clouds" to R.drawable.cloud_mg,
            "Dust" to R.drawable.mist_mg,
            "Sand" to R.drawable.mist_mg,
            "Ash" to R.drawable.mist_mg,
            "Squall" to R.drawable.storm,
            "Tornado" to R.drawable.torando,
            "Drizzle" to R.drawable.drizzle
        )
        val nightIconMap = mapOf(
            "Thunderstorm" to R.drawable.thunder_strom,
            "Rain" to R.drawable.rain_nt,
            "Snow" to R.drawable.snow,
            "Mist" to R.drawable.mist_nt,
            "Fog" to R.drawable.mist_nt,
            "Smoke" to R.drawable.mist_nt,
            "Haze" to R.drawable.mist_nt,
            "Clear" to R.drawable.clear_nt,
            "Clouds" to R.drawable.cloud_nt,
            "Dust" to R.drawable.mist_nt,
            "Sand" to R.drawable.mist_nt,
            "Ash" to R.drawable.mist_nt,
            "Squall" to R.drawable.storm,
            "Tornado" to R.drawable.torando,
            "Drizzle" to R.drawable.drizzle
        )
        @RequiresApi(Build.VERSION_CODES.O)
        fun unixTimestampToDateTime(unixTimestamp: Long): String {
            val instant = Instant.ofEpochSecond(unixTimestamp)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return instant.atZone(ZoneId.systemDefault()).format(formatter)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatTime(dt:Int, func:(unix:Long)->String={ unix-> unixTimestampToDateTime(unix) }):String{
            val output = func(dt.toLong())
            Log.d("time output", output)
            val time = output.split(" ")[1].substring(0,5)
            val hours = time.substring(0,2).toInt()
            if(hours>12)
                return "${hours-12}${time.substring(2,5)} PM"
            else
                return "${hours}${time.substring(2,5)} AM"
        }
        fun getCityName(context: Context, latitude: Double, longitude: Double): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            var cityName = ""
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 5)
                Log.d("adresses", addresses.toString())
                if (!addresses.isNullOrEmpty()) {
                    cityName = addresses?.get(0)?.locality ?: ""
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return cityName
        }
        fun getDayOfWeek(timestamp: Long): String {
            return SimpleDateFormat("EEEE", Locale.ENGLISH).format(timestamp * 1000)
        }

    }
    lateinit var binding : ActivityMainBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var viewModel: RemoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController= navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        val factory = RemoteViewModelFactory(Reposatory(LocalDataSource(this), RemoteDataSource()))
        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)


        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.favorateFragment -> {
                    navController.navigate(R.id.favorateFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.sittingFragment -> {
                    navController.navigate(R.id.sittingFragment)
                    true
                }
                else -> false
            }
        }



    }


    }


//val factory = RemoteViewModelFactory(RemoteDataSource())
//val viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)
//val localFactory = LocalViewModelFactory(LocalDataSource(this))
//val localViewModel = ViewModelProvider(this, localFactory).get(LocalViewModel::class.java)
////viewModel.getWeather("30.033333","31.233334")
//localViewModel.getAllResponses()
//val forecastJob = lifecycleScope.launch(Dispatchers.IO) {
//    repeatOnLifecycle(Lifecycle.State.STARTED) {
//        localViewModel.onlineWeather.collect {
//            Log.d("collect from db", it[0].city?.name.toString())
//
//        }
//    }
//
//
//}
//val list = mutableListOf<String>()
//viewModel.get_Locales("دمي", "30")
//
//val searchJob = lifecycleScope.launch {
//    repeatOnLifecycle(Lifecycle.State.STARTED) {
//        viewModel.locales.collect { localeResult ->
//            when (localeResult) {
//                is LocaleResult.Success -> {
//                    Log.d("collect with inf", localeResult.data.toString())
//
//                    if(!localeResult.data.isEmpty()&&!localeResult.data[0].localNames?.ar.isNullOrEmpty())
//                        list.add(localeResult.data[0].localNames?.ar.toString())
//                    Log.d("collect list",list.toString())
//                }
//                is LocaleResult.Error -> {
//                    Log.d("collect", localeResult.exception.toString())
//                }
//                else -> {
//                    Log.d("collect", "loading for list")
//                }
//            }
//        }
//
//    }
//
//
//}