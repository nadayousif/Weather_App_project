package com.example.weatherappproject.localData

import com.example.weatherappproject.model.Alert

sealed class AlertState {
    class Success(var data: List<Alert>?):AlertState()
    class Fail(val msg : Throwable):AlertState()
    object Loading :AlertState()
}