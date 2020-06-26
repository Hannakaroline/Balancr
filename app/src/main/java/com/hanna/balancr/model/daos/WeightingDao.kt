package com.hanna.balancr.model.daos

import androidx.room.Insert
import androidx.room.Query
import com.hanna.balancr.model.entities.Weighting
import java.sql.Date
import java.util.*

interface WeightingDao {
    @Query("SELECT * FROM weightings WHERE date BETWEEN :start AND :end")
    fun getWeightingsForPeriod(start: Date, end: Date): List<Weighting>

    @Insert
    fun insert(weighting: Weighting)

}