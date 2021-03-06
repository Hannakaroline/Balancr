package com.hanna.balancr.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "body_pictures")
data class BodyPicture(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val picturePath: String,
    val userId: Long
)