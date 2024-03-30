package com.mahmoud.weatherapp


import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mahmoud.weatherapp.MainActivity.Companion.morningBackgroundMap
import com.mahmoud.weatherapp.MainActivity.Companion.morningIconMap
import com.mahmoud.weatherapp.MainActivity.Companion.nightBackgroundMap
import com.mahmoud.weatherapp.MainActivity.Companion.nightIconMap
import com.mahmoud.weatherapp.databinding.FragmentHomeBinding
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.Pojos.ForecastResponse
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.CashWeather
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.model.result.ForecastResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val REQUEST_LOCATION_CODE = 2005
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: RemoteViewModel
    lateinit var forecastResponse: ForecastResponse
    lateinit var speedUnit: String
    lateinit var tempUnit: String
     var cityName: LocaleResponse?=null
    lateinit var language: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAllarm.setOnClickListener {
val dailog = AlaretDailog(requireContext())
            val args = Bundle()
            for (i in forecastResponse.daily.indices) {
                args.putSerializable("list$i", forecastResponse.daily[i])
            }
            args.putString("name",cityName?.name)

            dailog.arguments = args

            dailog.show(parentFragmentManager,"")
        }
        val factory = RemoteViewModelFactory(
            Reposatory(
                LocalDataSource(requireContext()),
                RemoteDataSource(),
            )
        )

        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)
        if (HomeFragmentArgs.fromBundle(requireArguments()).lat.isNullOrEmpty()) {
            viewModel.readSettingsPreference()
            lifecycleScope.launch(Dispatchers.Main) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.settingsPreferences.collect {
                        speedUnit = it.speedUnit
                        tempUnit = it.tempUnit
                        language = it.language
                        if (!it.lat.isNullOrEmpty() || !it.lon.isNullOrEmpty()) {
                            viewModel.getWeather(it.lat, it.lon,it.language)
                            viewModel.get_Locales(it.lat, it.lon, "10")

                            Log.d("how we do ","noraml")
                        } else if (it.location == Location.OFF.name) {
                            Log.d("how we do ","going to search")
                            NavHostFragment.findNavController(this@HomeFragment).navigate(
                                HomeFragmentDirections.actionHomeFragmentToSearchFragment("home")
                            )
                        } else {
                           Log.d("how we do ","refresh")
                                if(checkedPermission()){
                                    if(isLocationEnabled()){
                                        Log.d("how we do++ ","getFreshLocation")
                                        getFreshLocation()
                                    }
                                    else{
                                        Log.d("how we do++ ","enableLocationService")
                                        enableLocationService()
                                    }
                                }else{
                                    Log.d("how we do++ ","requestPermission")
                                    ActivityCompat.requestPermissions(
                                        requireActivity(),
                                        arrayOf(
                                            android.Manifest.permission.ACCESS_FINE_LOCATION
                                            ,android.Manifest.permission.ACCESS_COARSE_LOCATION
                                        ), REQUEST_LOCATION_CODE)
                                    NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_homeFragment_self)

                                }
                            }
                        }


                }
            }

        } else {
            viewModel.readSettingsPreference()
            lifecycleScope.launch(Dispatchers.Main) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.settingsPreferences.collect {
                        speedUnit = it.speedUnit
                        tempUnit = it.tempUnit
                        language = it.language
                        viewModel.getWeather(
                            HomeFragmentArgs.fromBundle(requireArguments()).lat,
                            HomeFragmentArgs.fromBundle(requireArguments()).lon,
                            it.language
                            )
                        viewModel.get_Locales(
                            HomeFragmentArgs.fromBundle(requireArguments()).lat,
                            HomeFragmentArgs.fromBundle(requireArguments()).lon,
                            "10"
                        )
                    }}}
        }

        viewModel.getAllFavourates()
        binding.loveIcon.setOnClickListener{
            if(binding.loveIcon.paddingTop==1){
                viewModel.deleteFromFavorites(Favourate(cityName?.name.toString(), cityName?.lat.toString(), cityName?.lon.toString(),cityName?.localNames?.ar.toString()))
                binding.loveIcon.setImageResource(R.drawable.empty_love)
                binding.loveIcon.setPadding(0,0,0,0)
            }
            else{
            viewModel.insertToFavorites(Favourate(cityName?.name.toString(), cityName?.lat.toString(), cityName?.lon.toString(),cityName?.localNames?.ar.toString()))
            binding.loveIcon.setImageResource(R.drawable.love)
                binding.loveIcon.setPadding(0,1,0,0)
            }
        }
        val loveIconJob = lifecycleScope.launch (Dispatchers.IO){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favourate.collect {
                    withContext(Dispatchers.Main){
                    binding.loveIcon.setImageResource(R.drawable.empty_love)
                    if (it.isNotEmpty()) {
                        for(fav in it){
                            while(cityName==null&&isInternetAvailable(requireContext())){
                                delay(50)

                            }
                            if(fav.cityName==cityName?.name.toString()){
                                binding.loveIcon.setImageResource(R.drawable.love)
                            }
                        }
                    }}
                }
            }
        }
        val homeWeatherJob = lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.onlineWeather.collect { result ->
                    when (result) {

                        is ForecastResult.Success -> {
                            forecastResponse = result.data
                            viewModel.deleteAllCash()
                            while(cityName==null&&isInternetAvailable(requireContext())){
                                delay(50)
                            }
                            viewModel.insertToCash(CashWeather(forecastResponse,cityName?.name.toString(),cityName?.localNames?.ar.toString()))

                            Log.d("hourly", forecastResponse.hourly.size.toString())
                            withContext(Dispatchers.Main) {
                                binding.loading.visibility = View.GONE
                                binding.error.visibility = View.GONE
                                binding.linearLayout.visibility = View.VISIBLE
                                val cardsList = listOf<CardView>(
                                    binding.cardView,
                                    binding.cardView1,
                                    binding.cardView2,
                                    binding.cardView3,
                                    binding.cardView4,
                                    binding.cardView5,
                                    binding.cardView6,
                                    binding.cardView7,
                                    binding.cardView8,
                                    binding.cardView9
                                )
                                for (card in cardsList)
                                    card.setCardBackgroundColor(Color.argb(128, 0, 0, 0))
                                if(forecastResponse.current.dt<forecastResponse.current.sunset&&forecastResponse.current.dt>forecastResponse.current.sunrise) {
                                    binding.weatherIcon.setImageResource(morningIconMap[forecastResponse.current.weather[0].main]!!)
                                    binding.fragmentHome.background =
                                        resources.getDrawable(morningBackgroundMap[forecastResponse.current.weather[0].main]!!)
                                }
                                else{
                                    binding.weatherIcon.setImageResource(nightIconMap[forecastResponse.current.weather[0].main]!!)
                                    binding.fragmentHome.background =
                                        resources.getDrawable(nightBackgroundMap[forecastResponse.current.weather[0].main]!!)
                                }
                                if(tempUnit == TempUnit.C.name)
                                    binding.weatherTemp.text =
                                        forecastResponse.current.temp.toInt().toString() + " °C"
                                else if(tempUnit == TempUnit.F.name)
                                    binding.weatherTemp.text =
                                        celsiusToFahrenheit(forecastResponse.current.temp).toInt().toString() + " °F"
                                else
                                    binding.weatherTemp.text =
                                        celsiusToKelvin(forecastResponse.current.temp).toInt().toString() + " K"

                                binding.weatherDescription.text =
                                    forecastResponse.current.weather[0].description
                                if (tempUnit == TempUnit.C.name)
                                {
                                binding.feelLike.text =
                                    "feels like: " + forecastResponse.current.feels_like.toInt() + " °C"
                                binding.minMax.text =
                                    "min: " + forecastResponse.daily[0].temp.max.toInt() + " °C" + " max: " + forecastResponse.daily[0]?.temp?.min?.toInt() + " °C"}
                                else if (tempUnit == TempUnit.F.name){
                                    binding.feelLike.text =
                                        "feels like: " + celsiusToFahrenheit(forecastResponse.current.feels_like).toInt() + " °F"
                                    binding.minMax.text =
                                        "min: " + celsiusToFahrenheit(forecastResponse.daily[0].temp.min) + " °F" + " max: " + celsiusToFahrenheit(forecastResponse.daily[0].temp.max) + " °F"
                                }else{
                                    binding.feelLike.text =
                                        "feels like: " + celsiusToKelvin(forecastResponse.current.feels_like).toInt() + " K"
                                    binding.minMax.text =
                                        "min: " + celsiusToKelvin(forecastResponse.daily[0].temp.min) + " K" + " max: " + celsiusToKelvin(forecastResponse.daily[0].temp.max) + " K"
                                }
                                binding.day.text =
                                    MainActivity.getDayOfWeek(forecastResponse.current.dt.toLong())
                                binding.time.text =
                                    MainActivity.formatTime(forecastResponse.current.dt)
                                binding.humidity.text =
                                    forecastResponse.current.humidity.toString() + "%"
                                binding.clouds.text =
                                    forecastResponse.current.clouds.toString() + "%"
                                if(speedUnit == SpeedUnit.KMPH.name)
                                binding.winds.text =
                                    forecastResponse.current.wind_speed.toInt().toString() + " m/s"
                                else
                                binding.winds.text =
                                    metersPerSecondToMilesPerHour(forecastResponse.current.wind_speed).toInt().toString() + " m/h"
                                binding.pressure.text =
                                    forecastResponse.current.pressure.toString() + " hPa"
                                binding.progressBar.isEnabled = false
                                binding.sunrise.text =
                                    MainActivity.formatTime(forecastResponse.current.sunrise)
                                binding.sunset.text =
                                    MainActivity.formatTime(forecastResponse.current.sunset)
                                binding.progressBar.progress = calcucateSunProgress(
                                    forecastResponse.current.sunrise,
                                    forecastResponse.current.sunset,
                                    forecastResponse.current.dt
                                )
                                binding.vision.text =
                                    ((forecastResponse.current.visibility) / 1000).toString() + " km"
                                binding.uv.text = (forecastResponse.current.uvi).toString()

                                binding.rain.text =
                                    (forecastResponse.hourly[0].pop * 100).toString() + "%"
                                binding.timeRv.layoutManager = LinearLayoutManager(
                                    activity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                val adapter = TimeAdapter(tempUnit,forecastResponse.current.sunrise.toLong(),forecastResponse.current.sunset.toLong())
                                binding.timeRv.adapter = adapter
                                adapter.submitList(forecastResponse.hourly)
                                binding.dayRv.layoutManager = LinearLayoutManager(
                                    activity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                val adapter2 = DayAdapter(tempUnit)
                                binding.dayRv.adapter = adapter2
                                binding.dayRv.setHasFixedSize(true)
                                adapter2.submitList(forecastResponse.daily)


                            }
                        }

                        is ForecastResult.Error -> {
                            Log.d("form cash","errrrrrrrrrr")
                            if(HomeFragmentArgs.fromBundle(requireArguments()).lat.isNullOrEmpty()){
                                viewModel.getAllCashed()
                                val getCashedJob = lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.cashedWeather.collect {
                                        if(it.isNotEmpty()) {

                                            forecastResponse = it[0].forecastResponse
                                            withContext(Dispatchers.Main) {
                                                binding.loading.visibility = View.GONE
                                                binding.error.visibility = View.GONE
                                                binding.linearLayout.visibility = View.VISIBLE
                                                val cardsList = listOf<CardView>(
                                                    binding.cardView,
                                                    binding.cardView1,
                                                    binding.cardView2,
                                                    binding.cardView3,
                                                    binding.cardView4,
                                                    binding.cardView5,
                                                    binding.cardView6,
                                                    binding.cardView7,
                                                    binding.cardView8,
                                                    binding.cardView9
                                                )
                                                for (card in cardsList)
                                                    card.setCardBackgroundColor(Color.argb(128, 0, 0, 0))

                                                binding.cityName.text =
                                                    if (language == Language.EN.name) it[0].cityName else it[0].cityNameAr
                                                if (forecastResponse.current.dt < forecastResponse.daily[0].sunset) {
                                                    binding.weatherIcon.setImageResource(
                                                        morningIconMap[forecastResponse.current.weather[0].main]!!
                                                    )
                                                    binding.fragmentHome.background =
                                                        resources.getDrawable(morningBackgroundMap[forecastResponse.current.weather[0].main]!!)
                                                } else {
                                                    binding.weatherIcon.setImageResource(
                                                        nightIconMap[forecastResponse.current.weather[0].main]!!
                                                    )
                                                    binding.fragmentHome.background =
                                                        resources.getDrawable(nightBackgroundMap[forecastResponse.current.weather[0].main]!!)
                                                }
                                                if (tempUnit == TempUnit.C.name)
                                                    binding.weatherTemp.text =
                                                        forecastResponse.current.temp.toInt()
                                                            .toString() + " °C"
                                                else if (tempUnit == TempUnit.F.name)
                                                    binding.weatherTemp.text =
                                                        celsiusToFahrenheit(forecastResponse.current.temp).toInt()
                                                            .toString() + " °F"
                                                else
                                                    binding.weatherTemp.text =
                                                        celsiusToKelvin(forecastResponse.current.temp).toInt()
                                                            .toString() + " K"

                                                binding.weatherDescription.text =
                                                    forecastResponse.current.weather[0].description
                                                if (tempUnit == TempUnit.C.name) {
                                                    binding.feelLike.text =
                                                        "feels like: " + forecastResponse.current.feels_like.toInt() + " °C"
                                                    binding.minMax.text =
                                                        "min: " + forecastResponse.daily[0].temp.max.toInt() + " °C" + " max: " + forecastResponse.daily[0]?.temp?.min?.toInt() + " °C"
                                                } else if (tempUnit == TempUnit.F.name) {
                                                    binding.feelLike.text =
                                                        "feels like: " + celsiusToFahrenheit(
                                                            forecastResponse.current.feels_like
                                                        ).toInt() + " °F"
                                                    binding.minMax.text =
                                                        "min: " + celsiusToFahrenheit(
                                                            forecastResponse.daily[0].temp.min
                                                        ) + " °F" + " max: " + celsiusToFahrenheit(
                                                            forecastResponse.daily[0].temp.max
                                                        ) + " °F"
                                                } else {
                                                    binding.feelLike.text =
                                                        "feels like: " + celsiusToKelvin(
                                                            forecastResponse.current.feels_like
                                                        ).toInt() + " K"
                                                    binding.minMax.text =
                                                        "min: " + celsiusToKelvin(forecastResponse.daily[0].temp.min) + " K" + " max: " + celsiusToKelvin(
                                                            forecastResponse.daily[0].temp.max
                                                        ) + " K"
                                                }
                                                binding.day.text =
                                                    MainActivity.getDayOfWeek(forecastResponse.current.dt.toLong())
                                                binding.time.text =
                                                    MainActivity.formatTime(forecastResponse.current.dt)
                                                binding.humidity.text =
                                                    forecastResponse.current.humidity.toString() + "%"
                                                binding.clouds.text =
                                                    forecastResponse.current.clouds.toString() + "%"
                                                if (speedUnit == SpeedUnit.KMPH.name)
                                                    binding.winds.text =
                                                        forecastResponse.current.wind_speed.toInt()
                                                            .toString() + " m/s"
                                                else
                                                    binding.winds.text =
                                                        metersPerSecondToMilesPerHour(
                                                            forecastResponse.current.wind_speed
                                                        ).toInt().toString() + " m/h"
                                                binding.pressure.text =
                                                    forecastResponse.current.pressure.toString() + " hPa"
                                                binding.progressBar.isEnabled = false
                                                binding.sunrise.text =
                                                    MainActivity.formatTime(forecastResponse.current.sunrise)
                                                binding.sunset.text =
                                                    MainActivity.formatTime(forecastResponse.current.sunset)
                                                binding.progressBar.progress = calcucateSunProgress(
                                                    forecastResponse.current.sunrise,
                                                    forecastResponse.current.sunset,
                                                    forecastResponse.current.dt
                                                )
                                                binding.vision.text =
                                                    ((forecastResponse.current.visibility) / 1000).toString() + " km"
                                                binding.uv.text =
                                                    (forecastResponse.current.uvi).toString()

                                                binding.rain.text =
                                                    (forecastResponse.hourly[0].pop * 100).toString() + "%"
                                                binding.timeRv.layoutManager = LinearLayoutManager(
                                                    activity,
                                                    LinearLayoutManager.HORIZONTAL,
                                                    false
                                                )
                                                val adapter = TimeAdapter(tempUnit,forecastResponse.current.sunrise.toLong(),forecastResponse.current.sunset.toLong())
                                                binding.timeRv.adapter = adapter
                                                adapter.submitList(forecastResponse.hourly)
                                                binding.dayRv.layoutManager = LinearLayoutManager(
                                                    activity,
                                                    LinearLayoutManager.VERTICAL,
                                                    false
                                                )
                                                val adapter2 = DayAdapter(tempUnit)
                                                binding.dayRv.adapter = adapter2
                                                binding.dayRv.setHasFixedSize(true)
                                                adapter2.submitList(forecastResponse.daily)


                                            }
                                        }
                                        }
                                    }


                            }
                            else{
                            withContext(Dispatchers.Main) {
                                binding.loading.visibility = View.GONE
                                binding.linearLayout.visibility = View.GONE
                                binding.error.visibility = View.VISIBLE
                                Log.d("biiiiigggggg", result.exception.toString())
                            }}
                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                binding.loading.visibility = View.VISIBLE
                                binding.linearLayout.visibility = View.GONE
                                binding.error.visibility = View.GONE
                            }

                        }
                    }
                }


            }

        }
        val homeCityJob = lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {


                viewModel.locales.collect { localeResult ->
                    when (localeResult) {
                        is LocaleResult.Success -> {
                            Log.d("collect with inf", localeResult.data.toString())

                            withContext(Dispatchers.Main) {
                                cityName = localeResult.data[0]
                                if(language == Language.EN.name){
                                binding.cityName.text = localeResult.data[0].name.toString()

                                }
                                else{
                                    binding.cityName.text = localeResult.data[0].localNames?.ar.toString()

                            }}
                        }

                        is LocaleResult.Error -> {
                            Log.d("collect", localeResult.exception.toString())
                        }

                        is LocaleResult.Loading -> {
                            Log.d("collect", "loading")
                        }
                    }

                }

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcucateSunProgress(sunrise: Int, sunset: Int, time: Int): Int {
        var _sunrise = MainActivity.unixTimestampToDateTime(sunrise.toLong())
            .split(" ")[1].split(":")[0].toInt()
        var _sunset = MainActivity.unixTimestampToDateTime(sunset.toLong())
            .split(" ")[1].split(":")[0].toInt()
        var _time = MainActivity.unixTimestampToDateTime(time.toLong())
            .split(" ")[1].split(":")[0].toInt()

        Log.d(
            "calculate sun",
            (((_time.toDouble() - _sunrise.toDouble()) / (_sunset.toDouble() - _sunrise.toDouble()) * 100)).toString() + "\nsunrise: " + _sunrise + "\nsunset: " + _sunset + "\ntime: " + _time
        )
        return (((_time.toDouble() - _sunrise.toDouble()) / (_sunset.toDouble() - _sunrise.toDouble()) * 100)).toInt()
    }

    val getTime: (dt: Int) -> Unit = {

    }
    val getDay: (dt: Int) -> Unit = {}

    private fun isLocationEnabled(): Boolean {
        var locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkedPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
            return true
        return false
    }

    private fun enableLocationService() {

        val intent = android.content.Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }

    @Suppress("MissingPermission")
    private fun getFreshLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_LOW_POWER)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    viewModel.updateSettingsPreferences(
                        lat = locationResult.lastLocation?.latitude.toString(),
                        lon = locationResult.lastLocation?.longitude.toString()
                    )
                    Log.d("location result", locationResult.lastLocation?.latitude.toString())

                    //latitude.text = locationResult.lastLocation?.latitude.toString()
//                    //longitude.text = locationResult.lastLocation?.longitude.toString()
//                    val geocoder = Geocoder(requireContext()).getFromLocation(
//                        locationResult.lastLocation!!.latitude,
//                        locationResult.lastLocation!!.longitude,
//                        1
//                    )
                    //   address.text = geocoder?.firstOrNull()?.getAddressLine(0)
                }
            },
            Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("how we doing", requestCode.toString() + " " + permissions.toString() + " " + grantResults.toString())
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.size > 1 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
                Log.d("permission granted", "can navigate")
            }
            else{
                Log.d("permission denied", "can't navigate")
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_searchFragment)
        }}

    }
    fun celsiusToKelvin(celsius: Double): Double {
        return celsius + 273.15
    }

    fun celsiusToFahrenheit(celsius: Double): Double {
        return celsius * 9 / 5 + 32
    }
    fun metersPerSecondToMilesPerHour(mps: Double): Double {
        val metersInOneMile = 1609.34 // 1 mile = 1609.34 meters
        val secondsInOneHour = 3600.0 // 1 hour = 3600 seconds
        return mps * metersInOneMile / secondsInOneHour
    }
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For SDK version 23 and above
                val activeNetwork = connectivityManager.activeNetwork
                if (activeNetwork != null) {
                    return true
                }
            } else {
                // For SDK version below 23
                val activeNetwork = connectivityManager.activeNetworkInfo
                if (activeNetwork != null && activeNetwork.isConnected) {
                    return true
                }
            }
        }
        return false
    }
}