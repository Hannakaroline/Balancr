package com.hanna.balancr.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "weightings")
data class Weighting(
   @PrimaryKey(autoGenerate = true)
   val id: Long = 0,
   val weight: Float,
   val date: Date
)