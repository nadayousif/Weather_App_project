package com.example.weatherappproject.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappproject.repositary.Repositary


class FavoriteViewModelFactory(private val repo: Repositary): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class not Found")
        }
    }
}