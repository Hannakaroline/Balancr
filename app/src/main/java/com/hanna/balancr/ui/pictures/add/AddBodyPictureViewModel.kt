package com.hanna.balancr.ui.pictures.add

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hanna.balancr.model.BalancrDataBase
import com.hanna.balancr.model.entities.BodyPicture
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class AddBodyPictureViewModel(application: Application) : AndroidViewModel(application) {
    enum class AddBodyPictureState {
        InProgress, WithError, Finished
    }

    private val database = BalancrDataBase.getInstance(application.applicationContext)
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

    val addBodyPictureState = MutableLiveData(AddBodyPictureState.InProgress)
    val pickedImage = MutableLiveData<String?>(null)

    fun clearState() {
        pickedImage.value = null
        addBodyPictureState.value = AddBodyPictureState.InProgress
    }

    fun saveBodyPicture(date: String?) {
        val currentlyPickedImage = pickedImage.value
        if (date == null || currentlyPickedImage == null) {
            addBodyPictureState.value = AddBodyPictureState.WithError
            return
        }
        val parsedDate = dateFormatter.parse(date)
        if (parsedDate == null) {
            addBodyPictureState.value = AddBodyPictureState.WithError
            return
        }


        viewModelScope.launch {
            val currentUser = database.userDao().currentUser() ?: return@launch
            val bodyPicture = BodyPicture(
                date = parsedDate,
                picturePath = currentlyPickedImage,
                userId = currentUser.id
            )
            database.bodyPictureDao().insert(bodyPicture)
            addBodyPictureState.value = AddBodyPictureState.Finished
        }
    }

}