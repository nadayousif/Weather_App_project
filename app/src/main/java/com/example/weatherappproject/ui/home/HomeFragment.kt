package com.example.weatherappproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherappproject.databinding.FragmentHomeBinding
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var myViewModel: ViewModel
    lateinit var myViewModelFactory: HomeViewModelFactory
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        myViewModelFactory = HomeViewModelFactory(Repositary.getInstance(RemoteDataSource.getInstance()))
        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[HomeViewModel::class.java]
        (myViewModel as HomeViewModel).getWeatherFromApi(31.2001,29.9187,"eng")
        (myViewModel as HomeViewModel).currentWeather.observe(viewLifecycleOwner) {
            _binding?.areaText?.text = it.timezone
            _binding?.tempText?.text = it.current.temp.toString()
            val dayhome=getCurrentDay(it.current.dt.toInt())
            _binding?.statusText?.text = it.current.weather.get(0).description
            _binding?.dateText?.text= dayhome
            _binding?.cloudText?.text= it.current.clouds.toString()
            binding.humidity.text =  it.current.humidity.toString() + "%"
            _binding?.windText?.text = it.current.wind_speed.toString()
            _binding?.pressureText?.text = it.current.pressure.toString()
            _binding?.ultraText?.text = it.current.dt.toString()
            _binding?.visibiltyText?.text = it.current.temp.toString()
            Glide.with(requireActivity()).load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png").into(binding.statusImage)

            binding.daysWaether.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = DayAdapter(it.daily)
            }

            binding.hourlyWeather.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                this.adapter = HoursAdapter(it.hourly)
            }


        }

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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