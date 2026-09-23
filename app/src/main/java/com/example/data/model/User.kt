package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val titleAr: String) {
    SUPER_ADMIN("المدير العام (Super Admin)"),
    ADMIN("مدير (Admin)"),
    ORDERS_STAFF("موظف الطلبات"),
    INVENTORY_STAFF("موظف المخزون"),
    CUSTOMER("عميل")
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String = "",
    val name: String,
    val phone: String,
    val email: String = "",
    val password: String = "",
    val governorate: String = "صنعاء",
    val city: String = "صنعاء",
    val district: String = "",
    val street: String = "",
    val nearestLandmark: String = "",
    val addressDetails: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val googleMapsUrl: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
