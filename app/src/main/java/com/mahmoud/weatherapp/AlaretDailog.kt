package com.mahmoud.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mahmoud.weatherapp.model.Pojos.Daily
import com.mahmoud.weatherapp.model.db.LocalDataSource
import com.mahmoud.weatherapp.remote.RemoteDataSource
import com.mahmoud.weatherapp.viewmodel.RemoteViewModel
import com.mahmoud.weatherapp.viewmodel.RemoteViewModelFactory
import com.plcoding.alarmmanagerguide.AlarmItem
import com.plcoding.alarmmanagerguide.AndroidAlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class AlaretDailog(var cont: Context): DialogFragment() {
    lateinit var from:TextView
    lateinit var viewModel: RemoteViewModel
    lateinit var group:RadioGroup
    lateinit var withAlert:RadioButton
    lateinit var withoutAlert:RadioButton
    lateinit var save:Button
    lateinit var list:List<Daily>
     var days:Long=0
    var massage=""

    val scheduler = AndroidAlarmScheduler(cont)
    var alarmItem: AlarmItem? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val factory = RemoteViewModelFactory(Reposatory(LocalDataSource(requireContext()), RemoteDataSource(), ))

        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.aleart_dailog, null)
        list = listOf(arguments?.getSerializable("list0") as Daily,
            arguments?.getSerializable("list1") as Daily,
            arguments?.getSerializable("list2") as Daily,
            arguments?.getSerializable("list3") as Daily,
            arguments?.getSerializable("list4") as Daily,
        arguments?.getSerializable("list5") as Daily,
        arguments?.getSerializable("list6") as Daily,
        arguments?.getSerializable("list7") as Daily
        )


        from = view.findViewById(R.id.from)

        group = view.findViewById(R.id.radioGroup)
        withAlert = view.findViewById(R.id.with_alarm)
        withoutAlert = view.findViewById(R.id.without_alarm)
        save = view.findViewById(R.id.save)
        from.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
           val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build()).build()
            datePicker.show(requireActivity().supportFragmentManager,"")
            datePicker.addOnPositiveButtonClickListener {
                val data =datePicker.selection
//                for(i in list.indices-3){
//                    if(MainActivity.getDayOfWeek(data!!) == MainActivity.getDayOfWeek(list[i].dt.toLong())){
                        days = 0.toLong()
                        massage = list[0].weather[0].description
//                    }
//                }
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Alarm Time")
                    .build()
                timePicker.show(requireActivity().supportFragmentManager,"")
                timePicker.addOnPositiveButtonClickListener {
                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    from.text = "$hour :$minute\n${MainActivity.getDayOfWeek(days)}"

                    viewModel.getAllAlarm()
                    val getAlarmJob = lifecycleScope.launch(Dispatchers.IO) {

                            viewModel.alarms.collect {
                                if(alarmItem==null){
                                alarmItem = AlarmItem(
                                    it.size + 1,
                                    days,
                                    hour,
                                    minute,
                                    massage,
                                    arguments?.getString("name")!!
                                )
                                alarmItem?.let { it1 -> scheduler.schedule(it1) }
                                viewModel.insertToAlarm(alarmItem!!)
                            }}


                        }


                }
            }

        }


        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        return builder.create()
    }


}