package com.mahmoud.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.mahmoud.weatherapp.databinding.FragmentMyDialogBinding
import kotlin.math.log
class MyDialogFragment : Fragment(), DateTimePickerFragment.DateTimePickerListener {
    private lateinit var binding: FragmentMyDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listeners for buttons
        binding.buttonDate.setOnClickListener {
            showDatePicker()
        }

        binding.buttonTime.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {
        val fragmentManager: FragmentManager = childFragmentManager
        val datePicker = DateTimePickerFragment(this)
        datePicker.show(fragmentManager, DateTimePickerFragment.TAG_DATE_PICKER)
    }

    private fun showTimePicker() {
        val fragmentManager: FragmentManager = childFragmentManager
        val timePicker = DateTimePickerFragment(this)
        timePicker.show(fragmentManager, DateTimePickerFragment.TAG_TIME_PICKER)
    }

    override fun onDateSelected(date: String) {
        // Update the date TextView
        binding.textViewDate.text = "Selected Date: $date"
    }

    override fun onTimeSelected(time: String) {
        // Update the time TextView
        binding.textViewTime.text = "Selected Time: $time"
    }
}
