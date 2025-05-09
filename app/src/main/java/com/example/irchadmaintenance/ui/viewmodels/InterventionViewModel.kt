package com.example.irchadmaintenance.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.models.Intervention
import com.example.irchadmaintenance.repository.InterventionRepository
import com.example.irchadmaintenance.ui.screens.InterventionType
import kotlinx.coroutines.launch
import java.time.LocalDate

class InterventionViewModel(private val repository: InterventionRepository) : ViewModel() {
    private val _interventions = mutableStateListOf<Intervention>()
    val interventions: List<Intervention> get() = _interventions

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    private val _success = mutableStateOf(false)
    val success: State<Boolean> get() = _success

    fun loadInterventions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getAllInterventions()
                _interventions.clear()
                _interventions.addAll(result)
            } catch (e: Exception) {
                Log.e("InterventionViewModel", "Error loading interventions", e)
                _error.value = e.message ?: "Failed to load interventions"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadInterventionsByStatus(status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getInterventionsByStatus(status)
                _interventions.clear()
                _interventions.addAll(result)
            } catch (e: Exception) {
                Log.e("InterventionViewModel", "Error loading interventions by status", e)
                _error.value = e.message ?: "Failed to load interventions"
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createIntervention(
        userId: Int,
        selectedDate: LocalDate,
        title: String,
        location: String,
        description: String,
        interventionType: InterventionType
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _success.value = false

            try {
                val intervention = repository.createIntervention(
                    userId = userId,
                    scheduledDate = selectedDate,
                    title = title,
                    location = location,
                    description = description,
                    interventionType = interventionType
                )

                _success.value = true
                loadInterventions() // Refresh the interventions list
            } catch (e: Exception) {
                Log.e("InterventionViewModel", "Error creating intervention", e)
                _error.value = e.message ?: "Failed to create intervention"
                _success.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _success.value = false
    }

    fun completeIntervention(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.completeIntervention(id)
                loadInterventions() // Refresh the list
            } catch (e: Exception) {
                Log.e("InterventionViewModel", "Error completing intervention", e)
                _error.value = e.message ?: "Failed to complete intervention"
            } finally {
                _isLoading.value = false
            }
        }
    }
}