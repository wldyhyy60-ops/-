package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderStatus(val titleAr: String, val stepIndex: Int) {
    NEW("طلب جديد", 0),
    UNDER_REVIEW("قيد المراجعة", 1),
    CONFIRMED("تم تأكيد الطلب", 2),
    PREPARING("جاري التجهيز", 3),
    SHIPPING("جاري الشحن", 4),
    OUT_FOR_DELIVERY("خرج للتوصيل", 5),
    DELIVERED("تم التسليم", 6),
    CANCELLED("تم إلغاء الطلب", -1)
}

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String, // e.g. #RM-10001
    val customerId: Long = 1L,
    val customerName: String,
    val customerPhone: String,
    val governorate: String,
    val city: String,
    val district: String = "",
    val street: String = "",
    val nearestLandmark: String = "",
    val addressDetails: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val googleMapsUrl: String = "",
    val notes: String = "",
    val subtotalYer: Double,
    val deliveryFeeYer: Double,
    val discountAmountYer: Double = 0.0,
    val totalYer: Double,
    val currencyUsed: String = "YER",
    val status: OrderStatus = OrderStatus.NEW,
    val paymentMethodId: Long = 0L,
    val paymentMethodName: String = "الدفع عند الاستلام",
    val paymentStatus: PaymentStatus = PaymentStatus.COD_PENDING,
    val paymentProofUri: String? = null,
    val paymentTransactionNumber: String = "",
    val paymentNotes: String = "",
    val paymentDate: Long? = null,
    val paymentRejectionReason: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val adminNotes: String = ""
) {
    fun getEffectiveMapsUrl(): String {
        return when {
            googleMapsUrl.isNotBlank() -> googleMapsUrl
            latitude != null && longitude != null -> "https://www.google.com/maps?q=$latitude,$longitude"
            else -> ""
        }
    }

    fun formattedDetailedAddress(): String {
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

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val productSku: String = "",
    val priceAtPurchase: Double,
    val quantity: Int,
    val totalPrice: Double
)

data class OrderWithItems(
    val order: OrderEntity,
    val items: List<OrderItemEntity>
)
