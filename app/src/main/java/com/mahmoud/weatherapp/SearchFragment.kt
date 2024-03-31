package com.mahmoud.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.weatherapp.databinding.FragmentSearchBinding
import com.mahmoud.weatherapp.model.Pojos.Features
import com.mahmoud.weatherapp.model.Pojos.LocaleResponse
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.model.result.CityResult
import com.mahmoud.weatherapp.model.result.LocaleResult
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: RemoteViewModel
    var list= mutableListOf<Features>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = RemoteViewModelFactory(Reposatory(LocalDataSource(requireContext()), RemoteDataSource(), ))

        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.resultRv.visibility = View.VISIBLE
                binding.descoverAnim.visibility = View.GONE
                newText?.let {
                    Log.d("texttttto",it)
                    viewModel.searchCity(it,"10")
                }
                return true
            }
        })
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter =  SearchAdapter(goToResult)
        binding.resultRv.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestions.collect {

               when(it){
                   is CityResult.Success -> {
                       withContext(Dispatchers.Main) {

                           adapter.submitList(it.data.features)
                           Log.d("I'm tired boss",it.data.features.toString())
                       }
                   }
                   is CityResult.Error -> {
                        Log.d("I'm tired boss",it.toString())

                }
                   else-> {

               }

               }
               }


                             }

        }
    }
    val goToResult: (Features) -> Unit = {
        if(!SearchFragmentArgs.fromBundle(requireArguments()).from.isNullOrEmpty()&&SearchFragmentArgs.fromBundle(requireArguments()).from=="home"){
            viewModel.updateSettingsPreferences(lat=it.geometry.coordinates[1].toString(),lon=it.geometry.coordinates[0].toString())
        }
        NavHostFragment.findNavController(this).navigate(SearchFragmentDirections.actionSearchFragmentToHomeFragment(lon = it.geometry.coordinates[0].toString(), lat = it.geometry.coordinates[1].toString()))
    }
}