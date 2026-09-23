package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.OrderWithItems
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.RoyalBottomBar
import com.example.ui.components.RoyalTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.admin.AdminPanelScreen
import com.example.ui.theme.AlMalakiTheme
import com.example.ui.theme.ObsidianBlack
import com.example.ui.viewmodel.MainNavTab
import com.example.ui.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlMalakiTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AlMalakiStoreApp()
                }
            }
        }
    }
}

@Composable
fun AlMalakiStoreApp(
    viewModel: StoreViewModel = viewModel()
) {
    val currentTab by viewModel.currentNavTab.collectAsState()
    val isInAdminMode by viewModel.isInAdminMode.collectAsState()
    val selectedProductId by viewModel.selectedProductId.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.userCartItems.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val unreadNotifs by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var isCheckingOut by remember { mutableStateOf(false) }
    var successOrder by remember { mutableStateOf<OrderWithItems?>(null) }
    var isViewingOrders by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    // If Admin mode is active, display the Admin Panel full-screen
    if (isInAdminMode) {
        AdminPanelScreen(viewModel = viewModel)
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ObsidianBlack,
        topBar = {
            // Only show TopBar if not in deep screens (checkout / product detail / success)
            if (selectedProductId == null && !isCheckingOut && successOrder == null && !isViewingOrders) {
                RoyalTopBar(
                    selectedCurrency = currency,
                    onToggleCurrency = { viewModel.toggleCurrency() },
                    unreadNotifications = unreadNotifs,
                    cartItemCount = cartItems.size,
                    userRole = currentUser?.role,
                    onOpenSearch = { viewModel.setNavTab(MainNavTab.CATEGORIES) },
                    onOpenCart = { viewModel.setNavTab(MainNavTab.CART) },
                    onOpenAdmin = { viewModel.enterAdminMode() },
                    onOpenNotifications = { showNotificationsDialog = true }
                )
            }
        },
        bottomBar = {
            // Only show BottomBar on standard root tabs
            if (selectedProductId == null && !isCheckingOut && successOrder == null && !isViewingOrders) {
                RoyalBottomBar(
                    currentTab = currentTab,
                    cartItemCount = cartItems.sumOf { it.cartItem.quantity },
                    favoritesCount = favoriteIds.size,
                    onTabSelected = { tab ->
                        viewModel.setNavTab(tab)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ObsidianBlack)
        ) {
            when {
                // Success Screen
                successOrder != null -> {
                    OrderSuccessScreen(
                        orderWithItems = successOrder!!,
                        onViewOrders = {
                            successOrder = null
                            isViewingOrders = true
                        },
                        onBackHome = {
                            successOrder = null
                            viewModel.setNavTab(MainNavTab.HOME)
                        }
                    )
                }

                // Customer Orders Tracking Screen
                isViewingOrders -> {
                    OrdersScreen(
                        viewModel = viewModel
                    )
                }

                // Checkout Screen
                isCheckingOut -> {
                    CheckoutScreen(
                        viewModel = viewModel,
                        onBack = { isCheckingOut = false },
                        onOrderSuccess = { order ->
                            isCheckingOut = false
                            successOrder = order
                        }
                    )
                }

                // Product Detail View
                selectedProductId != null -> {
                    ProductDetailScreen(
                        productId = selectedProductId!!,
                        viewModel = viewModel,
                        onBack = { viewModel.selectProduct(null) },
                        onNavigateToCheckout = {
                            viewModel.selectProduct(null)
                            isCheckingOut = true
                        }
                    )
                }

                // Main Tabs
                else -> {
                    when (currentTab) {
                        MainNavTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onProductClick = { id -> viewModel.selectProduct(id) }
                            )
                        }
                        MainNavTab.CATEGORIES -> {
                            CategoriesScreen(
                                viewModel = viewModel,
                                onProductClick = { id -> viewModel.selectProduct(id) }
                            )
                        }
                        MainNavTab.CART -> {
                            CartScreen(
                                viewModel = viewModel,
                                onNavigateToCheckout = { isCheckingOut = true }
                            )
                        }
                        MainNavTab.FAVORITES -> {
                            FavoritesScreen(
                                viewModel = viewModel,
                                onProductClick = { id -> viewModel.selectProduct(id) }
                            )
                        }
                        MainNavTab.ACCOUNT -> {
                            AccountScreen(
                                viewModel = viewModel,
                                onViewOrders = { isViewingOrders = true }
                            )
                        }
                    }
                }
            }
        }
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { showNotificationsDialog = false },
            onMarkAsRead = { id -> viewModel.markNotificationAsRead(id) }
        )
    }
}
