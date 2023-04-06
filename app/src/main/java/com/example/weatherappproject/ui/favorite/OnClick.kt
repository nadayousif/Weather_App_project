package com.example.weatherappproject.ui.favorite

import com.example.weatherappproject.model.FavoriteAddress

interface OnClick {
    fun onDeleteClickFavorites(favoriteAddress: FavoriteAddress)
    fun sendData(lat:Double,lon:Double)
}