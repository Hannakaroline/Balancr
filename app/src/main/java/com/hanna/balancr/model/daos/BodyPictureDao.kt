package com.hanna.balancr.model.daos

import androidx.room.Insert
import androidx.room.Query
import com.hanna.balancr.model.entities.BodyPicture
import java.sql.Date

interface BodyPictureDao {
    @Query("SELECT * FROM body_pictures WHERE date BETWEEN :start AND :end")
    fun getBodyPicturesForPeriod(start: Date, end: Date): List<BodyPicture>

    @Insert
    fun insert(bodyPicture: BodyPicture)
}