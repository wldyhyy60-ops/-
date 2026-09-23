package com.example.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.CouponEntity
import com.example.data.model.DiscountType
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusPreparing
import com.example.ui.theme.StatusShipping
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper
import com.example.ui.viewmodel.AdminTab
import com.example.ui.viewmodel.StoreViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.PaymentType

@Composable
fun AdminPanelScreen(
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.adminTab.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Admin Top Bar
        Surface(
            color = CardDark,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .border(1.dp, CardBorderGold.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.exitAdminMode() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "الخروج من لوحة التحكم",
                            tint = GoldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "لوحة التحكم الملكية 👑",
                            color = GoldPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentUser?.name} (${currentUser?.role?.name})",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldContainer)
                        .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                        .clickable { viewModel.exitAdminMode() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = "عرض المتجر", color = TextGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Admin Tabs Horizontal Scroll
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(AdminTab.entries) { tab ->
                val isSelected = currentTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) GoldPrimary else CardDark)
                        .border(1.dp, if (isSelected) GoldAccent else CardBorderGold, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setAdminTab(tab) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = tab.titleAr,
                        color = if (isSelected) ObsidianBlack else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Selected Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentTab) {
                AdminTab.DASHBOARD -> AdminDashboardView(viewModel)
                AdminTab.PAYMENTS -> AdminPaymentsView(viewModel)
                AdminTab.PRODUCTS -> AdminProductsView(viewModel)
                AdminTab.ORDERS -> AdminOrdersView(viewModel)
                AdminTab.INVENTORY -> AdminInventoryView(viewModel)
                AdminTab.CATEGORIES -> AdminCategoriesView(viewModel)
                AdminTab.CUSTOMERS -> AdminCustomersView(viewModel)
                AdminTab.COUPONS -> AdminCouponsView(viewModel)
                AdminTab.LOGS -> AdminLogsView(viewModel)
                AdminTab.BACKUP -> AdminBackupView(viewModel)
            }
        }
    }
}

// 4.1 Dashboard
@Composable
private fun AdminDashboardView(viewModel: StoreViewModel) {
    val allOrders by viewModel.allOrders.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val lowStock by viewModel.lowStockProducts.collectAsState()
    val outOfStock by viewModel.outOfStockProducts.collectAsState()

    val totalSalesYer = allOrders.filter { it.status != OrderStatus.CANCELLED }.sumOf { it.totalYer }
    val newOrdersCount = allOrders.count { it.status == OrderStatus.NEW || it.status == OrderStatus.UNDER_REVIEW }
    val shippingCount = allOrders.count { it.status == OrderStatus.SHIPPING || it.status == OrderStatus.PREPARING }
    val deliveredCount = allOrders.count { it.status == OrderStatus.DELIVERED }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp)
    ) {
        item {
            Text(
                text = "الإحصائيات العامة للمتجر 📊",
                color = GoldPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        // Sales Metric Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, GoldPrimary, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "إجمالي المبيعات المحققة", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${totalSalesYer.toInt()} ر.ي",
                        color = GoldPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ما يعادل تقريباً ${(totalSalesYer / 148.0).toInt()} ر.س",
                        color = TextGold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Metrics Grid (2 columns)
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricTile(title = "إجمالي الطلبات", value = allOrders.size.toString(), color = TextPrimary, modifier = Modifier.weight(1f))
                AdminMetricTile(title = "طلبات جديدة 🔔", value = newOrdersCount.toString(), color = StatusNew, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricTile(title = "قيد التجهيز والشحن", value = shippingCount.toString(), color = StatusPreparing, modifier = Modifier.weight(1f))
                AdminMetricTile(title = "طلبات مكتملة ✅", value = deliveredCount.toString(), color = StatusDelivered, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricTile(title = "إجمالي المنتجات", value = allProducts.size.toString(), color = TextPrimary, modifier = Modifier.weight(1f))
                AdminMetricTile(title = "عدد العملاء", value = allUsers.size.toString(), color = TextPrimary, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricTile(title = "مخزون قارب على النفاد", value = lowStock.size.toString(), color = WarningAmber, modifier = Modifier.weight(1f))
                AdminMetricTile(title = "منتجات نفدت ⚠️", value = outOfStock.size.toString(), color = ErrorRed, modifier = Modifier.weight(1f))
            }
        }

        // Broadcast Notification to all customers
        item {
            Spacer(modifier = Modifier.height(16.dp))
            BroadcastNotificationCard(viewModel)
        }
    }
}

@Composable
private fun AdminMetricTile(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun BroadcastNotificationCard(viewModel: StoreViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "📢 إرسال إشعار عام لكافة العملاء", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("عنوان الإشعار (مثال: عرض ملكي خاص)", fontSize = 11.sp, color = TextMuted) },
                singleLine = true,
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text("نص الرسالة الترويجية...", fontSize = 11.sp, color = TextMuted) },
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(70.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && message.isNotBlank()) {
                        viewModel.sendBroadcastNotification(title, message) {
                            Toast.makeText(context, "تم إرسال الإشعار لجميع المستخدمين", Toast.LENGTH_SHORT).show()
                            title = ""
                            message = ""
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Text(text = "إرسال الإشعار الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 4.2 Products Management
@Composable
private fun AdminProductsView(viewModel: StoreViewModel) {
    val allProducts by viewModel.allProducts.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = if (searchQuery.isBlank()) allProducts else {
        val q = searchQuery.lowercase()
        allProducts.filter { it.name.lowercase().contains(q) || it.sku.lowercase().contains(q) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "المنتجات (${filteredList.size})", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "إضافة منتج", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Search in products
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("بحث في المنتجات بالاسم أو كود SKU...", fontSize = 11.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
            colors = outlinedFieldColors(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .height(48.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList, key = { it.id }) { product ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.name,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            // Hidden status
                            if (product.isHidden) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ErrorRed.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "مخفي", color = ErrorRed, fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "السعر: ${product.priceYer.toInt()} ر.ي (${product.priceSar.toInt()} ر.س)", color = GoldPrimary, fontSize = 12.sp)
                            Text(
                                text = "المخزون: ${product.stockQuantity} قطعة",
                                color = if (product.stockQuantity <= 3) ErrorRed else StatusDelivered,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (product.sku.isNotBlank()) {
                            Text(text = "SKU: ${product.sku}", color = TextMuted, fontSize = 10.sp)
                        }

                        Divider(color = CardBorderGold.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 6.dp))

                        // Product Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Toggle Hide
                            IconButton(onClick = {
                                viewModel.toggleProductVisibility(product.id, !product.isHidden, product.name)
                            }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = if (product.isHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "إظهار/إخفاء",
                                    tint = if (product.isHidden) ErrorRed else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Edit
                            IconButton(onClick = { editingProduct = product }, modifier = Modifier.size(32.dp)) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "تعديل", tint = GoldPrimary, modifier = Modifier.size(18.dp))
                            }

                            // Delete
                            IconButton(onClick = {
                                viewModel.deleteProduct(product.id, product.name)
                                Toast.makeText(context, "تم حذف المنتج: ${product.name}", Toast.LENGTH_SHORT).show()
                            }, modifier = Modifier.size(32.dp)) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = ErrorRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Product Dialog
    if (showAddDialog || editingProduct != null) {
        ProductFormDialog(
            categories = categories,
            initialProduct = editingProduct,
            onDismiss = {
                showAddDialog = false
                editingProduct = null
            },
            onSave = { prod ->
                if (editingProduct == null) {
                    viewModel.addProduct(prod) {
                        Toast.makeText(context, "تمت إضافة المنتج بنجاح", Toast.LENGTH_SHORT).show()
                        showAddDialog = false
                    }
                } else {
                    viewModel.updateProduct(prod.copy(id = editingProduct!!.id)) {
                        Toast.makeText(context, "تم تعديل المنتج بنجاح", Toast.LENGTH_SHORT).show()
                        editingProduct = null
                    }
                }
            }
        )
    }
}

// Product Form Dialog (Add / Edit)
@Composable
private fun ProductFormDialog(
    categories: List<CategoryEntity>,
    initialProduct: ProductEntity?,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var categoryId by remember { mutableLongStateOf(initialProduct?.categoryId ?: (categories.firstOrNull()?.id ?: 1L)) }
    var subCategory by remember { mutableStateOf(initialProduct?.subCategory ?: "") }
    var priceYerStr by remember { mutableStateOf(initialProduct?.priceYer?.toInt()?.toString() ?: "") }
    var priceSarStr by remember { mutableStateOf(initialProduct?.priceSar?.toInt()?.toString() ?: "") }
    var oldPriceYerStr by remember { mutableStateOf(initialProduct?.originalPriceYer?.toInt()?.toString() ?: "") }
    var discountPercentStr by remember { mutableStateOf(initialProduct?.discountPercent?.toString() ?: "0") }
    var stockStr by remember { mutableStateOf(initialProduct?.stockQuantity?.toString() ?: "10") }
    var sku by remember { mutableStateOf(initialProduct?.sku ?: "") }
    var description by remember { mutableStateOf(initialProduct?.description ?: "") }
    var specs by remember { mutableStateOf(initialProduct?.specifications ?: "") }
    var isBestSeller by remember { mutableStateOf(initialProduct?.isBestSeller ?: false) }
    var isNewArrival by remember { mutableStateOf(initialProduct?.isNewArrival ?: false) }
    var hasSpecialOffer by remember { mutableStateOf(initialProduct?.hasSpecialOffer ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardDark,
        title = {
            Text(
                text = if (initialProduct == null) "إضافة منتج ملكي جديد 👑" else "تعديل المنتج",
                color = GoldPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(420.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("اسم المنتج *", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    // Category Selector
                    Column {
                        Text(text = "القسم:", color = TextSecondary, fontSize = 11.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(categories) { cat ->
                                val selected = categoryId == cat.id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) GoldPrimary else ObsidianBlack)
                                        .border(1.dp, if (selected) GoldAccent else CardBorderGold, RoundedCornerShape(8.dp))
                                        .clickable { categoryId = cat.id }
                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    Text(text = cat.nameAr, color = if (selected) ObsidianBlack else TextPrimary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = subCategory,
                        onValueChange = { subCategory = it },
                        label = { Text("القسم الفرعي", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = priceYerStr,
                            onValueChange = { priceYerStr = it },
                            label = { Text("السعر (ر.ي) *", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = priceSarStr,
                            onValueChange = { priceSarStr = it },
                            label = { Text("السعر (ر.س)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = stockStr,
                            onValueChange = { stockStr = it },
                            label = { Text("المخزون *", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = sku,
                            onValueChange = { sku = it },
                            label = { Text("كود SKU", fontSize = 11.sp) },
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("الوصف التفصيلي *", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = specs,
                        onValueChange = { specs = it },
                        label = { Text("المواصفات (كل سطر خاصية)", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Badges toggles
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "الأكثر مبيعاً 👑", color = TextPrimary, fontSize = 12.sp)
                        Switch(
                            checked = isBestSeller,
                            onCheckedChange = { isBestSeller = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "عرض خاص وخصم 🔥", color = TextPrimary, fontSize = 12.sp)
                        Switch(
                            checked = hasSpecialOffer,
                            onCheckedChange = { hasSpecialOffer = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceYer = priceYerStr.toDoubleOrNull() ?: 0.0
                    val priceSar = priceSarStr.toDoubleOrNull() ?: (priceYer / 148.0)
                    val stock = stockStr.toIntOrNull() ?: 10
                    if (name.isNotBlank() && priceYer > 0) {
                        val product = ProductEntity(
                            id = initialProduct?.id ?: 0,
                            name = name.trim(),
                            categoryId = categoryId,
                            subCategory = subCategory.trim(),
                            description = description.trim().ifEmpty { name },
                            specifications = specs.trim(),
                            priceYer = priceYer,
                            priceSar = priceSar,
                            stockQuantity = stock,
                            sku = sku.trim(),
                            isBestSeller = isBestSeller,
                            isNewArrival = isNewArrival,
                            hasSpecialOffer = hasSpecialOffer
                        )
                        onSave(product)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
            ) {
                Text(text = "حفظ المنتج")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "إلغاء", color = TextMuted)
            }
        }
    )
}

// 4.3 Orders Management
@Composable
private fun AdminOrdersView(viewModel: StoreViewModel) {
    val allOrders by viewModel.allOrders.collectAsState()
    val context = LocalContext.current

    var selectedStatusFilter by remember { mutableStateOf<OrderStatus?>(null) }
    var searchOrderQuery by remember { mutableStateOf("") }
    var expandedOrderId by remember { mutableStateOf<Long?>(null) }

    val filteredOrders = allOrders.filter { order ->
        val matchesStatus = selectedStatusFilter == null || order.status == selectedStatusFilter
        val matchesSearch = searchOrderQuery.isBlank() ||
                order.orderNumber.contains(searchOrderQuery, ignoreCase = true) ||
                order.customerName.contains(searchOrderQuery, ignoreCase = true) ||
                order.customerPhone.contains(searchOrderQuery)
        matchesStatus && matchesSearch
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "إدارة الطلبات والمتابعة (${filteredOrders.size})",
            color = GoldPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )

        // Search Bar for Orders
        OutlinedTextField(
            value = searchOrderQuery,
            onValueChange = { searchOrderQuery = it },
            placeholder = { Text("بحث برقم الطلب، اسم العميل، أو الهاتف...", fontSize = 11.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
            colors = outlinedFieldColors(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .height(48.dp)
        )

        // Status Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                StatusFilterChip(title = "الكل", isSelected = selectedStatusFilter == null, onClick = { selectedStatusFilter = null })
            }
            items(OrderStatus.entries) { st ->
                StatusFilterChip(
                    title = st.titleAr,
                    isSelected = selectedStatusFilter == st,
                    onClick = { selectedStatusFilter = if (selectedStatusFilter == st) null else st }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredOrders, key = { it.id }) { order ->
                var showStatusMenu by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = order.orderNumber, color = GoldPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                            // Status Dropdown selector
                            Box {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GoldContainer)
                                        .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                                        .clickable { showStatusMenu = true }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = order.status.titleAr, color = TextGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = GoldPrimary)
                                }

                                DropdownMenu(
                                    expanded = showStatusMenu,
                                    onDismissRequest = { showStatusMenu = false },
                                    modifier = Modifier.background(CardDark)
                                ) {
                                    OrderStatus.entries.forEach { statusOption ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(text = statusOption.titleAr, color = if (order.status == statusOption) GoldPrimary else TextPrimary)
                                            },
                                            onClick = {
                                                viewModel.updateOrderStatus(order.id, order.orderNumber, order.customerId, statusOption)
                                                showStatusMenu = false
                                                Toast.makeText(context, "تم تغيير حالة الطلب إلى ${statusOption.titleAr}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(text = "العميل: ${order.customerName} (${order.customerPhone})", color = TextPrimary, fontSize = 12.sp)
                        val fullAddress = order.formattedDetailedAddress()
                        Text(text = "العنوان: $fullAddress", color = TextSecondary, fontSize = 11.sp)

                        if (order.latitude != null && order.longitude != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldContainer.copy(alpha = 0.4f))
                                    .clickable {
                                        com.example.util.LocationAndGeocodingHelper.openInGoogleMaps(context, order.latitude, order.longitude)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "📍 موقع العميل على Google Maps ↗", color = GoldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(text = "المبلغ: ${order.totalYer.toInt()} ر.ي", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                        if (order.notes.isNotBlank()) {
                            Text(text = "ملاحظة: ${order.notes}", color = TextMuted, fontSize = 10.sp)
                        }

                        Divider(color = CardBorderGold.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 6.dp))

                        // Actions: WhatsApp direct chat with customer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dateStr = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault()).format(Date(order.createdAt))
                            Text(text = dateStr, color = TextMuted, fontSize = 10.sp)

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WhatsAppGreen)
                                    .clickable {
                                        val msg = "مرحباً يا ${order.customerName}، نتواصل معك من المتجر الملكي بخصوص طلبك ${order.orderNumber}."
                                        WhatsAppHelper.openWhatsApp(context, phone = "967" + order.customerPhone.trimStart('0'), message = msg)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "مراسلة العميل", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusFilterChip(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GoldPrimary else CardDark)
            .border(1.dp, if (isSelected) GoldAccent else CardBorderGold, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = title, color = if (isSelected) ObsidianBlack else TextPrimary, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

// 4.4 Inventory Management
@Composable
private fun AdminInventoryView(viewModel: StoreViewModel) {
    val allProducts by viewModel.allProducts.collectAsState()
    val lowStock by viewModel.lowStockProducts.collectAsState()
    val outOfStock by viewModel.outOfStockProducts.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "إدارة المخزون والتنبيهات 🏷️", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        if (outOfStock.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, ErrorRed, RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "يوجد ${outOfStock.size} منتجات نفد مخزونها تماماً (0 قطع)", color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (lowStock.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = WarningAmber.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, WarningAmber, RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "يوجد ${lowStock.size} منتجات مخزونها منخفض (5 قطع أو أقل)", color = WarningAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(allProducts, key = { it.id }) { product ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = product.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(text = "SKU: ${product.sku.ifEmpty { "#" + product.id }}", color = TextMuted, fontSize = 11.sp)
                    }

                    // Stock Controls (+ / -)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val newStock = (product.stockQuantity - 1).coerceAtLeast(0)
                                viewModel.updateProductStock(product.id, product.name, newStock)
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Text(text = "—", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (product.stockQuantity == 0) ErrorRed.copy(alpha = 0.2f) else ObsidianBlack)
                                .border(1.dp, if (product.stockQuantity == 0) ErrorRed else GoldPrimary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${product.stockQuantity}",
                                color = if (product.stockQuantity == 0) ErrorRed else TextGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.updateProductStock(product.id, product.name, product.stockQuantity + 5)
                                Toast.makeText(context, "تمت إضافة 5 قطع لمخزون: ${product.name}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "+5", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// 4.5 Categories Management
@Composable
private fun AdminCategoriesView(viewModel: StoreViewModel) {
    val categories by viewModel.allCategories.collectAsState()
    val context = LocalContext.current

    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryDesc by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "إدارة الأقسام الرئيسية 📂", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        // Add Category Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "إضافة قسم جديد", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        placeholder = { Text("اسم القسم (مثال: الساعات الفاخرة)", fontSize = 11.sp, color = TextMuted) },
                        singleLine = true,
                        colors = outlinedFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newCategoryDesc,
                        onValueChange = { newCategoryDesc = it },
                        placeholder = { Text("وصف مختصر للقسم...", fontSize = 11.sp, color = TextMuted) },
                        singleLine = true,
                        colors = outlinedFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                val cat = CategoryEntity(nameAr = newCategoryName.trim(), description = newCategoryDesc.trim())
                                viewModel.addCategory(cat) {
                                    Toast.makeText(context, "تمت إضافة القسم بنجاح", Toast.LENGTH_SHORT).show()
                                    newCategoryName = ""
                                    newCategoryDesc = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Text(text = "إضافة القسم", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(categories, key = { it.id }) { cat ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = cat.nameAr, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        if (cat.description.isNotBlank()) {
                            Text(text = cat.description, color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = {
                        viewModel.deleteCategory(cat.id, cat.nameAr)
                        Toast.makeText(context, "تم حذف القسم: ${cat.nameAr}", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = ErrorRed)
                    }
                }
            }
        }
    }
}

// 4.6 Customers Management
@Composable
private fun AdminCustomersView(viewModel: StoreViewModel) {
    val allUsers by viewModel.allUsers.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "قائمة العملاء والمستخدمين (${allUsers.size}) 👥", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        items(allUsers, key = { it.id }) { user ->
            val userOrders = allOrders.filter { it.customerId == user.id }
            val totalSpent = userOrders.sumOf { it.totalYer }

            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = user.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = user.role.name, color = TextGold, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "الهاتف: ${user.phone} • المحافظة: ${user.governorate}", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "عدد الطلبات: ${userOrders.size} • إجمالي الإنفاق: ${totalSpent.toInt()} ر.ي", color = GoldPrimary, fontSize = 11.sp)
                }
            }
        }
    }
}

// 4.7 Coupons & Offers Management
@Composable
private fun AdminCouponsView(viewModel: StoreViewModel) {
    val allCoupons by viewModel.allCoupons.collectAsState()
    val context = LocalContext.current

    var newCode by remember { mutableStateOf("") }
    var discountValueStr by remember { mutableStateOf("20") }
    var minOrderStr by remember { mutableStateOf("10000") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "إدارة كوبونات الخصم والعروض 🏷️", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "إنشاء كوبون جديد", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newCode,
                        onValueChange = { newCode = it.uppercase() },
                        placeholder = { Text("رمز الكوبون (مثال: ROYAL30)", fontSize = 11.sp, color = TextMuted) },
                        singleLine = true,
                        colors = outlinedFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = discountValueStr,
                            onValueChange = { discountValueStr = it },
                            label = { Text("نسبة الخصم %", fontSize = 10.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f).height(48.dp)
                        )

                        OutlinedTextField(
                            value = minOrderStr,
                            onValueChange = { minOrderStr = it },
                            label = { Text("الحد الأدنى للطلب (ر.ي)", fontSize = 10.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.weight(1f).height(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val valD = discountValueStr.toDoubleOrNull() ?: 10.0
                            val minOrder = minOrderStr.toDoubleOrNull() ?: 5000.0
                            if (newCode.isNotBlank()) {
                                val coupon = CouponEntity(code = newCode.trim(), discountValue = valD, minOrderAmountYer = minOrder)
                                viewModel.addCoupon(coupon) {
                                    Toast.makeText(context, "تمت إضافة الكوبون $newCode بنجاح", Toast.LENGTH_SHORT).show()
                                    newCode = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Text(text = "حفظ وتفعيل الكوبون", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(allCoupons, key = { it.id }) { coupon ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "الكوبون: ${coupon.code}", color = GoldPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "الخصم: ${coupon.discountValue.toInt()}% • الحد الأدنى: ${coupon.minOrderAmountYer.toInt()} ر.ي", color = TextSecondary, fontSize = 11.sp)
                        Text(text = "عدد مرات الاستخدام: ${coupon.timesUsed}", color = TextMuted, fontSize = 10.sp)
                    }

                    IconButton(onClick = { viewModel.deleteCoupon(coupon) }) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = ErrorRed)
                    }
                }
            }
        }
    }
}

// 4.8 Activity Logs
@Composable
private fun AdminLogsView(viewModel: StoreViewModel) {
    val activityLogs by viewModel.activityLogs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "سجل عمليات ونشاطات النظام 📜", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        items(activityLogs, key = { it.id }) { log ->
            val dateStr = SimpleDateFormat("yyyy/MM/dd hh:mm:ss a", Locale.getDefault()).format(Date(log.timestamp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = log.actionType, color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = dateStr, color = TextMuted, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = log.details, color = TextPrimary, fontSize = 11.sp)
                    Text(text = "بواسطة: ${log.userName}", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}

// 4.9 Backup & Database Reset
@Composable
private fun AdminBackupView(viewModel: StoreViewModel) {
    val context = LocalContext.current
    var showConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, CardBorderGold, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "قاعدة البيانات والنسخ الاحتياطي 💾", color = GoldPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "يتم حفظ جميع البيانات محلياً وبشكل دائم عبر Room Database وفقاً لأفضل الممارسات البرمجية.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "تم أخذ لقطة نسخة احتياطية محلية لقاعدة البيانات بنجاح ✅", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldContainer, contentColor = TextGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
                ) {
                    Text(text = "تصدير نسخة احتياطية للبيانات", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showConfirm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "إعادة ضبط قاعدة البيانات للمصنع الافتراضي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            containerColor = CardDark,
            title = { Text(text = "تأكيد الإجراء", color = ErrorRed, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = { Text(text = "هل أنت متأكد من إعادة ضبط البيانات؟ سيتم استرجاع كافة المنتجات والأقسام والطلبات الأولية.", color = TextSecondary, fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetDemoDatabase {
                            showConfirm = false
                            Toast.makeText(context, "تمت إعادة ضبط قاعدة البيانات بنجاح", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text(text = "نعم، استعد الافتراضي")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(text = "إلغاء", color = TextMuted)
                }
            }
        )
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = ObsidianBlack,
    unfocusedContainerColor = ObsidianBlack,
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = CardBorderGold,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextMuted
)

// ==========================================
// 4.2 Payments & Verification Management View
// ==========================================
@Composable
private fun AdminPaymentsView(viewModel: StoreViewModel) {
    val context = LocalContext.current
    val allOrders by viewModel.allOrders.collectAsState()
    val allPaymentMethods by viewModel.allPaymentMethods.collectAsState()

    var activeSubTab by remember { mutableStateOf(0) } // 0: Review Proofs, 1: Manage Methods
    var selectedPaymentFilter by remember { mutableStateOf<PaymentStatus?>(null) }
    var selectedProofImageUri by remember { mutableStateOf<String?>(null) }

    // Rejection dialog state
    var rejectingOrder by remember { mutableStateOf<OrderEntity?>(null) }
    var rejectionReason by remember { mutableStateOf("المبلغ المحول غير مطابق لقيمة الطلب") }

    // Method edit/add dialog state
    var editingMethod by remember { mutableStateOf<PaymentMethodEntity?>(null) }
    var isAddingMethod by remember { mutableStateOf(false) }
    var editingCodGovernorates by remember { mutableStateOf<PaymentMethodEntity?>(null) }

    val pendingCount = allOrders.count { it.paymentStatus == PaymentStatus.VERIFICATION_PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Sub-Tab Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeSubTab == 0) GoldPrimary else CardDark)
                    .border(1.dp, if (activeSubTab == 0) GoldAccent else CardBorderGold, RoundedCornerShape(10.dp))
                    .clickable { activeSubTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "مراجعة إثباتات الدفع",
                        color = if (activeSubTab == 0) ObsidianBlack else TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (pendingCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(if (activeSubTab == 0) ObsidianBlack else ErrorRed, CircleShape)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$pendingCount",
                                color = if (activeSubTab == 0) GoldPrimary else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeSubTab == 1) GoldPrimary else CardDark)
                    .border(1.dp, if (activeSubTab == 1) GoldAccent else CardBorderGold, RoundedCornerShape(10.dp))
                    .clickable { activeSubTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "إعدادات طرق الدفع (${allPaymentMethods.size})",
                    color = if (activeSubTab == 1) ObsidianBlack else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ============================
        // SUB-TAB 0: Review Proofs
        // ============================
        if (activeSubTab == 0) {
            // Status Filters Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                item {
                    StatusFilterChip(
                        title = "الكل (${allOrders.size})",
                        isSelected = selectedPaymentFilter == null,
                        onClick = { selectedPaymentFilter = null }
                    )
                }
                item {
                    StatusFilterChip(
                        title = "بانتظار التحقق ⏳ ($pendingCount)",
                        isSelected = selectedPaymentFilter == PaymentStatus.VERIFICATION_PENDING,
                        onClick = { selectedPaymentFilter = PaymentStatus.VERIFICATION_PENDING }
                    )
                }
                item {
                    val count = allOrders.count { it.paymentStatus == PaymentStatus.VERIFIED }
                    StatusFilterChip(
                        title = "تم التحقق ✅ ($count)",
                        isSelected = selectedPaymentFilter == PaymentStatus.VERIFIED,
                        onClick = { selectedPaymentFilter = PaymentStatus.VERIFIED }
                    )
                }
                item {
                    val count = allOrders.count { it.paymentStatus == PaymentStatus.COD_PENDING }
                    StatusFilterChip(
                        title = "دفع عند الاستلام 🚚 ($count)",
                        isSelected = selectedPaymentFilter == PaymentStatus.COD_PENDING,
                        onClick = { selectedPaymentFilter = PaymentStatus.COD_PENDING }
                    )
                }
                item {
                    val count = allOrders.count { it.paymentStatus == PaymentStatus.REJECTED }
                    StatusFilterChip(
                        title = "مرفوض ❌ ($count)",
                        isSelected = selectedPaymentFilter == PaymentStatus.REJECTED,
                        onClick = { selectedPaymentFilter = PaymentStatus.REJECTED }
                    )
                }
            }

            val filteredOrders = allOrders.filter {
                selectedPaymentFilter == null || it.paymentStatus == selectedPaymentFilter
            }

            if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد طلبات تطابق هذا الفلتر",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 60.dp)
                ) {
                    items(filteredOrders, key = { it.id }) { order ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardDark),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (order.paymentStatus) {
                                    PaymentStatus.VERIFICATION_PENDING -> GoldPrimary
                                    PaymentStatus.VERIFIED -> StatusDelivered
                                    PaymentStatus.REJECTED -> ErrorRed
                                    PaymentStatus.COD_PENDING -> Color(0xFF38BDF8)
                                }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Header: Order # and Payment Status Badge
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = order.orderNumber,
                                            color = GoldPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${order.governorate} — ${order.city}",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }

                                    val (badgeBg, badgeText, badgeTitle) = when (order.paymentStatus) {
                                        PaymentStatus.VERIFIED -> Triple(StatusDelivered.copy(alpha = 0.2f), StatusDelivered, "تم التحقق ✅")
                                        PaymentStatus.VERIFICATION_PENDING -> Triple(GoldContainer, TextGold, "بانتظار التحقق ⏳")
                                        PaymentStatus.COD_PENDING -> Triple(Color(0xFF0369A1).copy(alpha = 0.2f), Color(0xFF38BDF8), "دفع عند الاستلام 🚚")
                                        PaymentStatus.REJECTED -> Triple(ErrorRed.copy(alpha = 0.2f), ErrorRed, "مرفوض ❌")
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(badgeBg, RoundedCornerShape(6.dp))
                                            .border(1.dp, badgeText.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = badgeTitle,
                                            color = badgeText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Divider(color = CardBorderGold.copy(alpha = 0.3f), thickness = 0.5.dp)

                                // Customer & Amount Info
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "العميل: ${order.customerName}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text(text = "الهاتف: ${order.customerPhone}", color = TextMuted, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "المبلغ الإجمالي:", color = TextMuted, fontSize = 10.sp)
                                        Text(
                                            text = "${order.totalYer.toInt()} ر.ي",
                                            color = GoldPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Payment Method Name & Details
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ObsidianBlack, RoundedCornerShape(8.dp))
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "طريقة الدفع: ${order.paymentMethodName}",
                                        color = TextGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (order.paymentTransactionNumber.isNotBlank()) {
                                        Text(
                                            text = "رقم العملية: ${order.paymentTransactionNumber}",
                                            color = TextPrimary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (order.paymentNotes.isNotBlank()) {
                                    Text(
                                        text = "ملاحظات الدفع: ${order.paymentNotes}",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                if (order.paymentStatus == PaymentStatus.REJECTED && order.paymentRejectionReason.isNotBlank()) {
                                    Text(
                                        text = "سبب الرفض: ${order.paymentRejectionReason}",
                                        color = ErrorRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Receipt Proof Image Preview (If available)
                                if (!order.paymentProofUri.isNullOrBlank()) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(130.dp)
                                            .clickable { selectedProofImageUri = order.paymentProofUri },
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            AsyncImage(
                                                model = order.paymentProofUri,
                                                contentDescription = "سند التحويل",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .fillMaxWidth()
                                                    .background(ObsidianBlack.copy(alpha = 0.75f))
                                                    .padding(vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "🔍 اضغط لتكبير وفحص إشعار التحويل",
                                                    color = GoldPrimary,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                // Admin Action Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Verify Button
                                    Button(
                                        onClick = {
                                            viewModel.verifyPayment(
                                                orderId = order.id,
                                                orderNumber = order.orderNumber,
                                                customerId = order.customerId,
                                                notes = "تم التحقق بواسطة الإدارة بنجاح",
                                                onDone = {
                                                    Toast.makeText(context, "تم تأكيد دفع الطلب ${order.orderNumber} بنجاح ✅", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusDelivered, contentColor = Color.White),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(vertical = 6.dp)
                                    ) {
                                        Text(text = "تأكيد الدفع ✅", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    // Reject Button
                                    OutlinedButton(
                                        onClick = { rejectingOrder = order },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(vertical = 6.dp)
                                    ) {
                                        Text(text = "رفض الإثبات ❌", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    // WhatsApp Button
                                    IconButton(
                                        onClick = {
                                            val msg = "مرحباً يا ${order.customerName}، نتواصل معك من المتجر الملكي بخصوص إثبات دفع طلبك ${order.orderNumber}."
                                            WhatsAppHelper.openWhatsApp(context, phone = order.customerPhone, message = msg)
                                        },
                                        modifier = Modifier
                                            .background(WhatsAppGreen, RoundedCornerShape(8.dp))
                                            .size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Chat, contentDescription = "واتساب", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // SUB-TAB 1: Manage Payment Methods & COD
        // ==========================================
        if (activeSubTab == 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "طرق الدفع المفعلة والمتاحة",
                    color = GoldPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { isAddingMethod = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إضافة طريقة دفع", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                items(allPaymentMethods, key = { it.id }) { method ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (method.isActive) CardDark else CardDark.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (method.isActive) CardBorderGold else CardBorderGold.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = method.name,
                                        color = if (method.isActive) GoldPrimary else TextMuted,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(GoldContainer, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = when (method.type) {
                                                PaymentType.COD -> "دفع عند الاستلام"
                                                PaymentType.BANK_TRANSFER -> "تحويل بنكي"
                                                PaymentType.E_WALLET -> "محفظة إلكترونية"
                                                PaymentType.REMITTANCE -> "حوالة صرافة"
                                            },
                                            color = TextGold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                // Active Switch
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (method.isActive) "مفعل" else "معطل",
                                        color = if (method.isActive) StatusDelivered else TextMuted,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Switch(
                                        checked = method.isActive,
                                        onCheckedChange = { viewModel.togglePaymentMethodActive(method.id, it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = GoldPrimary,
                                            checkedTrackColor = GoldContainer,
                                            uncheckedThumbColor = TextMuted,
                                            uncheckedTrackColor = CardDark
                                        )
                                    )
                                }
                            }

                            if (method.accountNumber.isNotBlank()) {
                                Text(
                                    text = "رقم الحساب / المحفظة: ${method.accountNumber} (${method.currency})",
                                    color = TextPrimary,
                                    fontSize = 12.sp
                                )
                            }

                            if (method.accountHolder.isNotBlank()) {
                                Text(
                                    text = "المستفيد: ${method.accountHolder}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            if (method.type == PaymentType.COD) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ObsidianBlack, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "المحافظات المسموح بها للدفع عند الاستلام:", color = TextMuted, fontSize = 10.sp)
                                        Text(
                                            text = method.allowedGovernorates.ifEmpty { "كافة المحافظات" },
                                            color = TextGold,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { editingCodGovernorates = method },
                                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("تعديل المحافظات", fontSize = 10.sp)
                                    }
                                }
                            }

                            if (method.instructions.isNotBlank()) {
                                Text(
                                    text = "التعليمات للعميل: ${method.instructions}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            // Edit & Delete Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { editingMethod = method }) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تعديل البيانات", color = GoldPrimary, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = {
                                    viewModel.deletePaymentMethod(method)
                                    Toast.makeText(context, "تم حذف طريقة الدفع", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("حذف", color = ErrorRed, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // DIALOGS
    // ==========================================

    // 1. Full Proof Image Dialog
    if (selectedProofImageUri != null) {
        AlertDialog(
            onDismissRequest = { selectedProofImageUri = null },
            containerColor = ObsidianBlack,
            title = {
                Text(
                    text = "إشعار وسند التحويل الملكي 📸",
                    color = GoldPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    AsyncImage(
                        model = selectedProofImageUri,
                        contentDescription = "سند التحويل بكامل الحجم",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedProofImageUri = null },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
                ) {
                    Text("إغلاق")
                }
            }
        )
    }

    // 2. Reject Payment Dialog
    if (rejectingOrder != null) {
        val order = rejectingOrder!!
        AlertDialog(
            onDismissRequest = { rejectingOrder = null },
            containerColor = CardDark,
            title = {
                Text(
                    text = "رفض إثبات الدفع للطلب ${order.orderNumber}",
                    color = ErrorRed,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "يرجى تحديد سبب الرفض لإشعار العميل وتسجيله في النظام:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = rejectionReason,
                        onValueChange = { rejectionReason = it },
                        label = { Text("سبب الرفض") },
                        colors = outlinedFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Quick presets
                    Text(text = "أسباب شائعة:", color = TextMuted, fontSize = 10.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .background(CardDark, RoundedCornerShape(4.dp))
                                .border(1.dp, CardBorderGold, RoundedCornerShape(4.dp))
                                .clickable { rejectionReason = "المبلغ المحول غير مطابق" }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text("المبلغ غير مطابق", color = TextGold, fontSize = 9.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(CardDark, RoundedCornerShape(4.dp))
                                .border(1.dp, CardBorderGold, RoundedCornerShape(4.dp))
                                .clickable { rejectionReason = "رقم العملية غير صحيح" }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text("رقم العملية خطأ", color = TextGold, fontSize = 9.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(CardDark, RoundedCornerShape(4.dp))
                                .border(1.dp, CardBorderGold, RoundedCornerShape(4.dp))
                                .clickable { rejectionReason = "الصورة غير واضحة" }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text("الصورة غير واضحة", color = TextGold, fontSize = 9.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rejectPayment(
                            orderId = order.id,
                            orderNumber = order.orderNumber,
                            customerId = order.customerId,
                            reason = rejectionReason,
                            onDone = {
                                rejectingOrder = null
                                Toast.makeText(context, "تم رفض إثبات الدفع وإشعار العميل", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("تأكيد الرفض ❌")
                }
            },
            dismissButton = {
                TextButton(onClick = { rejectingOrder = null }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }

    // 3. Edit / Add Payment Method Dialog
    if (isAddingMethod || editingMethod != null) {
        val isNew = isAddingMethod
        val currentMethod = editingMethod
        var name by remember { mutableStateOf(currentMethod?.name ?: "") }
        var accountNumber by remember { mutableStateOf(currentMethod?.accountNumber ?: "") }
        var accountHolder by remember { mutableStateOf(currentMethod?.accountHolder ?: "") }
        var currency by remember { mutableStateOf(currentMethod?.currency ?: "YER") }
        var instructions by remember { mutableStateOf(currentMethod?.instructions ?: "") }
        var selectedType by remember { mutableStateOf(currentMethod?.type ?: PaymentType.BANK_TRANSFER) }
        var allowedGovs by remember { mutableStateOf(currentMethod?.allowedGovernorates ?: "صنعاء,إب") }

        AlertDialog(
            onDismissRequest = {
                isAddingMethod = false
                editingMethod = null
            },
            containerColor = CardDark,
            title = {
                Text(
                    text = if (isNew) "إضافة طريقة دفع جديدة 💳" else "تعديل طريقة الدفع ✏️",
                    color = GoldPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("اسم طريقة الدفع (مثال: بنك الكريمي، محفظة جيب)") },
                            colors = outlinedFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Text(text = "نوع طريقة الدفع:", color = TextMuted, fontSize = 11.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            PaymentType.entries.forEach { type ->
                                val sel = selectedType == type
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (sel) GoldPrimary else CardDark)
                                        .border(1.dp, if (sel) GoldAccent else CardBorderGold, RoundedCornerShape(6.dp))
                                        .clickable { selectedType = type }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = when (type) {
                                            PaymentType.COD -> "عند الاستلام"
                                            PaymentType.BANK_TRANSFER -> "بنكي"
                                            PaymentType.E_WALLET -> "محفظة"
                                            PaymentType.REMITTANCE -> "صرافة"
                                        },
                                        color = if (sel) ObsidianBlack else TextPrimary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    if (selectedType != PaymentType.COD) {
                        item {
                            OutlinedTextField(
                                value = accountNumber,
                                onValueChange = { accountNumber = it },
                                label = { Text("رقم الحساب / المحفظة / الهاتف") },
                                colors = outlinedFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = accountHolder,
                                onValueChange = { accountHolder = it },
                                label = { Text("اسم صاحب الحساب أو المستفيد") },
                                colors = outlinedFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = currency,
                                onValueChange = { currency = it },
                                label = { Text("العملة (YER, SAR, ANY)") },
                                colors = outlinedFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        item {
                            OutlinedTextField(
                                value = allowedGovs,
                                onValueChange = { allowedGovs = it },
                                label = { Text("المحافظات المسموح بها (مفصولة بفواصل)") },
                                colors = outlinedFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("تعليمات تظهر للعميل عند اختيار هذه الطريقة") },
                            colors = outlinedFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            if (isNew) {
                                val newMethod = PaymentMethodEntity(
                                    name = name.trim(),
                                    accountNumber = accountNumber.trim(),
                                    accountHolder = accountHolder.trim(),
                                    currency = currency.trim().uppercase(),
                                    instructions = instructions.trim(),
                                    type = selectedType,
                                    allowedGovernorates = if (selectedType == PaymentType.COD) allowedGovs.trim() else "",
                                    isActive = true
                                )
                                viewModel.addPaymentMethod(newMethod) {
                                    isAddingMethod = false
                                    Toast.makeText(context, "تمت إضافة طريقة الدفع بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            } else if (currentMethod != null) {
                                val updated = currentMethod.copy(
                                    name = name.trim(),
                                    accountNumber = accountNumber.trim(),
                                    accountHolder = accountHolder.trim(),
                                    currency = currency.trim().uppercase(),
                                    instructions = instructions.trim(),
                                    type = selectedType,
                                    allowedGovernorates = if (selectedType == PaymentType.COD) allowedGovs.trim() else ""
                                )
                                viewModel.updatePaymentMethod(updated) {
                                    editingMethod = null
                                    Toast.makeText(context, "تم تحديث طريقة الدفع بنجاح", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
                ) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isAddingMethod = false
                    editingMethod = null
                }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }

    // 4. Edit COD Allowed Governorates Dialog
    if (editingCodGovernorates != null) {
        val method = editingCodGovernorates!!
        var govs by remember { mutableStateOf(method.allowedGovernorates) }

        AlertDialog(
            onDismissRequest = { editingCodGovernorates = null },
            containerColor = CardDark,
            title = {
                Text(
                    text = "محافظات الدفع عند الاستلام 🚚",
                    color = GoldPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "حدد المحافظات التي يسمح فيها بالدفع عند الاستلام، مفصولة بفواصل (مثال: صنعاء,إب):",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    OutlinedTextField(
                        value = govs,
                        onValueChange = { govs = it },
                        label = { Text("المحافظات المسموح بها") },
                        colors = outlinedFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateCodAllowedGovernorates(method.id, govs) {
                            editingCodGovernorates = null
                            Toast.makeText(context, "تم تحديث محافظات الدفع عند الاستلام بنجاح", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
                ) {
                    Text("حفظ التغييرات")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingCodGovernorates = null }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }
}

