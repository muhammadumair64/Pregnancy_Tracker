package com.example.pregnancytrackerignite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentUser(
    val name: String?,
    val age: String?,
    val userIsMother: Boolean=true,
    val isThisFirstChild: Boolean=true,
   @PrimaryKey(autoGenerate = false) val id:Int=0
)

