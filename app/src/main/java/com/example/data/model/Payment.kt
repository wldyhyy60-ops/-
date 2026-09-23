package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PaymentType(val titleAr: String) {
    COD("الدفع عند الاستلام"),
    BANK_TRANSFER("تحويل بنكي"),
    E_WALLET("محفظة إلكترونية"),
    REMITTANCE("حوالة صرافة")
}

enum class PaymentStatus(val titleAr: String, val badgeColorHex: Long) {
    COD_PENDING("عند الاستلام - بانتظار التحصيل", 0xFF3B82F6),
    VERIFICATION_PENDING("بانتظار التحقق من الإثبات", 0xFFF59E0B),
    VERIFIED("تم التحقق من الدفع ✅", 0xFF10B981),
    REJECTED("تم رفض إثبات الدفع ❌", 0xFFEF4444)
}

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val accountNumber: String = "",
    val accountHolder: String = "",
    val currency: String = "YER", // YER, SAR, ANY
    val type: PaymentType = PaymentType.BANK_TRANSFER,
    val isActive: Boolean = true,
    val allowedGovernorates: String = "", // Comma-separated list for COD (e.g. "صنعاء,إب"). Empty means all.
    val instructions: String = "",
    val sortOrder: Int = 0
)
