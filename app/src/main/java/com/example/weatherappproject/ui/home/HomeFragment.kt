package com.example.weatherappproject.ui.home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherappproject.DialogeFragmentDirections
import com.example.weatherappproject.databinding.FragmentHomeBinding
import com.example.weatherappproject.localData.*
import com.example.weatherappproject.model.Current
import com.example.weatherappproject.model.Daily
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.model.WeatherData
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.util.ConnectionUtils.checkConnection
import com.example.weatherappproject.util.MySharedPreference
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


const val PERMISSION_ID = 44
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var myViewModel: HomeViewModel
    lateinit var myViewModelFactory: HomeViewModelFactory
    private lateinit var fusedClient : FusedLocationProviderClient
    lateinit var geocoder: Geocoder
    lateinit var weatherData: WeatherData
    val args:HomeFragmentArgs by navArgs()

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
        val pd = ProgressDialog(requireActivity())

        geocoder = Geocoder(requireActivity(), Locale.getDefault())
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        myViewModelFactory = HomeViewModelFactory(Repositary.getInstance(RemoteDataSource.getInstance(),
            LocalDataSource.getInstance(requireContext())))
        myViewModel =
            ViewModelProvider(this.requireActivity(), myViewModelFactory)[HomeViewModel::class.java]

            //myViewModel.getWeatherDataFromDB()


        //(myViewModel as HomeViewModel).getWeatherFromApi(31.2001,29.9187,"eng")


        lifecycleScope.launch {
            myViewModel.WeatherFromDb.collect {
                when(it){
                    is DatabaseWeatherState.Loading ->{

                        pd.setMessage("loading")
                        pd.show()


                    }
                    is DatabaseWeatherState.SuccessHome->{
                        pd.dismiss()
                        if (it!=null){
                            _binding?.areaText?.text = it.data?.timezone
                            _binding?.tempText?.text = it.data?.current?.temp.toString()
                            val dayhome = getCurrentDay(it.data?.current?.dt!!.toInt())
                            _binding?.statusText?.text = it.data?.current?.weather?.get(0)?.description
                            _binding?.dateText?.text = dayhome
                            _binding?.cloudText?.text = it.data?.current?.clouds.toString()
                            binding.humidity.text = it.data?.current?.humidity.toString() + "%"
                            _binding?.windText?.text = it.data?.current.wind_speed.toString()
                            _binding?.pressureText?.text = it.data?.current.pressure.toString()
                            _binding?.ultraText?.text = it.data?.current.dt.toString()
                            _binding?.visibiltyText?.text = it.data?.current.temp.toString()
                           /* Glide.with(requireActivity())
                                .load("https://openweathermap.org/img/wn/${it.data?.current.weather.get(0).icon}@2x.png")
                                .into(binding.statusImage)*/

                            binding.daysWaether.apply {
                                layoutManager = LinearLayoutManager(context)
                                this.adapter = DayAdapter(it.data.daily)
                            }

                            binding.hourlyWeather.apply {
                                layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                this.adapter = HoursAdapter(it.data.hourly)
                            }
                        }

                    }
                    is DatabaseWeatherState.Failure ->{
                        Log.i("TAG", "onCreateView: ${it.msg}")
                    }
                    else->{

                        pd.dismiss()
                        //Toast.makeText(this@FavoriteFragment,"Can't handle your request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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
                            weatherData= it.data.body()!!
                            Log.i("home nada", "onCreateView: ${it.data.body()!!.lat} long  ${it.data.body()!!.lon}" )
                            myViewModel.insertWeatherData(weatherData)

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
                weatherData=it
                Log.i("home nada", "onCreateView: ${it.lat} long  ${it.lon}" )
                myViewModel.insertWeatherData(weatherData)

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


    override fun onResume() {
        super.onResume()
        if(checkConnection()){

            if (args.map && args!=null){
                Log.i("tag", "hi "+args.lat+" "+args.lon)
                (myViewModel as HomeViewModel).getWeatherFromApi(args.lat.toDouble(),
                    args.lon.toDouble(), MySharedPreference.getLanguage(),MySharedPreference.getUnits())
            }
            /*else if(MySharedPreference.getWeatherFromMap()){

            }*/
            else {
                getLastLocation()

            }
        }else{
            myViewModel.getWeatherDataFromDB()
        }

    }

    private fun checkPermissions(): Boolean {
        return context?.let {
            ActivityCompat.checkSelfPermission(
                it, android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED ||
                context?.let {
                    ActivityCompat.checkSelfPermission(
                        it, android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result

                    // if(location==null){
                    requestNewLocationData()
                    Log.i("nada", location?.latitude.toString())
                    Log.i("nada", location?.longitude.toString())
                    if (location != null) {
                       /* homeViewModel.getWeatherData(
                            location.latitude, location.longitude,
                            "exclude",
                            "5e0f6f10e9b7bf48be1f3d781c3aa597"
                        )*/
                        (myViewModel as HomeViewModel).getWeatherFromApi(
                            location.latitude, location.longitude,
                            MySharedPreference.getLanguage(),MySharedPreference.getUnits()
                        )
                    }

                }
            } else {
                // Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    private fun requestNewLocationData(){

        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        )
            println("mmm")
        Looper.myLooper()?.let {
            fusedClient.requestLocationUpdates(
                mLocationRequest, mLocationCallBack, it
            )
        }

    }

    private val mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            val mLastLocation: Location = locationResult.lastLocation
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