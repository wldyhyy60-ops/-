package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CartItemWithProduct
import com.example.data.model.CategoryEntity
import com.example.data.model.CouponEntity
import com.example.data.model.DeliveryZoneEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.OrderItemEntity
import com.example.data.model.OrderStatus
import com.example.data.model.OrderWithItems
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.PaymentType
import com.example.data.model.ProductEntity
import com.example.data.model.UserAddressEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class AlMalakiRepository(private val db: AppDatabase) {

    // Categories
    val activeCategories: Flow<List<CategoryEntity>> = db.categoryDao().getActiveCategories()
    val allCategories: Flow<List<CategoryEntity>> = db.categoryDao().getAllCategories()

    suspend fun addCategory(category: CategoryEntity, adminName: String): Long {
        val id = db.categoryDao().insertCategory(category)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إضافة قسم",
                details = "تمت إضافة قسم جديد: ${category.nameAr}"
            )
        )
        return id
    }

    suspend fun updateCategory(category: CategoryEntity, adminName: String) {
        db.categoryDao().updateCategory(category)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل قسم",
                details = "تم تعديل بيانات القسم: ${category.nameAr}"
            )
        )
    }

    suspend fun deleteCategory(categoryId: Long, categoryName: String, adminName: String) {
        db.categoryDao().deleteCategoryById(categoryId)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "حذف قسم",
                details = "تم حذف القسم: $categoryName"
            )
        )
    }

    // Products
    val activeProducts: Flow<List<ProductEntity>> = db.productDao().getActiveProducts()
    val allProducts: Flow<List<ProductEntity>> = db.productDao().getAllProducts()
    val bestSellers: Flow<List<ProductEntity>> = db.productDao().getBestSellers()
    val newArrivals: Flow<List<ProductEntity>> = db.productDao().getNewArrivals()
    val specialOffers: Flow<List<ProductEntity>> = db.productDao().getSpecialOffers()
    val featuredProducts: Flow<List<ProductEntity>> = db.productDao().getFeatured()
    val lowStockProducts: Flow<List<ProductEntity>> = db.productDao().getLowStockProducts()
    val outOfStockProducts: Flow<List<ProductEntity>> = db.productDao().getOutOfStockProducts()

    fun getProductByIdFlow(id: Long): Flow<ProductEntity?> = db.productDao().getProductByIdFlow(id)
    suspend fun getProductById(id: Long): ProductEntity? = db.productDao().getProductById(id)
    fun getProductsByCategory(categoryId: Long): Flow<List<ProductEntity>> = db.productDao().getProductsByCategory(categoryId)
    fun searchProducts(query: String): Flow<List<ProductEntity>> = db.productDao().searchProducts(query)

    suspend fun addProduct(product: ProductEntity, adminName: String): Long {
        val id = db.productDao().insertProduct(product)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إضافة منتج",
                details = "تمت إضافة منتج: ${product.name} بسعر ${product.priceYer} ر.ي ومخزون ${product.stockQuantity}"
            )
        )
        return id
    }

    suspend fun updateProduct(product: ProductEntity, adminName: String) {
        db.productDao().updateProduct(product)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل منتج",
                details = "تم تحديث بيانات المنتج: ${product.name} (SKU: ${product.sku})"
            )
        )
    }

    suspend fun updateStock(productId: Long, productName: String, newStock: Int, adminName: String) {
        db.productDao().updateStock(productId, newStock)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل المخزون",
                details = "تم تعديل مخزون $productName إلى $newStock قطعة"
            )
        )
    }

    suspend fun toggleProductVisibility(productId: Long, isHidden: Boolean, productName: String, adminName: String) {
        db.productDao().updateHidden(productId, isHidden)
        val statusText = if (isHidden) "إخفاء" else "إظهار"
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "$statusText منتج",
                details = "تم $statusText المنتج: $productName"
            )
        )
    }

    suspend fun deleteProduct(productId: Long, productName: String, adminName: String) {
        db.productDao().deleteProductById(productId)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "حذف منتج",
                details = "تم حذف المنتج: $productName نهائياً"
            )
        )
    }

    // Cart
    fun getCartWithProducts(userId: Long): Flow<List<CartItemWithProduct>> {
        return combine(
            db.cartDao().getCartItemsByUser(userId),
            db.productDao().getAllProducts()
        ) { cartItems, allProducts ->
            val productMap = allProducts.associateBy { it.id }
            cartItems.mapNotNull { cartItem ->
                productMap[cartItem.productId]?.let { product ->
                    CartItemWithProduct(cartItem, product)
                }
            }
        }
    }

    suspend fun addToCart(userId: Long, productId: Long, quantity: Int = 1): Boolean {
        val product = db.productDao().getProductById(productId) ?: return false
        if (product.stockQuantity <= 0) return false

        val existing = db.cartDao().getCartItem(userId, productId)
        if (existing != null) {
            val newQty = (existing.quantity + quantity).coerceAtMost(product.stockQuantity)
            db.cartDao().updateQuantity(existing.id, newQty)
        } else {
            val allowedQty = quantity.coerceAtMost(product.stockQuantity)
            db.cartDao().insertCartItem(
                CartItemEntity(userId = userId, productId = productId, quantity = allowedQty)
            )
        }
        return true
    }

    suspend fun updateCartQuantity(cartItemId: Long, quantity: Int, stockLimit: Int) {
        if (quantity <= 0) {
            db.cartDao().deleteCartItemById(cartItemId)
        } else {
            val bounded = quantity.coerceAtMost(stockLimit)
            db.cartDao().updateQuantity(cartItemId, bounded)
        }
    }

    suspend fun removeFromCart(cartItemId: Long) {
        db.cartDao().deleteCartItemById(cartItemId)
    }

    suspend fun clearCart(userId: Long) {
        db.cartDao().clearCart(userId)
    }

    // Favorites
    fun getFavoriteProductIds(userId: Long): Flow<List<Long>> = db.favoriteDao().getFavoriteProductIds(userId)
    fun isFavorite(userId: Long, productId: Long): Flow<Boolean> = db.favoriteDao().isFavorite(userId, productId)

    suspend fun toggleFavorite(userId: Long, productId: Long) {
        val isFav = db.favoriteDao().isFavorite(userId, productId).first()
        if (isFav) {
            db.favoriteDao().removeFavorite(userId, productId)
        } else {
            db.favoriteDao().insertFavorite(FavoriteEntity(userId = userId, productId = productId))
        }
    }

    // Orders
    val allOrders: Flow<List<OrderEntity>> = db.orderDao().getAllOrders()
    fun getOrdersByCustomer(customerId: Long): Flow<List<OrderEntity>> = db.orderDao().getOrdersByCustomer(customerId)
    fun getOrderByIdFlow(orderId: Long): Flow<OrderEntity?> = db.orderDao().getOrderByIdFlow(orderId)
    fun getOrderItems(orderId: Long): Flow<List<OrderItemEntity>> = db.orderDao().getOrderItems(orderId)

    suspend fun placeOrder(
        customerId: Long,
        customerName: String,
        customerPhone: String,
        governorate: String,
        city: String,
        district: String = "",
        street: String = "",
        nearestLandmark: String = "",
        addressDetails: String,
        latitude: Double? = null,
        longitude: Double? = null,
        googleMapsUrl: String = "",
        notes: String,
        subtotalYer: Double,
        deliveryFeeYer: Double,
        discountAmountYer: Double,
        totalYer: Double,
        currencyUsed: String,
        itemsToOrder: List<CartItemWithProduct>,
        paymentMethodId: Long = 1L,
        paymentMethodName: String = "الدفع عند الاستلام",
        paymentStatus: PaymentStatus = PaymentStatus.COD_PENDING,
        paymentProofUri: String? = null,
        paymentTransactionNumber: String = "",
        paymentNotes: String = "",
        paymentDate: Long? = null
    ): OrderWithItems {
        // Calculate new order number
        val existingOrders = db.orderDao().getAllOrders().first()
        val nextNum = 10000 + existingOrders.size + 1
        val orderNumber = "#RM-$nextNum"

        val effectiveMapsUrl = when {
            googleMapsUrl.isNotBlank() -> googleMapsUrl
            latitude != null && longitude != null -> "https://www.google.com/maps?q=$latitude,$longitude"
            else -> ""
        }

        val orderEntity = OrderEntity(
            orderNumber = orderNumber,
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            governorate = governorate,
            city = city,
            district = district,
            street = street,
            nearestLandmark = nearestLandmark,
            addressDetails = addressDetails,
            latitude = latitude,
            longitude = longitude,
            googleMapsUrl = effectiveMapsUrl,
            notes = notes,
            subtotalYer = subtotalYer,
            deliveryFeeYer = deliveryFeeYer,
            discountAmountYer = discountAmountYer,
            totalYer = totalYer,
            currencyUsed = currencyUsed,
            status = OrderStatus.NEW,
            paymentMethodId = paymentMethodId,
            paymentMethodName = paymentMethodName,
            paymentStatus = paymentStatus,
            paymentProofUri = paymentProofUri,
            paymentTransactionNumber = paymentTransactionNumber,
            paymentNotes = paymentNotes,
            paymentDate = paymentDate ?: System.currentTimeMillis(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        val orderId = db.orderDao().insertOrder(orderEntity)

        val orderItems = itemsToOrder.map { item ->
            // Reduce stock
            val currentProduct = db.productDao().getProductById(item.product.id)
            if (currentProduct != null) {
                val updatedStock = (currentProduct.stockQuantity - item.cartItem.quantity).coerceAtLeast(0)
                db.productDao().updateStock(currentProduct.id, updatedStock)
            }

            OrderItemEntity(
                orderId = orderId,
                productId = item.product.id,
                productName = item.product.name,
                productSku = item.product.sku,
                priceAtPurchase = item.product.priceYer,
                quantity = item.cartItem.quantity,
                totalPrice = item.totalYer
            )
        }
        db.orderDao().insertOrderItems(orderItems)

        // Clear user cart
        db.cartDao().clearCart(customerId)

        // Notify user
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = customerId,
                title = "تم استلام طلبك بنجاح $orderNumber 🛍️",
                message = "طلبك قيد المراجعة الآن بإجمالي ${totalYer.toInt()} ر.ي وسيتم التواصل معك عبر واتساب لتأكيده.",
                type = "ORDER"
            )
        )

        // Activity log
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userId = customerId,
                userName = customerName,
                actionType = "إنشاء طلب",
                details = "طلب جديد $orderNumber للعميل $customerName بقيمة ${totalYer.toInt()} ر.ي"
            )
        )

        return OrderWithItems(
            order = orderEntity.copy(id = orderId),
            items = orderItems
        )
    }

    suspend fun updateOrderStatus(orderId: Long, orderNumber: String, customerId: Long, newStatus: OrderStatus, adminName: String) {
        db.orderDao().updateOrderStatus(orderId, newStatus)

        // Send customer notification
        val title = when (newStatus) {
            OrderStatus.UNDER_REVIEW -> "طلبك $orderNumber قيد المراجعة"
            OrderStatus.CONFIRMED -> "تم تأكيد طلبك $orderNumber بنجاح ✅"
            OrderStatus.PREPARING -> "جاري تجهيز طلبك $orderNumber 📦"
            OrderStatus.SHIPPING -> "طلبك $orderNumber جاري شحنه 🚚"
            OrderStatus.OUT_FOR_DELIVERY -> "طلبك $orderNumber خرج للتوصيل الآن 🛵"
            OrderStatus.DELIVERED -> "تم تسليم طلبك $orderNumber بنجاح 👑"
            OrderStatus.CANCELLED -> "تم إلغاء طلبك $orderNumber ❌"
            OrderStatus.NEW -> "طلب جديد $orderNumber"
        }
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = customerId,
                title = title,
                message = "حالة طلبك $orderNumber أصبحت: ${newStatus.titleAr}",
                type = "ORDER"
            )
        )

        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تغيير حالة طلب",
                details = "تم تغيير حالة الطلب $orderNumber إلى ${newStatus.titleAr}"
            )
        )
    }

    suspend fun updateAdminNotes(orderId: Long, notes: String) {
        db.orderDao().updateAdminNotes(orderId, notes)
    }

    // Payment Verification by Admin
    suspend fun verifyPayment(orderId: Long, orderNumber: String, customerId: Long, adminName: String, notes: String = "") {
        db.orderDao().updatePaymentStatus(orderId, PaymentStatus.VERIFIED, "", System.currentTimeMillis())
        if (notes.isNotBlank()) {
            db.orderDao().updateAdminNotes(orderId, notes)
        }
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = customerId,
                title = "تم تأكيد الدفع لطلبك $orderNumber بنجاح ✅",
                message = "تمت مراجعة والتحقق من إثبات الدفع لطلبك بنجاح. جاري استكمال تجهيز وشحن الطلب.",
                type = "PAYMENT"
            )
        )
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تأكيد الدفع",
                details = "تم تأكيد التحقق من دفع الطلب $orderNumber بنجاح."
            )
        )
    }

    suspend fun rejectPayment(orderId: Long, orderNumber: String, customerId: Long, adminName: String, rejectionReason: String) {
        db.orderDao().updatePaymentStatus(orderId, PaymentStatus.REJECTED, rejectionReason, System.currentTimeMillis())
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = customerId,
                title = "تنبيه: تم رفض إثبات الدفع لطلبك $orderNumber ❌",
                message = "سبب الرفض: $rejectionReason. يرجى مراجعة بيانات التحويل أو التواصل معنا عبر واتساب.",
                type = "PAYMENT"
            )
        )
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "رفض إثبات الدفع",
                details = "تم رفض إثبات دفع الطلب $orderNumber. السبب: $rejectionReason"
            )
        )
    }

    // Payment Methods Management
    val allPaymentMethods: Flow<List<PaymentMethodEntity>> = db.paymentMethodDao().getAllPaymentMethods()
    val activePaymentMethods: Flow<List<PaymentMethodEntity>> = db.paymentMethodDao().getActivePaymentMethods()

    suspend fun addPaymentMethod(method: PaymentMethodEntity, adminName: String): Long {
        val id = db.paymentMethodDao().insertPaymentMethod(method)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إضافة طريقة دفع",
                details = "تمت إضافة طريقة دفع جديدة: ${method.name}"
            )
        )
        return id
    }

    suspend fun updatePaymentMethod(method: PaymentMethodEntity, adminName: String) {
        db.paymentMethodDao().updatePaymentMethod(method)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل طريقة دفع",
                details = "تم تحديث بيانات طريقة الدفع: ${method.name}"
            )
        )
    }

    suspend fun deletePaymentMethod(method: PaymentMethodEntity, adminName: String) {
        db.paymentMethodDao().deletePaymentMethod(method)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "حذف طريقة دفع",
                details = "تم حذف طريقة الدفع: ${method.name}"
            )
        )
    }

    suspend fun togglePaymentMethodActive(id: Long, isActive: Boolean, adminName: String) {
        db.paymentMethodDao().toggleActive(id, isActive)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = if (isActive) "تفعيل طريقة دفع" else "تعطيل طريقة دفع",
                details = "تم تغيير حالة تفعيل طريقة الدفع (ID: $id) إلى $isActive"
            )
        )
    }

    suspend fun updateCodAllowedGovernorates(id: Long, governorates: String, adminName: String) {
        db.paymentMethodDao().updateAllowedGovernorates(id, governorates)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل محافظات الدفع عند الاستلام",
                details = "المحافظات المسموح بها للدفع عند الاستلام: $governorates"
            )
        )
    }

    // Delivery Zones
    val availableDeliveryZones: Flow<List<DeliveryZoneEntity>> = db.deliveryZoneDao().getAvailableZones()
    val allDeliveryZones: Flow<List<DeliveryZoneEntity>> = db.deliveryZoneDao().getAllZones()

    suspend fun addDeliveryZone(zone: DeliveryZoneEntity, adminName: String) {
        db.deliveryZoneDao().insertZone(zone)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إضافة منطقة توصيل",
                details = "تمت إضافة منطقة: ${zone.governorateName} برسوم ${zone.deliveryFeeYer} ر.ي"
            )
        )
    }

    suspend fun updateDeliveryZone(zone: DeliveryZoneEntity, adminName: String) {
        db.deliveryZoneDao().updateZone(zone)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "تعديل منطقة توصيل",
                details = "تم تحديث رسوم توصيل: ${zone.governorateName}"
            )
        )
    }

    // Coupons
    val allCoupons: Flow<List<CouponEntity>> = db.couponDao().getAllCoupons()
    suspend fun checkCoupon(code: String): CouponEntity? = db.couponDao().getCouponByCode(code.trim().uppercase())
    suspend fun incrementCouponUsage(couponId: Long) = db.couponDao().incrementUsage(couponId)
    suspend fun addCoupon(coupon: CouponEntity, adminName: String) {
        db.couponDao().insertCoupon(coupon)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إضافة كوبون",
                details = "تمت إضافة الكوبون: ${coupon.code}"
            )
        )
    }
    suspend fun deleteCoupon(coupon: CouponEntity, adminName: String) {
        db.couponDao().deleteCoupon(coupon)
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "حذف كوبون",
                details = "تم حذف الكوبون: ${coupon.code}"
            )
        )
    }

    // Users
    val allUsers: Flow<List<UserEntity>> = db.userDao().getAllUsers()
    val customers: Flow<List<UserEntity>> = db.userDao().getCustomers()
    suspend fun getUserById(id: Long): UserEntity? = db.userDao().getUserById(id)
    suspend fun getUserByPhone(phone: String): UserEntity? = db.userDao().getUserByPhone(phone)
    suspend fun saveUser(user: UserEntity): Long = db.userDao().insertUser(user)
    suspend fun updateUser(user: UserEntity) = db.userDao().updateUser(user)

    // User Addresses
    fun getUserAddresses(userId: Long): Flow<List<UserAddressEntity>> = db.userAddressDao().getAddressesForUser(userId)
    suspend fun getDefaultAddress(userId: Long): UserAddressEntity? = db.userAddressDao().getDefaultAddress(userId)
    suspend fun saveAddress(address: UserAddressEntity): Long {
        if (address.isDefault) {
            db.userAddressDao().clearDefaultAddresses(address.userId)
        }
        return db.userAddressDao().insertAddress(address)
    }
    suspend fun updateAddress(address: UserAddressEntity) {
        if (address.isDefault) {
            db.userAddressDao().clearDefaultAddresses(address.userId)
        }
        db.userAddressDao().updateAddress(address)
    }
    suspend fun setDefaultAddress(userId: Long, addressId: Long) {
        db.userAddressDao().clearDefaultAddresses(userId)
        db.userAddressDao().setDefaultAddress(userId, addressId)
    }
    suspend fun deleteAddress(address: UserAddressEntity) = db.userAddressDao().deleteAddress(address)
    suspend fun deleteAddressById(id: Long) = db.userAddressDao().deleteAddressById(id)

    // Notifications
    fun getNotifications(userId: Long): Flow<List<NotificationEntity>> = db.notificationDao().getNotificationsForUser(userId)
    fun getUnreadNotificationsCount(userId: Long): Flow<Int> = db.notificationDao().getUnreadCount(userId)
    suspend fun markNotificationAsRead(id: Long) = db.notificationDao().markAsRead(id)
    suspend fun sendBroadcastNotification(title: String, message: String, adminName: String) {
        db.notificationDao().insertNotification(
            NotificationEntity(
                userId = 0, // 0 means broadcast to all
                title = title,
                message = message,
                type = "PROMO"
            )
        )
        db.activityLogDao().insertLog(
            ActivityLogEntity(
                userName = adminName,
                actionType = "إرسال إشعار عام",
                details = "تم إرسال إشعار عام: $title"
            )
        )
    }

    // Activity Logs
    val recentActivityLogs: Flow<List<ActivityLogEntity>> = db.activityLogDao().getRecentLogs()

    // Backup & Restore
    suspend fun resetDatabaseToDefaults() {
        // Reset by re-clearing and initializing
        db.clearAllTables()
        DatabaseInitializer.populateInitialDataIfEmpty(db)
    }
}
