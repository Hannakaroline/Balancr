package com.hanna.balancr.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hanna.balancr.model.entities.Weighting
import java.sql.Date
import java.util.*

@Dao
interface WeightingDao {
    @Query("SELECT * FROM weightings WHERE userId = (SELECT id FROM users WHERE isLoggedIn = 1) ORDER BY date DESC, id DESC")
    fun getCurrentUserWeightings(): LiveData<List<Weighting>>

    @Insert
    suspend fun insert(weighting: Weighting)
}