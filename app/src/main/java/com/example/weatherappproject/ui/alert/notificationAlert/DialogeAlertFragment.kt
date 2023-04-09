package com.example.weatherappproject.ui.alert.notificationAlert

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.weatherappproject.R
import com.example.weatherappproject.databinding.FragmentDialogeAlertBinding
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.ui.alert.AlertViewModel
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.work.*
import com.example.weatherappproject.databinding.FragmentAlertBinding
import com.example.weatherappproject.databinding.FragmentDialogeBinding
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.model.AlertSettings
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.alert.AlertViewModelFactory
import com.example.weatherappproject.util.Constants
import com.example.weatherappproject.util.Utils
import com.example.weatherappproject.util.Utils.getCurrentDate
import com.example.weatherappproject.util.Utils.getCurrentTime
import com.example.weatherappproject.util.Utils.getCurrentTime2
import com.google.gson.Gson
import java.util.concurrent.TimeUnit


class DialogeAlertFragment :  DialogFragment()  {

    private var _binding: FragmentDialogeAlertBinding? = null
    private val binding get() = _binding!!
    lateinit var start_cal: Calendar
    lateinit var end_cal: Calendar
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var alertViewModel: AlertViewModel
    private lateinit var editor: SharedPreferences.Editor
    val ALERTSETTINGS = "ALERTSETTINGS"
    lateinit var alert: Alert


  

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val TAG = "DialogNotification"
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window!!.attributes
        //dialog!!.window!!.setBackgroundDrawableResource(androidx.work.R.color.light_gray)
        window.gravity = Gravity.BOTTOM
        dialog!!.window!!.attributes = window
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dialoge_alert, container, false)
        _binding= FragmentDialogeAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getDialog()?.requestWindowFeature(STYLE_NO_TITLE)
        setCancelable(true)
        sharedPreferences = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        alert = Alert(getCurrentTime().second, getCurrentTime2().second, 0.0, 0.0, "City")
        val repo = Repositary.getInstance(
            RemoteDataSource.getInstance(), LocalDataSource(requireActivity()),
            PreferenceManager.getDefaultSharedPreferences(context)
        )
        alertViewModel = ViewModelProvider(
            this.requireActivity(),
            AlertViewModelFactory(repo)
        )[AlertViewModel::class.java]
        val inputData = Data.Builder()
        inputData.putString(Constants.Alert, Gson().toJson(alert).toString())
        val alarm = alertViewModel.getAlertSettings()
        start_cal = Calendar.getInstance()
        end_cal = Calendar.getInstance()
        binding.startDate.text = getCurrentDate()
        alert.startDay = Calendar.getInstance().timeInMillis
        alert.endDay = Calendar.getInstance().timeInMillis
        binding.endDate.text = getCurrentDate()
        binding.fromTime.text = getCurrentTime().first
        binding.toTime.text = getCurrentTime2().first
        if (Utils.isOnline(requireContext())) {
            binding.countryDate.isEnabled = true
            binding.save.isEnabled = true
        } else {
            binding.countryDate.isEnabled = false
            binding.save.isEnabled = false
            Toast.makeText(requireContext(), "No Connection ", Toast.LENGTH_SHORT).show()
        }
        if (alarm?.isALarm == true && !alarm.isNotification) {
            binding.alarm.isChecked = true
        }
        if (alarm?.isALarm == false && alarm.isNotification) {
            binding.notification.isChecked = true
        }

        binding.fromTime.setOnClickListener {
            pickDateTime(binding.startDate, binding.fromTime, start_cal)
        }
        binding.toTime.setOnClickListener {
            pickDateTime(binding.endDate, binding.toTime, end_cal)
        }
        binding.countryDate.text =
            Utils.getAddressEnglish(requireContext(), alarm?.lat, alarm?.lon)
        binding.countryDate.setOnClickListener {
            val action =
                DialogeAlertFragmentDirections.actionDialogeAlertFragmentToMapsFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.save.setOnClickListener {
            var alert = Alert(
                startDay = start_cal.timeInMillis,
                endDay = end_cal.timeInMillis,
                lat = alarm!!.lat,
                lon = alarm.lon,
                AlertCityName = Utils.getAddressEnglish(requireContext(), alarm!!.lat, alarm.lon)
            )


            if (alert.startDay < alert.endDay) {
                if (binding.alarm.isChecked) {
                    alarm?.isALarm = true
                    alarm?.isNotification = false
                }
                if (binding.notification.isChecked) {
                    alarm?.isALarm = false
                    alarm?.isNotification = true
                }
                alarm?.let { alertViewModel.saveAlertSettings(it) }
                alertViewModel.insertAlertDB(alert)


                val inputData = Data.Builder()
                inputData.putString(Constants.Alert, Gson().toJson(alert).toString())

                val myConstraints: Constraints = Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                Toast.makeText(context, "Daily", Toast.LENGTH_SHORT).show()
                val myWorkRequest =
                    PeriodicWorkRequestBuilder<doWorker>(1, TimeUnit.DAYS).setConstraints(
                        myConstraints
                    ).setInputData(inputData.build()).addTag(alert.startDay.toString()).build()
                WorkManager.getInstance(requireContext().applicationContext)
                    .enqueueUniquePeriodicWork(
                        alert.startDay.toString(),
                        ExistingPeriodicWorkPolicy.REPLACE,
                        myWorkRequest
                    )
                dismiss()
            } else {

                Toast.makeText(
                    context,
                    "Please specify the end time of your alert",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //////////////////Cancel_Button///////////////////

        }

        binding.cancel.setOnClickListener {
            Log.i("TAG", "getLocalWeathers: errror")
            dismiss()
        }
        return root
    }


    private fun pickDateTime(tvdate: TextView, tvTime: TextView, calendar: Calendar) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        var pickedDateTime: Calendar
        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute)
                        tvdate.text = Utils.pickedDateFormatDate(pickedDateTime.time)
                        tvTime.text = Utils.pickedDateFormatTime(pickedDateTime.time)
                        calendar.time = pickedDateTime.time

                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

    fun saveAlertSettings(alertSettings: AlertSettings) {
        editor = sharedPreferences.edit()
        editor.putString(ALERTSETTINGS, Gson().toJson(alertSettings))
        editor.commit()
    }

    fun getAlertSettings(): AlertSettings? {
        val settingsStr = sharedPreferences.getString(ALERTSETTINGS, null)
        var alertSettings: AlertSettings? = AlertSettings()
        if (settingsStr != null) {
            alertSettings = Gson().fromJson(settingsStr, AlertSettings::class.java)

        }
        return alertSettings
    }


}