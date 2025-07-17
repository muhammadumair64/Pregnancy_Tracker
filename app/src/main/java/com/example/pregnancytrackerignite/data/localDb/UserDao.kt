package com.example.pregnancytrackerignite.data.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pregnancytrackerignite.data.models.CurrentUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: CurrentUser)

    @Update
    suspend fun updateUser(user: CurrentUser)

    @Query("Select * from currentuser order by id desc limit 1")
    fun getCurrentUser(): kotlinx.coroutines.flow.Flow<CurrentUser?>

    @Delete
    suspend fun deleteCurrentUser(user: CurrentUser)
}