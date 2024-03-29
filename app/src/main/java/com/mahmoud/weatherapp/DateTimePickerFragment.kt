package com.mahmoud.weatherapp
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

import java.util.*

class DateTimePickerFragment(private val listener: DateTimePickerListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return when (tag) {
            TAG_DATE_PICKER -> {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(requireContext(), dateSetListener, year, month, day)
            }
            TAG_TIME_PICKER -> {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(requireContext(), timeSetListener, hour, minute, true)
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            listener.onDateSelected(selectedDate)
        }

    private val timeSetListener =
        TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val selectedTime = "$hourOfDay:$minute"
            listener.onTimeSelected(selectedTime)
        }

    companion object {
        const val TAG_DATE_PICKER = "datePicker"
        const val TAG_TIME_PICKER = "timePicker"
    }

    interface DateTimePickerListener {
        fun onDateSelected(date: String)
        fun onTimeSelected(time: String)
    }
}

