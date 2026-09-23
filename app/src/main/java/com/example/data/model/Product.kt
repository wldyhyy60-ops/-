package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryId: Long,
    val subCategory: String = "",
    val description: String,
    val specifications: String = "",
    val priceYer: Double,
    val priceSar: Double,
    val originalPriceYer: Double = 0.0,
    val discountPercent: Int = 0,
    val stockQuantity: Int = 10,
    val sku: String = "",
    val keywords: String = "",
    val isBestSeller: Boolean = false,
    val isNewArrival: Boolean = false,
    val isFeatured: Boolean = false,
    val hasSpecialOffer: Boolean = false,
    val isHidden: Boolean = false,
    val mainImageUrl: String = "",
    val additionalImages: String = "" // comma separated
)

enum class CurrencyType(val symbolAr: String, val code: String) {
    YER("ر.ي", "YER"),
    SAR("ر.س", "SAR")
}

enum class ProductSortOption(val titleAr: String) {
    NEWEST("الأحدث أولاً"),
    PRICE_LOW_HIGH("السعر: من الأقل للأعلى"),
    PRICE_HIGH_LOW("السعر: من الأعلى للأقل"),
    BEST_SELLER("الأكثر مبيعاً")
}
