package com.example.weatherappproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappproject.repositary.Repositary

class HomeViewModelFactory (private val repo: Repositary) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}