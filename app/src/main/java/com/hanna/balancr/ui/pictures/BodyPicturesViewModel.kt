package com.hanna.balancr.ui.pictures

import android.app.Application
import androidx.lifecycle.*
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.BodyPicture
import kotlinx.coroutines.launch
import java.util.*

class BodyPicturesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BalancrDataBase.getInstance(application.applicationContext)

    val random = Random(1234)

    val bodyPictures = database.bodyPictureDao().getBodyPictures()

    fun addBodyPicture() {
        viewModelScope.launch {
            val bodyPicture = BodyPicture(date = Date(), picturePath = "x")
            database.bodyPictureDao().insert(bodyPicture)
        }
    }
}