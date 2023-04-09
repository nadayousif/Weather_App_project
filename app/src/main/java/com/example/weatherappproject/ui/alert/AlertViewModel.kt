package com.example.weatherappproject.ui.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappproject.localData.AlertState
import com.example.weatherappproject.model.Alert
import com.example.weatherappproject.model.AlertSettings
import com.example.weatherappproject.repositary.RepositaryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertViewModel(private val repo: RepositaryInterface) : ViewModel() {
    var _currentAlert: MutableStateFlow<AlertState> =
        MutableStateFlow(AlertState.Loading)
    var currentAlert: StateFlow<AlertState> = _currentAlert

    fun deleteAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
            getAlertsDB()
        }
    }

    fun getAlertsDB() {
        viewModelScope.launch {
            repo.getAlerts().catch { e -> _currentAlert.value = AlertState.Fail(e) }
                .collectLatest {
                    _currentAlert.value = AlertState.Success(it)
                }
        }

    }

    fun insertAlertDB(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlert(alert)
            getAlertsDB()
        }
    }

    fun getAlertSettings(): AlertSettings? {
        return repo.getAlertSettings()
    }

    fun saveAlertSettings(alertSettings: AlertSettings) {
        repo.saveAlertSettings(alertSettings)
    }

}