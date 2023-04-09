package com.example.weatherappproject.ui.details

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.weatherappproject.databinding.FragmentDetailsBinding
import com.example.weatherappproject.localData.ApiState
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.home.*
import com.example.weatherappproject.util.ConnectionUtils.checkConnection
import com.example.weatherappproject.util.MySharedPreference
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    lateinit var myViewModel: DetailsViewModel
    lateinit var myViewModelFactory: DetailsViewModelFactory
    private lateinit var fusedClient : FusedLocationProviderClient
    lateinit var geocoder: Geocoder
    var latitude = 0.0
    var longitude = 0.0
    val args: DetailsFragmentArgs by navArgs()
    lateinit var sharedPreferences: SharedPreferences


    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        latitude=args.detailsLat.toDouble()
        longitude=args.detailsLon.toDouble()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val pd = ProgressDialog(requireActivity())

        myViewModelFactory = DetailsViewModelFactory(
            Repositary.getInstance(
                RemoteDataSource.getInstance(),
            LocalDataSource.getInstance(requireContext()),
                PreferenceManager.getDefaultSharedPreferences(requireContext())))
        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[DetailsViewModel::class.java]
       // (myViewModel as HomeViewModel).getWeatherFromApi(latitude,longitude,"eng")
        lifecycleScope.launch {
            (myViewModel).currentWeather.collectLatest {
                when (it) {
                    is ApiState.Loading -> {

                        pd.setMessage("loading")
                        pd.show()
                    }
                    is ApiState.Success -> {
                        pd.dismiss()
                        if (it.data.body() != null) {
                            Log.i("home nada", "onCreateView: ${it.data.body()!!.lat} long  ${it.data.body()!!.lon}" )
                            _binding?.areaText?.text = it.data.body()!!.timezone
                            _binding?.tempText?.text = it.data.body()!!.current.temp.toString()
                            val dayhome = getCurrentDay(it.data.body()!!.current.dt.toInt())
                            _binding?.statusText?.text = it.data.body()!!.current.weather.get(0).description
                            _binding?.dateText?.text = dayhome
                            _binding?.cloudText?.text = it.data.body()!!.current.clouds.toString()
                            binding.humidity.text = it.data.body()!!.current.humidity.toString() + "%"
                            _binding?.windText?.text = it.data.body()!!.current.wind_speed.toString()
                            _binding?.pressureText?.text = it.data.body()!!.current.pressure.toString()
                            _binding?.ultraText?.text = it.data.body()!!.current.dt.toString()
                            _binding?.visibiltyText?.text = it.data.body()!!.current.temp.toString()
                            Glide.with(requireActivity())
                                .load("https://openweathermap.org/img/wn/${it.data.body()!!.current.weather.get(0).icon}@2x.png")
                                .into(binding.statusImage)

                            binding.daysWaether.apply {
                                layoutManager = LinearLayoutManager(context)
                                this.adapter = DayAdapter(it.data.body()!!.daily)
                            }

                            binding.hourlyWeather.apply {
                                layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                this.adapter = HoursAdapter(it.data.body()!!.hourly)
                            }
                        }
                    }
                    else -> {
                        pd.dismiss()
                        Toast.makeText(
                            requireActivity(),
                            "Can't handle your request",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        /*(myViewModel).currentWeather.observe(viewLifecycleOwner) {
            if (it != null) {
                _binding?.areaText?.text = it.timezone
                _binding?.tempText?.text = it.current.temp.toString()
                val dayhome = getCurrentDay(it.current.dt.toInt())
                _binding?.statusText?.text = it.current.weather.get(0).description
                _binding?.dateText?.text = dayhome
                _binding?.cloudText?.text = it.current.clouds.toString()
                binding.humidity.text = it.current.humidity.toString() + "%"
                _binding?.windText?.text = it.current.wind_speed.toString()
                _binding?.pressureText?.text = it.current.pressure.toString()
                _binding?.ultraText?.text = it.current.dt.toString()
                _binding?.visibiltyText?.text = it.current.temp.toString()
                Glide.with(requireActivity())
                    .load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png")
                    .into(binding.statusImage)

                binding.daysWaether.apply {
                    layoutManager = LinearLayoutManager(context)
                    this.adapter = DayAdapter(it.daily)
                }

                binding.hourlyWeather.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    this.adapter = HoursAdapter(it.hourly)
                }
            }

        }*/


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()

            Log.i("tag", "hi "+args.detailsLat+" "+args.detailsLon)
        if(checkConnection()) {
            (myViewModel as DetailsViewModel).getWeatherFromApi(
                args.detailsLat.toDouble(),
                args.detailsLon.toDouble(), MySharedPreference.getLanguage(),
                MySharedPreference.getUnits()
            )
        }

    }

    fun getCurrentDay( dt: Int) : String{
        var date= Date(dt*1000L)
        var sdf= SimpleDateFormat("d")
        sdf.timeZone= TimeZone.getDefault()
        var formatedData=sdf.format(date)
        var intDay=formatedData.toInt()
        var calendar= Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,intDay)
        var format= SimpleDateFormat("EEEE")
        return format.format(calendar.time)
    }
    fun formatTime(dateObject: Long?): String {
        val date = Date(dateObject!! * 1000L)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }



}