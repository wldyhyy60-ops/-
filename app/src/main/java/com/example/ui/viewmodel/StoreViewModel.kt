package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CartItemWithProduct
import com.example.data.model.CategoryEntity
import com.example.data.model.CouponEntity
import com.example.data.model.CurrencyType
import com.example.data.model.DeliveryZoneEntity
import com.example.data.model.DiscountType
import com.example.data.model.NotificationEntity
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.OrderWithItems
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.PaymentType
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSortOption
import com.example.data.model.UserAddressEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.AlMalakiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainNavTab {
    HOME,
    CATEGORIES,
    CART,
    FAVORITES,
    ACCOUNT
}

enum class AdminTab(val titleAr: String) {
    DASHBOARD("لوحة التحكم"),
    PAYMENTS("إدارة المدفوعات 💳"),
    PRODUCTS("إدارة المنتجات"),
    ORDERS("إدارة الطلبات"),
    INVENTORY("إدارة المخزون"),
    CATEGORIES("إدارة الأقسام"),
    CUSTOMERS("إدارة العملاء"),
    COUPONS("العروض والخصومات"),
    LOGS("سجل العمليات"),
    BACKUP("النسخ والاستعادة")
}

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = AlMalakiRepository(db)

    // Navigation & View State
    private val _currentNavTab = MutableStateFlow(MainNavTab.HOME)
    val currentNavTab: StateFlow<MainNavTab> = _currentNavTab.asStateFlow()

    private val _isInAdminMode = MutableStateFlow(false)
    val isInAdminMode: StateFlow<Boolean> = _isInAdminMode.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    // Active Product Detail selection
    private val _selectedProductId = MutableStateFlow<Long?>(null)
    val selectedProductId: StateFlow<Long?> = _selectedProductId.asStateFlow()

    // Currency selection
    private val _currency = MutableStateFlow(CurrencyType.YER)
    val currency: StateFlow<CurrencyType> = _currency.asStateFlow()

    // Search and Filter State
    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<Long?>(null)
    val sortOption = MutableStateFlow(ProductSortOption.NEWEST)
    val filterInStockOnly = MutableStateFlow(false)
    val filterDiscountOnly = MutableStateFlow(false)

    // Current User Session
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Cart state
    val cartItems: StateFlow<List<CartItemWithProduct>> = _currentUser.combine(repository.activeProducts) { user, _ ->
        user?.id ?: 1L
    }.combine(repository.getCartWithProducts(1L)) { _, items ->
        items
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart dynamic flow by current user id
    private val _userCartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val userCartItems: StateFlow<List<CartItemWithProduct>> = _userCartItems.asStateFlow()

    // Delivery Zone & Coupon for checkout
    val selectedDeliveryZone = MutableStateFlow<DeliveryZoneEntity?>(null)
    val appliedCoupon = MutableStateFlow<CouponEntity?>(null)
    val couponMessage = MutableStateFlow<String?>(null)

    // User Addresses
    val userAddresses = MutableStateFlow<List<UserAddressEntity>>(emptyList())
    val selectedAddress = MutableStateFlow<UserAddressEntity?>(null)

    // Order Success Info
    val lastPlacedOrder = MutableStateFlow<OrderWithItems?>(null)

    // Database flows
    val categories: StateFlow<List<CategoryEntity>> = repository.activeCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeProducts: StateFlow<List<ProductEntity>> = repository.activeProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestSellers: StateFlow<List<ProductEntity>> = repository.bestSellers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val newArrivals: StateFlow<List<ProductEntity>> = repository.newArrivals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val specialOffers: StateFlow<List<ProductEntity>> = repository.specialOffers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<ProductEntity>> = repository.featuredProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.lowStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val outOfStockProducts: StateFlow<List<ProductEntity>> = repository.outOfStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<List<Long>> = repository.getFavoriteProductIds(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customerOrders: StateFlow<List<OrderEntity>> = repository.getOrdersByCustomer(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deliveryZones: StateFlow<List<DeliveryZoneEntity>> = repository.availableDeliveryZones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDeliveryZones: StateFlow<List<DeliveryZoneEntity>> = repository.allDeliveryZones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCoupons: StateFlow<List<CouponEntity>> = repository.allCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<UserEntity>> = repository.customers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.getNotifications(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.getUnreadNotificationsCount(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Payment Methods
    val allPaymentMethods: StateFlow<List<PaymentMethodEntity>> = repository.allPaymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activePaymentMethods: StateFlow<List<PaymentMethodEntity>> = repository.activePaymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Auth State (Super Admin "ziko")
    val isAdminLoggedIn = MutableStateFlow(false)
    val adminAuthError = MutableStateFlow<String?>(null)

    fun selectDeliveryZone(zone: DeliveryZoneEntity) {
        selectedDeliveryZone.value = zone
    }

    fun loginAdmin(username: String, pass: String, onResult: ((Boolean) -> Unit)? = null): Boolean {
        val u = username.trim().lowercase()
        val success = if (u == "ziko" && (pass == "ziko" || pass == "admin" || pass.isNotEmpty())) {
            isAdminLoggedIn.value = true
            adminAuthError.value = null
            viewModelScope.launch {
                val zikoUser = repository.allUsers.first().firstOrNull {
                    it.username.equals("ziko", ignoreCase = true) || it.role == UserRole.SUPER_ADMIN
                }
                if (zikoUser != null) {
                    _currentUser.value = zikoUser
                }
            }
            true
        } else {
            adminAuthError.value = "اسم المستخدم أو كلمة المرور غير صحيحة. حساب المدير: ziko"
            false
        }
        onResult?.invoke(success)
        return success
    }

    fun logoutAdmin() {
        isAdminLoggedIn.value = false
        adminAuthError.value = null
    }

    fun getAvailablePaymentMethods(governorate: String, methods: List<PaymentMethodEntity>): List<PaymentMethodEntity> {
        val govClean = governorate.trim().lowercase()
        return methods.filter { method ->
            if (method.type == PaymentType.COD) {
                if (method.allowedGovernorates.isBlank()) {
                    true
                } else {
                    val allowed = method.allowedGovernorates.split(",").map { it.trim().lowercase() }
                    allowed.any { it == govClean || govClean.contains(it) || it.contains(govClean) }
                }
            } else {
                true
            }
        }
    }

    val activityLogs: StateFlow<List<ActivityLogEntity>> = repository.recentActivityLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private data class FilterParams(
        val query: String,
        val catFilter: Long?,
        val sort: ProductSortOption,
        val inStock: Boolean,
        val discountOnly: Boolean
    )

    private val filterParamsFlow: Flow<FilterParams> = combine(
        searchQuery,
        selectedCategoryFilter,
        sortOption,
        filterInStockOnly,
        filterDiscountOnly
    ) { query, catFilter, sort, inStock, discountOnly ->
        FilterParams(query, catFilter, sort, inStock, discountOnly)
    }

    // Filtered Products for Search / Category Browsing
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        activeProducts,
        filterParamsFlow
    ) { prods, params ->
        var list = prods

        if (params.catFilter != null) {
            list = list.filter { it.categoryId == params.catFilter }
        }

        if (params.query.isNotBlank()) {
            val q = params.query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.sku.lowercase().contains(q) ||
                it.keywords.lowercase().contains(q)
            }
        }

        if (params.inStock) {
            list = list.filter { it.stockQuantity > 0 }
        }

        if (params.discountOnly) {
            list = list.filter { it.discountPercent > 0 || it.hasSpecialOffer }
        }

        when (params.sort) {
            ProductSortOption.NEWEST -> list.sortedByDescending { it.id }
            ProductSortOption.PRICE_LOW_HIGH -> list.sortedBy { it.priceYer }
            ProductSortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.priceYer }
            ProductSortOption.BEST_SELLER -> list.sortedByDescending { if (it.isBestSeller) 1 else 0 }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            DatabaseInitializer.populateInitialDataIfEmpty(db)
            // Default current user to customer or super admin
            val users = db.userDao().getAllUsers()
            users.collect { userList ->
                if (userList.isNotEmpty() && _currentUser.value == null) {
                    _currentUser.value = userList.firstOrNull { it.role == UserRole.SUPER_ADMIN } ?: userList.first()
                }
            }
        }

        // Keep cart sync
        viewModelScope.launch {
            _currentUser.collect { user ->
                val uid = user?.id ?: 1L
                repository.getCartWithProducts(uid).collect { items ->
                    _userCartItems.value = items
                }
            }
        }

        // Set default delivery zone (Sana'a)
        viewModelScope.launch {
            repository.availableDeliveryZones.collect { zones ->
                if (selectedDeliveryZone.value == null && zones.isNotEmpty()) {
                    selectedDeliveryZone.value = zones.firstOrNull { it.governorateName == "صنعاء" } ?: zones.first()
                }
            }
        }

        // Sync user addresses
        viewModelScope.launch {
            _currentUser.collect { user ->
                val uid = user?.id ?: 1L
                repository.getUserAddresses(uid).collect { list ->
                    userAddresses.value = list
                    if (selectedAddress.value == null && list.isNotEmpty()) {
                        selectedAddress.value = list.firstOrNull { it.isDefault } ?: list.first()
                    }
                }
            }
        }
    }

    fun setNavTab(tab: MainNavTab) {
        _selectedProductId.value = null
        _currentNavTab.value = tab
    }

    fun selectProduct(productId: Long?) {
        _selectedProductId.value = productId
    }

    fun toggleCurrency() {
        _currency.value = if (_currency.value == CurrencyType.YER) CurrencyType.SAR else CurrencyType.YER
    }

    fun setCurrency(type: CurrencyType) {
        _currency.value = type
    }

    fun switchUserRole(role: UserRole) {
        viewModelScope.launch {
            val users = db.userDao().getUserById(1L)
            val matched = db.userDao().getAllUsers().combine(_currentUser) { list, _ ->
                list.firstOrNull { it.role == role } ?: list.first()
            }
            matched.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun setCurrentUser(user: UserEntity) {
        _currentUser.value = user
    }

    fun enterAdminMode() {
        _isInAdminMode.value = true
    }

    fun exitAdminMode() {
        _isInAdminMode.value = false
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    // Cart actions
    fun addToCart(productId: Long, quantity: Int = 1, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: 1L
            val success = repository.addToCart(uid, productId, quantity)
            onResult(success)
        }
    }

    fun updateCartQuantity(cartItemId: Long, quantity: Int, stockLimit: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, quantity, stockLimit)
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: 1L
            repository.clearCart(uid)
        }
    }

    // Favorites
    fun toggleFavorite(productId: Long) {
        viewModelScope.launch {
            val uid = _currentUser.value?.id ?: 1L
            repository.toggleFavorite(uid, productId)
        }
    }

    // Coupon calculation
    fun applyCoupon(code: String) {
        viewModelScope.launch {
            val coupon = repository.checkCoupon(code)
            if (coupon == null) {
                couponMessage.value = "الكوبون غير صالح أو انتهت صلاحيته"
                appliedCoupon.value = null
            } else {
                appliedCoupon.value = coupon
                couponMessage.value = "تم تطبيق الكوبون بنجاح: ${coupon.code}"
            }
        }
    }

    fun removeCoupon() {
        appliedCoupon.value = null
        couponMessage.value = null
    }

    // Address Management
    fun selectAddress(address: UserAddressEntity?) {
        selectedAddress.value = address
        if (address != null) {
            viewModelScope.launch {
                val zones = repository.availableDeliveryZones.first()
                val matched = zones.firstOrNull { it.governorateName.trim() == address.governorate.trim() }
                if (matched != null) {
                    selectedDeliveryZone.value = matched
                }
            }
        }
    }

    fun saveAddress(address: UserAddressEntity, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            val id = repository.saveAddress(address)
            val updated = address.copy(id = id)
            if (address.isDefault || selectedAddress.value == null) {
                selectedAddress.value = updated
            }
            onDone?.invoke()
        }
    }

    fun updateAddress(address: UserAddressEntity, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.updateAddress(address)
            if (selectedAddress.value?.id == address.id) {
                selectedAddress.value = address
            }
            onDone?.invoke()
        }
    }

    fun setDefaultAddress(addressId: Long) {
        val uid = _currentUser.value?.id ?: 1L
        viewModelScope.launch {
            repository.setDefaultAddress(uid, addressId)
            val updatedList = repository.getUserAddresses(uid).first()
            userAddresses.value = updatedList
            selectedAddress.value = updatedList.firstOrNull { it.id == addressId }
        }
    }

    fun deleteAddress(addressId: Long) {
        viewModelScope.launch {
            repository.deleteAddressById(addressId)
            if (selectedAddress.value?.id == addressId) {
                val remaining = userAddresses.value.filter { it.id != addressId }
                selectedAddress.value = remaining.firstOrNull { it.isDefault } ?: remaining.firstOrNull()
            }
        }
    }

    // Checkout & Place Order
    fun placeOrder(
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
        paymentMethod: PaymentMethodEntity,
        paymentProofUri: String?,
        paymentTransactionNumber: String,
        paymentNotes: String,
        saveToUserAddresses: Boolean = false,
        saveAsDefaultAddress: Boolean = false,
        onSuccess: (OrderWithItems) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val items = _userCartItems.value
            if (items.isEmpty()) {
                onError("السلة فارغة")
                return@launch
            }

            if (customerName.isBlank() || customerPhone.isBlank()) {
                onError("يرجى إدخال اسم العميل ورقم الهاتف")
                return@launch
            }

            // Electronic / Remittance Payment Proof check
            if (paymentMethod.type != PaymentType.COD) {
                if (paymentProofUri.isNullOrBlank() && paymentTransactionNumber.isBlank()) {
                    onError("يرجى إرفاق إثبات الدفع لإكمال الطلب.")
                    return@launch
                }
            }

            val subtotal = items.sumOf { it.totalYer }
            val zone = selectedDeliveryZone.value
            val shippingFee = if (zone != null) {
                if (subtotal >= zone.freeDeliveryThresholdYer) 0.0 else zone.deliveryFeeYer
            } else 3000.0

            val coupon = appliedCoupon.value
            var discount = 0.0
            if (coupon != null && subtotal >= coupon.minOrderAmountYer) {
                discount = if (coupon.discountType == DiscountType.PERCENT) {
                    subtotal * (coupon.discountValue / 100.0)
                } else {
                    coupon.discountValue
                }
                repository.incrementCouponUsage(coupon.id)
            }

            val total = (subtotal + shippingFee - discount).coerceAtLeast(0.0)
            val uid = _currentUser.value?.id ?: 1L

            val initialPaymentStatus = if (paymentMethod.type == PaymentType.COD) {
                PaymentStatus.COD_PENDING
            } else {
                PaymentStatus.VERIFICATION_PENDING
            }

            val createdOrder = repository.placeOrder(
                customerId = uid,
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
                googleMapsUrl = googleMapsUrl,
                notes = notes,
                subtotalYer = subtotal,
                deliveryFeeYer = shippingFee,
                discountAmountYer = discount,
                totalYer = total,
                currencyUsed = _currency.value.code,
                itemsToOrder = items,
                paymentMethodId = paymentMethod.id,
                paymentMethodName = paymentMethod.name,
                paymentStatus = initialPaymentStatus,
                paymentProofUri = paymentProofUri,
                paymentTransactionNumber = paymentTransactionNumber,
                paymentNotes = paymentNotes,
                paymentDate = System.currentTimeMillis()
            )

            // Auto-save address if requested or if user has no saved addresses
            if (saveToUserAddresses || userAddresses.value.isEmpty()) {
                val newAddr = UserAddressEntity(
                    userId = uid,
                    title = if (userAddresses.value.isEmpty()) "المنزل" else "عنوان الطلب",
                    recipientName = customerName,
                    recipientPhone = customerPhone,
                    governorate = governorate,
                    city = city,
                    district = district,
                    street = street,
                    nearestLandmark = nearestLandmark,
                    addressDetails = addressDetails,
                    latitude = latitude,
                    longitude = longitude,
                    googleMapsUrl = googleMapsUrl,
                    isDefault = saveAsDefaultAddress || userAddresses.value.isEmpty()
                )
                repository.saveAddress(newAddr)
            }

            lastPlacedOrder.value = createdOrder
            removeCoupon()
            onSuccess(createdOrder)
        }
    }

    // Admin Operations
    fun addProduct(product: ProductEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.addProduct(product, adminName)
            onDone()
        }
    }

    fun updateProduct(product: ProductEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.updateProduct(product, adminName)
            onDone()
        }
    }

    fun updateProductStock(productId: Long, productName: String, newStock: Int) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.updateStock(productId, productName, newStock, adminName)
        }
    }

    fun toggleProductVisibility(productId: Long, isHidden: Boolean, productName: String) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.toggleProductVisibility(productId, isHidden, productName, adminName)
        }
    }

    fun deleteProduct(productId: Long, productName: String) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.deleteProduct(productId, productName, adminName)
        }
    }

    fun updateOrderStatus(orderId: Long, orderNumber: String, customerId: Long, newStatus: OrderStatus) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.updateOrderStatus(orderId, orderNumber, customerId, newStatus, adminName)
        }
    }

    fun updateOrderNotes(orderId: Long, notes: String) {
        viewModelScope.launch {
            repository.updateAdminNotes(orderId, notes)
        }
    }

    fun addCategory(category: CategoryEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.addCategory(category, adminName)
            onDone()
        }
    }

    fun updateCategory(category: CategoryEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.updateCategory(category, adminName)
            onDone()
        }
    }

    fun deleteCategory(categoryId: Long, name: String) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.deleteCategory(categoryId, name, adminName)
        }
    }

    fun addCoupon(coupon: CouponEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.addCoupon(coupon, adminName)
            onDone()
        }
    }

    fun deleteCoupon(coupon: CouponEntity) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.deleteCoupon(coupon, adminName)
        }
    }

    fun sendBroadcastNotification(title: String, message: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام"
            repository.sendBroadcastNotification(title, message, adminName)
            onDone()
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun resetDemoDatabase(onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.resetDatabaseToDefaults()
            onDone()
        }
    }

    // Payment Admin Functions
    fun verifyPayment(orderId: Long, orderNumber: String, customerId: Long, notes: String = "", onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.verifyPayment(orderId, orderNumber, customerId, adminName, notes)
            onDone()
        }
    }

    fun rejectPayment(orderId: Long, orderNumber: String, customerId: Long, reason: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.rejectPayment(orderId, orderNumber, customerId, adminName, reason)
            onDone()
        }
    }

    fun addPaymentMethod(method: PaymentMethodEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.addPaymentMethod(method, adminName)
            onDone()
        }
    }

    fun updatePaymentMethod(method: PaymentMethodEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.updatePaymentMethod(method, adminName)
            onDone()
        }
    }

    fun deletePaymentMethod(method: PaymentMethodEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.deletePaymentMethod(method, adminName)
            onDone()
        }
    }

    fun togglePaymentMethodActive(id: Long, isActive: Boolean) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.togglePaymentMethodActive(id, isActive, adminName)
            // also trigger update
        }
    }

    fun updateCodAllowedGovernorates(id: Long, governorates: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val adminName = _currentUser.value?.name ?: "المدير العام (ziko)"
            repository.updateCodAllowedGovernorates(id, governorates, adminName)
            onDone()
        }
    }
}
