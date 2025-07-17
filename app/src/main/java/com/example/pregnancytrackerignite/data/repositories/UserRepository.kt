package com.example.pregnancytrackerignite.data.repositories

import com.example.pregnancytrackerignite.data.localDb.UserDao
import com.example.pregnancytrackerignite.data.models.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val dao: UserDao) {

suspend fun insertUser(user: CurrentUser) {
    withContext(Dispatchers.IO){
        dao.insertUser(user)
    }
}
    suspend fun updateUser(user: CurrentUser){
        withContext(Dispatchers.IO){
            dao.updateUser(user)
        }
    }
    suspend fun deleteUser(user: CurrentUser){
        withContext(Dispatchers.IO){
            dao.deleteCurrentUser(user)
        }
    }
    fun getCurrentUser(): Flow<CurrentUser?> {
        return dao.getCurrentUser()
    }
}