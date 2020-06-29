package com.hanna.balancr.ui.weight.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.Weighting
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AddWeightingViewModel(application: Application) : AndroidViewModel(application) {
    enum class AddWeightingState {
        InProgress, WithError, Finished
    }

    val addWeightingState = MutableLiveData(AddWeightingState.InProgress)

    private val database = BalancrDataBase.getInstance(application.applicationContext)
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

    fun clearState() {
        addWeightingState.value = AddWeightingState.InProgress
    }

    fun saveWeight(weight: Float?, date: String?) {
        if (weight == null || weight <= 0.0f || date == null) {
            addWeightingState.value = AddWeightingState.WithError
            return
        }
        val parsedDate = dateFormatter.parse(date)
        if (parsedDate == null) {
            addWeightingState.value = AddWeightingState.WithError
            return
        }
        viewModelScope.launch {
            val currentUser = database.userDao().currentUser() ?: return@launch
            val weighting = Weighting(weight = weight, date = parsedDate, userId = currentUser.id)
            database.weightingDao().insert(weighting)
            addWeightingState.value = AddWeightingState.Finished
        }
    }
}