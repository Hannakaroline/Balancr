package com.hanna.balancr.ui.pictures

import android.app.Application
import androidx.lifecycle.*
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.BodyPicture
import com.hanna.balancr.model.entities.Weighting
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class BodyPicturesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BalancrDataBase.getInstance(application.applicationContext)
    val bodyPictures = database.bodyPictureDao().getCurrentUserBodyPictures()
}