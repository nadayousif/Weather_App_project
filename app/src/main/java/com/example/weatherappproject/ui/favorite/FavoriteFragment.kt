package com.example.weatherappproject.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappproject.databinding.FragmentFavoriteBinding
import com.example.weatherappproject.localData.LocalDataSource
import com.example.weatherappproject.model.FavoriteAddress
import com.example.weatherappproject.remoteData.RemoteDataSource
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.home.HomeFragmentArgs
import com.example.weatherappproject.ui.home.HomeViewModel


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
        (myViewModel).currentWeather.observe(viewLifecycleOwner){

            fav.latitude=it.lat
            fav.longitude= it.lon
            fav.address= it.timezone.toString()
            fav.currentDescription= it.current.toString()
            fav.latlngString= it.lat.toString()+it.lon.toString()
            fav.currentTemp=it.current.temp


            (myViewModel as FavoriteViewModel).insertFavorite( fav)//nada

        }

        myViewModel.favoriteWeather.observe(viewLifecycleOwner) {
            favoriteAdapter.setList(it)

            binding.favoriteRv.adapter = favoriteAdapter
            binding.favoriteRv.layoutManager = LinearLayoutManager(context)
            binding.floatingButton.setOnClickListener {
                val action =
                    FavoriteFragmentDirections.actionNavFavoriteToMapsFragment().setFromFav(0)
                Navigation.findNavController(it).navigate(action)
            }

        }



        return root
    }
    override fun onResume() {
        super.onResume()
        if (args.fav){
            Log.i("tag", "hi "+args.latFav+" "+args.lonFav)
            (myViewModel as FavoriteViewModel).getWeatherFromApi(args.latFav.toDouble(),
                args.lonFav.toDouble(),"eng")


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
}