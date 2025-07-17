package com.example.pregnancytrackerignite.data.repositories

import androidx.lifecycle.LiveData
import com.example.pregnancytrackerignite.data.localDb.BabiesNameDao
import com.example.pregnancytrackerignite.data.models.BabiesNameModel
import javax.inject.Inject

class BabiesNameRepository @Inject constructor(private val dao: BabiesNameDao) {

    suspend fun insertNames(names: List<BabiesNameModel>) {
        dao.insertAll(names)
    }

    fun fetchNames(type: String, country: String): LiveData<List<BabiesNameModel>> {
        return dao.fetchNamesByTypeAndCountry(type, country)
    }

    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        dao.updateFavoriteStatus(id, isFavorite)
    }

    fun getFavoriteNames(): LiveData<List<BabiesNameModel>> {
        return dao.getFavoriteNames()
    }

    suspend fun clearNames() {
        dao.clearAll()
    }
}
