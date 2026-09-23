package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery_zones")
data class DeliveryZoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val governorateName: String,
    val deliveryFeeYer: Double,
    val freeDeliveryThresholdYer: Double = 50000.0,
    val estimatedDays: String = "1-2 أيام",
    val isAvailable: Boolean = true
)
