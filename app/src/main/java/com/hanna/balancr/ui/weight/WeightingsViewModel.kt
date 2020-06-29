package com.hanna.balancr.ui.weight

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hanna.balancr.model.BalancrDataBase

class WeightingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BalancrDataBase.getInstance(application.applicationContext)
    val weightings = database.weightingDao().getCurrentUserWeightings()
}