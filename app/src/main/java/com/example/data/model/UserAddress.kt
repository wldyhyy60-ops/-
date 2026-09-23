package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AddressType(val titleAr: String, val iconEmoji: String) {
    HOME("المنزل", "🏠"),
    WORK("العمل", "💼"),
    OTHER("عنوان آخر", "📍")
}

@Entity(tableName = "user_addresses")
data class UserAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long = 1L,
    val title: String = "المنزل",
    val type: AddressType = AddressType.HOME,
    val recipientName: String,
    val recipientPhone: String,
    val governorate: String,
    val city: String,
    val district: String = "",
    val street: String = "",
    val nearestLandmark: String = "",
    val addressDetails: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val googleMapsUrl: String = "",
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getEffectiveMapsUrl(): String {
        return when {
            googleMapsUrl.isNotBlank() -> googleMapsUrl
            latitude != null && longitude != null -> "https://www.google.com/maps?q=$latitude,$longitude"
            else -> ""
        }
    }

    fun formattedAddressString(): String {
        val parts = mutableListOf<String>()
        if (governorate.isNotBlank()) parts.add(governorate)
        if (city.isNotBlank()) parts.add(city)
        if (district.isNotBlank()) parts.add("حي $district")
        if (street.isNotBlank()) parts.add("شارع $street")
        if (nearestLandmark.isNotBlank()) parts.add("أقرب معلم: $nearestLandmark")
        if (addressDetails.isNotBlank()) parts.add(addressDetails)
        return parts.joinToString(" - ")
    }
}
