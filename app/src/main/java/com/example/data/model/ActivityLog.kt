package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long = 1L,
    val userName: String = "مدير النظام",
    val actionType: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
