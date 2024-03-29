package com.mahmoud.weatherapp

import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.weatherapp.databinding.FragmentFavorateBinding
import com.mahmoud.weatherapp.model.db.Favourate
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavorateFragment : Fragment() {
    lateinit var binding: FragmentFavorateBinding
    lateinit var viewModel: RemoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavorateBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = RemoteViewModelFactory(Reposatory(LocalDataSource(requireContext()), RemoteDataSource()))
        val favAdapter = FavAdapter(delete,goToFavorite)
        binding.addLove.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(FavorateFragmentDirections.actionFavorateFragmentToSearchFragment("favorate"))

        }

        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)
         viewModel.getAllFavourates()
        val getFavoritesJob = lifecycleScope.launch(Dispatchers.IO) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favourate.collect {
withContext(Dispatchers.Main) {
                    binding.favRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.favRv.adapter = favAdapter
                    favAdapter.submitList(it)
                }}
            }
        }
    }
val delete : (Favourate) -> Unit = {
    viewModel.deleteFromFavorites(it)
}
    val goToFavorite:(Favourate) -> Unit = {
        NavHostFragment.findNavController(this).navigate(FavorateFragmentDirections.actionFavorateFragmentToHomeFragment(lat = it.lat,lon = it.lon))
    }

}