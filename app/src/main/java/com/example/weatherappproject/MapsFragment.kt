package com.example.weatherappproject

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment



import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.weatherappproject.databinding.FragmentMapsBinding
import com.example.weatherappproject.util.ConnectionUtils.checkConnection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap: GoogleMap
    var lat :Double = 0.0
    var lon :Double = 0.0
    lateinit var location : LatLng
    val args: MapsFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            goToLatLng(it.latitude,it.longitude,16f)
            lat = location.latitude
            lon = location.latitude
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapsBinding.inflate(inflater,container,false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        mapInitialize()
        binding.goBtn.setOnClickListener {

            if (args.fromFav ==0){
                Log.i("nada", "${lat},${lon}")
                val action = MapsFragmentDirections.actionMapsFragmentToNavFavorite().setFav(true).setLatFav(lat.toString()).setLonFav(lon.toString())
                findNavController().navigate(action)
            }
             else{
                Log.i("home", "${lat},${lon}")
            val action = MapsFragmentDirections.actionMapsFragmentToNavHome(
                lat.toString(),
                lon.toString()
            ).setMap(true)
            findNavController().navigate(action)
        }
           }

        return binding.root
    }

    private fun mapInitialize() {
        val locationRequest : LocationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setSmallestDisplacement(14f)
        locationRequest.setFastestInterval(3000)
        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                if (!binding.searchEditText.text.isNullOrEmpty())
                    goToSearchLocation()
            }
            false
        }
        binding.searchBtn.setOnClickListener { if(!binding.searchEditText.text.isNullOrEmpty())goToSearchLocation()}

        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun goToLatLng(latitude: Double,longitude:Double, float: Float) {
        var name = "Unknown "
        if(checkConnection()) {
            var geocoder = Geocoder(requireContext()).getFromLocation(latitude, longitude, 1)
            if (geocoder!!.size > 0)
                name = "${geocoder?.get(0)?.subAdminArea} , ${geocoder?.get(0)?.adminArea}"
            var latLng = LatLng(latitude, longitude)
            var update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, float)
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.animateCamera(update)
        }

    }
    private fun goToSearchLocation(){
        var searchLocation = binding.searchEditText.text.toString()
        if(checkConnection()){
        var list = Geocoder(requireContext()).getFromLocationName(searchLocation,1)
        if (list!= null && list.size>0) {
            var address: Address = list.get(0)
            lat = address.latitude
            lon = address.longitude
            goToLatLng(address.latitude, address.latitude, 16f)
        }
        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}