package com.example.weatherappproject.ui.favorite

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherappproject.databinding.FragmentFavoriteBinding
import com.example.weatherappproject.localData.ApiState
import com.example.weatherappproject.localData.DatabaseState
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.home.DayAdapter
import com.example.weatherappproject.ui.home.HomeFragmentArgs
import com.example.weatherappproject.ui.home.HomeViewModel
import com.example.weatherappproject.ui.home.HoursAdapter
import com.example.weatherappproject.util.MySharedPreference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment(), OnClick {


    lateinit var myViewModel: FavoriteViewModel
    lateinit var myViewModelFactory: FavoriteViewModelFactory
    lateinit var favoriteAdapter: FavoriteAdapter
    private var _binding: FragmentFavoriteBinding? = null
    lateinit var fav :FavoriteAddress
    private val binding get() = _binding!!
    val args: FavoriteFragmentArgs by navArgs()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // val favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val pd = ProgressDialog(requireActivity())
        //Factory
        myViewModelFactory = FavoriteViewModelFactory(
            Repositary.getInstance(
                RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(requireContext())
            )
        )

        myViewModel = ViewModelProvider(requireActivity(), myViewModelFactory)[FavoriteViewModel::class.java]
        fav = FavoriteAddress("",0.0,0.0,"",0.0,"",0,"")
            favoriteAdapter = FavoriteAdapter(listOf(), this)

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
                            fav.latitude=it.data.body()!!.lat
                            fav.longitude= it.data.body()!!.lon
                            fav.address= it.data.body()!!.timezone.toString()
                            fav.currentDescription= it.data.body()!!.current.toString()
                            fav.latlngString= it.data.body()!!.lat.toString()+it.data.body()!!.lon.toString()
                            fav.currentTemp=it.data.body()!!.current.temp


                            (myViewModel as FavoriteViewModel).insertFavorite( fav)//nada
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

        /*(myViewModel).currentWeather.observe(viewLifecycleOwner){

            fav.latitude=it.lat
            fav.longitude= it.lon
            fav.address= it.timezone.toString()
            fav.currentDescription= it.current.toString()
            fav.latlngString= it.lat.toString()+it.lon.toString()
            fav.currentTemp=it.current.temp


            (myViewModel as FavoriteViewModel).insertFavorite( fav)//nada

        }*/
        binding.favoriteRv.adapter = favoriteAdapter
        binding.favoriteRv.layoutManager = LinearLayoutManager(context)
        binding.floatingButton.setOnClickListener {
            val action =
                FavoriteFragmentDirections.actionNavFavoriteToMapsFragment().setFromFav(0)
            Navigation.findNavController(it).navigate(action)
        }


        lifecycleScope.launch {
            myViewModel.favoriteWeather.collectLatest {
                when(it){
                    is DatabaseState.Loading ->{

                        pd.setMessage("loading")
                        pd.show()


                    }
                    is DatabaseState.Success->{
                        pd.dismiss()
                        favoriteAdapter.setList(it.data)
                    }
                    else->{
                        pd.dismiss()
                        //Toast.makeText(this@FavoriteFragment,"Can't handle your request", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

       /* myViewModel.favoriteWeather.observe(viewLifecycleOwner) {
            favoriteAdapter.setList(it)

            binding.favoriteRv.adapter = favoriteAdapter
            binding.favoriteRv.layoutManager = LinearLayoutManager(context)
            binding.floatingButton.setOnClickListener {
                val action =
                    FavoriteFragmentDirections.actionNavFavoriteToMapsFragment().setFromFav(0)
                Navigation.findNavController(it).navigate(action)
            }

        }*/



        return root
    }
    override fun onResume() {
        super.onResume()
        if (args.fav){
            Log.i("tag", "hi "+args.latFav+" "+args.lonFav)
            (myViewModel as FavoriteViewModel).getWeatherFromApi(args.latFav.toDouble(),
                args.lonFav.toDouble(),"eng", "default")


        }else{
            (myViewModel as FavoriteViewModel).getAllFavorite()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDeleteClickFavorites(city: FavoriteAddress) {
        myViewModel.deleteFavorite(city)
    }
    override fun sendData(lat: Double, long: Double) {
        val action=FavoriteFragmentDirections.actionNavFavoriteToDetailsFragment().setDetailsLat(lat.toString()).setDetailsLon(
            long.toString()
        )
        Navigation.findNavController(requireView()).navigate(action)
    }
}