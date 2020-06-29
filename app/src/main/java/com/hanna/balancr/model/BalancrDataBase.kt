package com.hanna.balancr.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hanna.balancr.model.daos.BodyPictureDao
import com.hanna.balancr.model.daos.UserDao
import com.hanna.balancr.model.daos.WeightingDao
import com.hanna.balancr.model.entities.BodyPicture
import com.hanna.balancr.model.entities.User
import com.hanna.balancr.model.entities.Weighting

@Database(
    entities = arrayOf(User::class, Weighting::class, BodyPicture::class),
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BalancrDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun weightingDao(): WeightingDao
    abstract fun bodyPictureDao(): BodyPictureDao

    companion object {
        @Volatile
        private var instance: BalancrDataBase? = null

        fun getInstance(context: Context): BalancrDataBase {
            val tempInstance = instance
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BalancrDataBase::class.java,
                    "balancr_database"
                ).build().apply {
                    instance = this
                    return this
                }
            }
        }
    }
}