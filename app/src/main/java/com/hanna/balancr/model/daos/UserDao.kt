package com.hanna.balancr.model.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hanna.balancr.model.entities.BodyPicture
import com.hanna.balancr.model.entities.User
import java.sql.Date

@Dao
interface UserDao {
    @Query("SELECT * from users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Transaction
    @Update
    suspend fun updateUsers(users: List<User>)

    @Query("SELECT * from users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * from users WHERE isLoggedIn = 1")
    fun liveCurrentUser(): LiveData<User?>

    @Query("SELECT * from users WHERE isLoggedIn = 1")
    suspend fun currentUser(): User?
}