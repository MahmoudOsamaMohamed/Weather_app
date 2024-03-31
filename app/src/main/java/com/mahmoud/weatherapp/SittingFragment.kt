package com.mahmoud.weatherapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mahmoud.weatherapp.databinding.FragmentSittingBinding
import com.mahmoud.weatherapp.model.Language
import com.mahmoud.weatherapp.model.Location
import com.mahmoud.weatherapp.model.SpeedUnit
import com.mahmoud.weatherapp.model.TempUnit
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


class SittingFragment : Fragment() {
    lateinit var binding: FragmentSittingBinding
    lateinit var viewModel: RemoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSittingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = RemoteViewModelFactory(Reposatory(LocalDataSource(requireContext()), RemoteDataSource(), ))

        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)
        viewModel.readSettingsPreference()
        lifecycleScope.launch(Dispatchers.IO) {
           viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingsPreferences.collect {

                    when(it.tempUnit){
                        TempUnit.C.name -> {binding.celcous.setBackgroundColor( resources.getColor(R.color.sunny))
                            binding.klevin.setBackgroundColor( resources.getColor(R.color.white))
                            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.white))

                        }
                        TempUnit.F.name ->{binding.celcous.setBackgroundColor( resources.getColor(R.color.white))
                            binding.klevin.setBackgroundColor( resources.getColor(R.color.white))
                            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.sunny))

                        }
                        else -> {binding.celcous.setBackgroundColor( resources.getColor(R.color.white))
                            binding.klevin.setBackgroundColor( resources.getColor(R.color.sunny))
                            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.white))

                        }

                    }
                    when(it.speedUnit){
                        SpeedUnit.MPH.name -> {binding.mph.setBackgroundColor( resources.getColor(R.color.sunny))
                            binding.mps.setBackgroundColor( resources.getColor(R.color.white))
                        }
                        SpeedUnit.KMPH.name -> {binding.mph.setBackgroundColor( resources.getColor(R.color.white))
                            binding.mps.setBackgroundColor( resources.getColor(R.color.sunny))
                        }

                    }
                    when(it.language){
                        Language.EN.name -> {binding.english.setBackgroundColor( resources.getColor(R.color.sunny))
                            binding.arabic.setBackgroundColor( resources.getColor(R.color.white))
                        }
                        Language.AR.name -> {binding.english.setBackgroundColor( resources.getColor(R.color.white))
                            binding.arabic.setBackgroundColor( resources.getColor(R.color.sunny))
                        }

                    }
                    when(it.location) {
                        Location.ON.name -> {
                            binding.gps.setBackgroundColor(resources.getColor(R.color.sunny))
                            binding.map.setBackgroundColor(resources.getColor(R.color.white))

                        }

                        Location.OFF.name -> {
                            binding.gps.setBackgroundColor(resources.getColor(R.color.white))
                            binding.map.setBackgroundColor(resources.getColor(R.color.sunny))

                        }

                    }}}

            }

        binding.celcous.setOnClickListener {
            binding.celcous.setBackgroundColor( resources.getColor(R.color.sunny))
            binding.klevin.setBackgroundColor( resources.getColor(R.color.white))
            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.white))
            viewModel.updateSettingsPreferences(tempUnit = TempUnit.C)
        }
        binding.klevin.setOnClickListener {
            binding.celcous.setBackgroundColor( resources.getColor(R.color.white))
            binding.klevin.setBackgroundColor( resources.getColor(R.color.sunny))
            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.white))
            viewModel.updateSettingsPreferences(tempUnit = TempUnit.K)
        }
        binding.fehrenheit.setOnClickListener {
            binding.celcous.setBackgroundColor( resources.getColor(R.color.white))
            binding.klevin.setBackgroundColor( resources.getColor(R.color.white))
            binding.fehrenheit.setBackgroundColor( resources.getColor(R.color.sunny))
            viewModel.updateSettingsPreferences(tempUnit = TempUnit.F)
        }
        binding.mph.setOnClickListener {
            binding.mph.setBackgroundColor( resources.getColor(R.color.sunny))
            binding.mps.setBackgroundColor( resources.getColor(R.color.white))
            viewModel.updateSettingsPreferences(speedUnit = SpeedUnit.MPH)
        }
        binding.mps.setOnClickListener {
            binding.mph.setBackgroundColor( resources.getColor(R.color.white))
            binding.mps.setBackgroundColor( resources.getColor(R.color.sunny))
            viewModel.updateSettingsPreferences(speedUnit = SpeedUnit.KMPH)
        }
        binding.english.setOnClickListener {
            binding.english.setBackgroundColor( resources.getColor(R.color.sunny))
            binding.arabic.setBackgroundColor( resources.getColor(R.color.white))
            viewModel.updateSettingsPreferences(language = Language.EN)
            val locale = Locale("en")
            Locale.setDefault(locale)
            val configuration = Configuration(resources.configuration)
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            requireActivity().recreate()
        }
        binding.arabic.setOnClickListener {
            binding.english.setBackgroundColor(resources.getColor(R.color.white))
            binding.arabic.setBackgroundColor(resources.getColor(R.color.sunny))
            viewModel.updateSettingsPreferences(language = Language.AR)
            val locale = Locale("ar")
            Locale.setDefault(locale)
            val configuration = Configuration(resources.configuration)
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            requireActivity().recreate()
        }
        binding.gps.setOnClickListener {
            binding.gps.setBackgroundColor( resources.getColor(R.color.sunny))
            binding.map.setBackgroundColor( resources.getColor(R.color.white))
            viewModel.updateSettingsPreferences(location = Location.ON)
        }
        binding.map.setOnClickListener {
            binding.gps.setBackgroundColor( resources.getColor(R.color.white))
            binding.map.setBackgroundColor( resources.getColor(R.color.sunny))
            viewModel.updateSettingsPreferences(location = Location.OFF)
            viewModel.updateSettingsPreferences(lat = "",lon="")
            viewModel.updateSettingsPreferences(lat = "",lon="")
        }
    }



}