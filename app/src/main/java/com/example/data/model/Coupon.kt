package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DiscountType(val titleAr: String) {
    PERCENT("نسبة مئوية"),
    FIXED("مبلغ ثابت (ر.ي)")
}

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,
    val discountType: DiscountType = DiscountType.PERCENT,
    val discountValue: Double = 20.0,
    val minOrderAmountYer: Double = 10000.0,
    val expiryDate: String = "2026-12-31",
    val isActive: Boolean = true,
    val timesUsed: Int = 0
)
