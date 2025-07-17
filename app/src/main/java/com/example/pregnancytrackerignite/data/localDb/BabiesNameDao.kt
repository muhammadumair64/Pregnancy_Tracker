package com.example.pregnancytrackerignite.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pregnancytrackerignite.data.models.BabiesNameModel

@Dao
interface BabiesNameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(names: List<BabiesNameModel>)

    @Query("SELECT * FROM babies_names WHERE type = :type AND country = :country")
    fun fetchNamesByTypeAndCountry(type: String, country: String): LiveData<List<BabiesNameModel>>

    @Query("UPDATE babies_names SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM babies_names WHERE isFavorite = 1")
    fun getFavoriteNames(): LiveData<List<BabiesNameModel>>

    @Query("DELETE FROM babies_names")
    suspend fun clearAll()
}