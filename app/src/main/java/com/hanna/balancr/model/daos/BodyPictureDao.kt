package com.hanna.balancr.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hanna.balancr.model.entities.BodyPicture
import com.hanna.balancr.model.entities.User
import java.sql.Date

@Dao
interface BodyPictureDao {
    @Query("SELECT * FROM body_pictures WHERE userId = (SELECT id FROM users WHERE isLoggedIn = 1)")
    fun getCurrentUserBodyPictures(): LiveData<List<BodyPicture>>

    @Insert
    suspend fun insert(bodyPicture: BodyPicture)
}