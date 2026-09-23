package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long = 1L,
    val title: String,
    val message: String,
    val type: String = "ORDER",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
