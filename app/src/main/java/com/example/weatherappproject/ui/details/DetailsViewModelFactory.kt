package com.example.weatherappproject.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappproject.repositary.Repositary
import com.example.weatherappproject.ui.home.HomeViewModel



class DetailsViewModelFactory (private val repo: Repositary) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            DetailsViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}