package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import com.example.data.model.UserAddressEntity
import com.example.ui.components.RoyalAddressEditorDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper
import com.example.ui.viewmodel.MainNavTab
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun AccountScreen(
    viewModel: StoreViewModel,
    onViewOrders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val customerOrders by viewModel.customerOrders.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val unreadNotifs by viewModel.unreadNotificationsCount.collectAsState()
    val userAddresses by viewModel.userAddresses.collectAsState()

    var showRoleDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showAdminLoginDialog by remember { mutableStateOf(false) }
    var showAddressManagerDialog by remember { mutableStateOf(false) }
    var showAddressEditor by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<UserAddressEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 90.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, CardBorderGold, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(GoldContainer)
                                .border(1.5.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.name ?: "مستخدم المتجر",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "📱 ${currentUser?.phone ?: "777128378"}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Role Badge
                            val roleTitle = when (currentUser?.role) {
                                UserRole.SUPER_ADMIN -> "المدير العام 🛡️"
                                UserRole.ORDERS_STAFF -> "موظف الطلبات 📦"
                                UserRole.INVENTORY_STAFF -> "مسؤول المخزون 🏷️"
                                else -> "عميل المتجر 👤"
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldContainer)
                                    .border(0.5.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = roleTitle,
                                    color = TextGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showRoleDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ObsidianBlack, contentColor = TextGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .border(1.dp, CardBorderGold, RoundedCornerShape(10.dp))
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "تبديل الصلاحية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { showAdminLoginDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "دخول الإدارة (ziko)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Admin Panel Launch (if Admin or Staff)
        if (currentUser?.role != null && currentUser?.role != UserRole.CUSTOMER) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, GoldPrimary, RoundedCornerShape(14.dp))
                        .clickable { viewModel.enterAdminMode() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "لوحة الإدارة",
                                    tint = ObsidianBlack,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "لوحة تحكم المتجر الملكي",
                                    color = TextGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "إدارة المنتجات، الطلبات، المخزون، والعملاء",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Text(text = "دخول ⬅", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Shortcuts Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, CardBorderGold, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    AccountMenuRow(
                        icon = Icons.Default.ReceiptLong,
                        title = "سجل طلباتي",
                        subtitle = "${customerOrders.size} طلبات مسجلة",
                        onClick = onViewOrders
                    )

                    Divider(color = CardBorderGold.copy(alpha = 0.3f))

                    AccountMenuRow(
                        icon = Icons.Default.Favorite,
                        title = "قائمة المفضلة",
                        subtitle = "${favoriteIds.size} منتجات محفوظة",
                        onClick = { viewModel.setNavTab(MainNavTab.FAVORITES) }
                    )

                    Divider(color = CardBorderGold.copy(alpha = 0.3f))

                    AccountMenuRow(
                        icon = Icons.Default.LocationOn,
                        title = "عناويني ومواقع التوصيل (Google Maps)",
                        subtitle = "${userAddresses.size} عناوين محفوظة 📍",
                        iconTint = GoldPrimary,
                        onClick = { showAddressManagerDialog = true }
                    )

                    Divider(color = CardBorderGold.copy(alpha = 0.3f))

                    AccountMenuRow(
                        icon = Icons.Default.Chat,
                        title = "تواصل مع خدمة العملاء (واتساب)",
                        subtitle = "الرقم المباشر: 777128378",
                        iconTint = WhatsAppGreen,
                        onClick = {
                            WhatsAppHelper.openWhatsAppChat(context, message = "السلام عليكم، أحتاج مساعدة بخصوص المتجر الملكي")
                        }
                    )
                }
            }
        }

        // About Store & Slogan Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, CardBorderGold, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_almalaki_logo),
                        contentDescription = "شعار المتجر",
                        modifier = Modifier.size(54.dp).clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "👑 المتجر الملكي | ALMALAKI STORE",
                        color = GoldPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "الشعار: كل ما تحتاجه في مكان واحد",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "التطبيق مخصص للبيع الإلكتروني الموثوق داخل اليمن مع شحن سريع وتأكيد فوري عبر واتساب.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Reset Demo Database Action
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showResetDialog = true }
                    .border(1.dp, CardBorderGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "إعادة ضبط البيانات التجريبية", color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "إعادة المنتجات والطلبات والمستخدمين إلى الإعدادات الافتراضية", color = TextMuted, fontSize = 10.sp)
                    }
                }
            }
        }
    }

    // Role Selection Dialog
    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            containerColor = CardDark,
            title = {
                Text(text = "اختر دور المستخدم للتجربة", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    allUsers.forEach { user ->
                        val roleDesc = when (user.role) {
                            UserRole.SUPER_ADMIN -> "👑 المدير العام (كامل الصلاحيات)"
                            UserRole.ADMIN -> "🛡️ مدير المتجر"
                            UserRole.ORDERS_STAFF -> "📦 مسؤول الطلبات والشحن"
                            UserRole.INVENTORY_STAFF -> "🏷️ مسؤول المخزون والمنتجات"
                            UserRole.CUSTOMER -> "👤 عميل المتجر العادي"
                        }
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (currentUser?.id == user.id) GoldContainer else ObsidianBlack
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.setCurrentUser(user)
                                    showRoleDialog = false
                                    Toast.makeText(context, "تم التحويل إلى: ${user.name}", Toast.LENGTH_SHORT).show()
                                }
                                .border(1.dp, CardBorderGold, RoundedCornerShape(10.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = user.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = roleDesc, color = TextGold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleDialog = false }) {
                    Text(text = "إغلاق", color = GoldPrimary)
                }
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = CardDark,
            title = {
                Text(text = "تأكيد إعادة الضبط", color = ErrorRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "هل تريد إعادة تعيين المتجر إلى بياناته الافتراضية؟ سيتم تحديث المنتجات والأقسام والطلبات.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetDemoDatabase {
                            showResetDialog = false
                            Toast.makeText(context, "تمت إعادة ضبط البيانات بنجاح", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text(text = "نعم، إعادة الضبط")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(text = "إلغاء", color = TextMuted)
                }
            }
        )
    }

    // Admin Login Dialog (for ziko / ziko)
    if (showAdminLoginDialog) {
        var adminUsername by remember { mutableStateOf("ziko") }
        var adminPassword by remember { mutableStateOf("ziko") }
        var loginError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAdminLoginDialog = false },
            containerColor = CardDark,
            title = {
                Text(
                    text = "تسجيل دخول إدارة المتجر الملكي 👑",
                    color = GoldPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "أدخل بيانات حساب المدير للوصول إلى لوحة التحكم وإدارة المدفوعات والطلبات:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = adminUsername,
                        onValueChange = {
                            adminUsername = it
                            loginError = false
                        },
                        label = { Text("اسم المستخدم (مثال: ziko)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianBlack,
                            unfocusedContainerColor = ObsidianBlack,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = CardBorderGold,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = adminPassword,
                        onValueChange = {
                            adminPassword = it
                            loginError = false
                        },
                        label = { Text("كلمة المرور") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianBlack,
                            unfocusedContainerColor = ObsidianBlack,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = CardBorderGold,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (loginError) {
                        Text(
                            text = "اسم المستخدم أو كلمة المرور غير صحيحة!",
                            color = ErrorRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "💡 حساب المدير العام الافتراضي: اسم المستخدم: ziko | كلمة المرور: ziko",
                        color = TextGold,
                        fontSize = 10.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.loginAdmin(adminUsername.trim(), adminPassword.trim()) { success ->
                            if (success) {
                                showAdminLoginDialog = false
                                Toast.makeText(context, "تم تسجيل الدخول بنجاح كـ ${adminUsername}", Toast.LENGTH_SHORT).show()
                            } else {
                                loginError = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack)
                ) {
                    Text("تسجيل الدخول", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminLoginDialog = false }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }

    // 4. Saved Addresses Management Dialog
    if (showAddressManagerDialog) {
        AlertDialog(
            onDismissRequest = { showAddressManagerDialog = false },
            containerColor = CardDark,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "عناويني ومواقع التوصيل",
                        color = TextGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "يمكنك حفظ عدة عناوين وتحديد موقع كل منزل أو متجر بدقة عبر Google Maps لتسهيل وصول المندوب.",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Button(
                        onClick = {
                            editingAddress = null
                            showAddressEditor = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "➕ إضافة موقع / عنوان جديد", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    if (userAddresses.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لا توجد عناوين محفوظة حالياً 📍",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            items(userAddresses, key = { it.id }) { addr ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (addr.isDefault) GoldPrimary else CardBorderGold.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = "${addr.type.iconEmoji} ${addr.title}", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                if (addr.isDefault) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(text = "(افتراضي ⭐)", color = TextGold, fontSize = 10.sp)
                                                }
                                            }

                                            Row {
                                                IconButton(
                                                    onClick = {
                                                        editingAddress = addr
                                                        showAddressEditor = true
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                                }
                                                IconButton(
                                                    onClick = {
                                                        viewModel.deleteAddress(addr)
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(Icons.Default.Delete, contentDescription = "حذف", tint = ErrorRed, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }

                                        Text(text = "المستلم: ${addr.recipientName} (${addr.recipientPhone})", color = TextPrimary, fontSize = 11.sp)
                                        Text(text = "الموقع: ${addr.governorate} — ${addr.city} — ${addr.addressDetails}", color = TextMuted, fontSize = 11.sp)

                                        if (addr.latitude != null && addr.longitude != null) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(GoldContainer.copy(alpha = 0.3f))
                                                    .clickable {
                                                        com.example.util.LocationAndGeocodingHelper.openInGoogleMaps(context, addr.latitude, addr.longitude)
                                                    }
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = "عرض على Google Maps ↗", color = GoldPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddressManagerDialog = false }) {
                    Text("إغلاق", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 5. Address Editor Dialog
    if (showAddressEditor) {
        RoyalAddressEditorDialog(
            initialAddress = editingAddress,
            userId = currentUser?.id ?: 1L,
            defaultName = currentUser?.name ?: "",
            defaultPhone = currentUser?.phone ?: "",
            onDismiss = {
                showAddressEditor = false
                editingAddress = null
            },
            onSaveAddress = { newOrUpdated ->
                if (newOrUpdated.id == 0L) {
                    viewModel.saveAddress(newOrUpdated)
                } else {
                    viewModel.updateAddress(newOrUpdated)
                }
                showAddressEditor = false
                editingAddress = null
            }
        )
    }
}

@Composable
private fun AccountMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: Color = GoldPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ObsidianBlack),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
            }
        }

        Text(text = "‹", color = TextMuted, fontSize = 18.sp)
    }
}
