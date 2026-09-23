package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameAr: String,
    val nameEn: String = "",
    val iconKey: String = "bolt", // bolt, cosmetics, phone, gift, tech, home
    val description: String = "",
    val sortOrder: Int = 0,
    val isHidden: Boolean = false
)
