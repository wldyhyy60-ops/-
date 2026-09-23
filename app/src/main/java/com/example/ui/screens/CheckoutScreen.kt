package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Signpost
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DiscountType
import com.example.data.model.OrderWithItems
import com.example.data.model.PaymentMethodEntity
import com.example.data.model.PaymentType
import com.example.data.model.UserAddressEntity
import com.example.ui.components.RoyalAddressEditorDialog
import com.example.ui.components.RoyalMapLocationPicker
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.StoreViewModel
import com.example.util.LocationAndGeocodingHelper
import com.example.util.WhatsAppHelper

@Composable
fun CheckoutScreen(
    viewModel: StoreViewModel,
    onBack: () -> Unit,
    onOrderSuccess: (OrderWithItems) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartItems by viewModel.userCartItems.collectAsState()
    val deliveryZones by viewModel.deliveryZones.collectAsState()
    val selectedZone by viewModel.selectedDeliveryZone.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val activePaymentMethods by viewModel.activePaymentMethods.collectAsState()

    val userAddresses by viewModel.userAddresses.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    var customerName by remember { mutableStateOf(selectedAddress?.recipientName ?: currentUser?.name ?: "") }
    var customerPhone by remember { mutableStateOf(selectedAddress?.recipientPhone ?: currentUser?.phone ?: "") }
    var city by remember { mutableStateOf(selectedAddress?.city ?: currentUser?.city ?: "") }
    var district by remember { mutableStateOf(selectedAddress?.district ?: "") }
    var street by remember { mutableStateOf(selectedAddress?.street ?: "") }
    var nearestLandmark by remember { mutableStateOf(selectedAddress?.nearestLandmark ?: "") }
    var addressDetails by remember { mutableStateOf(selectedAddress?.addressDetails ?: currentUser?.addressDetails ?: "") }
    var latitude by remember { mutableStateOf<Double?>(selectedAddress?.latitude) }
    var longitude by remember { mutableStateOf<Double?>(selectedAddress?.longitude) }
    var googleMapsUrl by remember { mutableStateOf(selectedAddress?.googleMapsUrl ?: "") }
    var notes by remember { mutableStateOf("") }
    var showZoneDropdown by remember { mutableStateOf(false) }
    var saveToAddresses by remember { mutableStateOf(true) }
    var isAddressConfirmed by remember { mutableStateOf(selectedAddress != null) }

    var showMapPicker by remember { mutableStateOf(false) }
    var showAddressEditor by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<UserAddressEntity?>(null) }
    var showSavedAddressesList by remember { mutableStateOf(false) }

    LaunchedEffect(selectedAddress) {
        selectedAddress?.let { addr ->
            customerName = addr.recipientName
            customerPhone = addr.recipientPhone
            city = addr.city
            district = addr.district
            street = addr.street
            nearestLandmark = addr.nearestLandmark
            addressDetails = addr.addressDetails
            latitude = addr.latitude
            longitude = addr.longitude
            googleMapsUrl = addr.getEffectiveMapsUrl()
            isAddressConfirmed = true
        }
    }

    // Payment state
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethodEntity?>(null) }
    var paymentProofUriString by remember { mutableStateOf<String?>(null) }
    var paymentTxNumber by remember { mutableStateOf("") }
    var paymentNotes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Photo picker for receipt proof
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                paymentProofUriString = uri.toString()
                validationError = null
            }
        }
    )

    val currentGov = selectedZone?.governorateName ?: "صنعاء"
    val availableMethods = remember(currentGov, activePaymentMethods) {
        viewModel.getAvailablePaymentMethods(currentGov, activePaymentMethods)
    }

    // If selected payment method is COD but no longer available in this governorate, reset it
    LaunchedEffect(availableMethods) {
        if (selectedPaymentMethod != null && !availableMethods.any { it.id == selectedPaymentMethod!!.id }) {
            selectedPaymentMethod = null
            paymentProofUriString = null
        }
    }

    val subtotalYer = cartItems.sumOf { it.totalYer }
    val deliveryFeeYer = if (selectedZone != null) {
        if (subtotalYer >= selectedZone!!.freeDeliveryThresholdYer) 0.0 else selectedZone!!.deliveryFeeYer
    } else 3000.0

    var discountAmountYer = 0.0
    if (appliedCoupon != null && subtotalYer >= appliedCoupon!!.minOrderAmountYer) {
        discountAmountYer = if (appliedCoupon!!.discountType == DiscountType.PERCENT) {
            subtotalYer * (appliedCoupon!!.discountValue / 100.0)
        } else {
            appliedCoupon!!.discountValue
        }
    }

    val totalYer = (subtotalYer + deliveryFeeYer - discountAmountYer).coerceAtLeast(0.0)

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "تم نسخ $label بنجاح 📋", Toast.LENGTH_SHORT).show()
    }

    // Google Maps Location Picker Dialog
    if (showMapPicker) {
        RoyalMapLocationPicker(
            initialLatitude = latitude,
            initialLongitude = longitude,
            onDismiss = { showMapPicker = false },
            onLocationSelected = { geocoded ->
                latitude = geocoded.latitude
                longitude = geocoded.longitude
                googleMapsUrl = geocoded.googleMapsUrl
                if (geocoded.governorate.isNotBlank()) {
                    val matchedZone = deliveryZones.firstOrNull { it.governorateName.trim() == geocoded.governorate.trim() }
                    if (matchedZone != null) {
                        viewModel.selectDeliveryZone(matchedZone)
                    }
                }
                if (geocoded.city.isNotBlank()) {
                    city = geocoded.city
                }
                if (geocoded.district.isNotBlank()) {
                    district = geocoded.district
                }
                if (geocoded.street.isNotBlank()) {
                    street = geocoded.street
                }
                if (addressDetails.isBlank() && geocoded.fullFormattedAddress.isNotBlank()) {
                    addressDetails = geocoded.fullFormattedAddress
                }
                isAddressConfirmed = true
                showMapPicker = false
                Toast.makeText(context, "تم تحديد الموقع واستخراج العنوان بنجاح 📍", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Address Editor Dialog
    if (showAddressEditor) {
        RoyalAddressEditorDialog(
            initialAddress = editingAddress,
            userId = currentUser?.id ?: 1L,
            defaultName = customerName,
            defaultPhone = customerPhone,
            onDismiss = { showAddressEditor = false; editingAddress = null },
            onSaveAddress = { newOrUpdated ->
                if (newOrUpdated.id == 0L) {
                    viewModel.saveAddress(newOrUpdated)
                } else {
                    viewModel.updateAddress(newOrUpdated)
                }
                viewModel.selectAddress(newOrUpdated)
                showAddressEditor = false
                editingAddress = null
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = GoldPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "إتمام الطلب الملكي",
                        color = GoldPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "حدد موقع التوصيل بدقة واختر طريقة الدفع الملكية",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // 2. Saved Address Card (If user has saved addresses)
        if (userAddresses.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isAddressConfirmed) StatusDelivered else CardBorderGold),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bookmark, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "📍 عنوان التوصيل المحفوظ",
                                    color = TextGold,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (isAddressConfirmed) {
                                Surface(
                                    color = StatusDelivered.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusDelivered)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusDelivered, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("معتمد للطلب", color = StatusDelivered, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Summary of address
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GoldContainer.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "👤 المستلم: $customerName", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(text = "📱 الهاتف: $customerPhone", color = TextPrimary, fontSize = 13.sp)
                            Text(text = "📍 المحافظة والمدينة: $currentGov — $city", color = TextPrimary, fontSize = 13.sp)
                            val detailedParts = listOf(district, street, nearestLandmark, addressDetails).filter { it.isNotBlank() }
                            if (detailedParts.isNotEmpty()) {
                                Text(text = "🏠 العنوان: ${detailedParts.joinToString("، ")}", color = TextMuted, fontSize = 12.sp)
                            }

                            if (latitude != null && longitude != null) {
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "🗺️ الموقع محدد على الخريطة (${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)})",
                                        color = GoldPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Action Buttons: Change, Confirm, Add
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showSavedAddressesList = !showSavedAddressesList },
                                modifier = Modifier.weight(1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (showSavedAddressesList) "إخفاء القائمة" else "تغيير العنوان", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    isAddressConfirmed = true
                                    Toast.makeText(context, "تم اعتماد هذا العنوان للتوصيل ✅", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1.2f),
                                colors = ButtonDefaults.buttonColors(containerColor = if (isAddressConfirmed) StatusDelivered else GoldPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAddressConfirmed) "العنوان معتمد ✅" else "تأكيد هذا العنوان",
                                    color = ObsidianBlack,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    editingAddress = null
                                    showAddressEditor = true
                                },
                                modifier = Modifier
                                    .background(GoldContainer.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .size(38.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "إضافة عنوان جديد", tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            }
                        }

                        // Expanded Saved Addresses List
                        if (showSavedAddressesList) {
                            Divider(color = CardBorderGold.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
                            Text(text = "اختر من عناوينك المحفوظة:", color = TextGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            userAddresses.forEach { addr ->
                                val isCur = selectedAddress?.id == addr.id
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectAddress(addr)
                                            showSavedAddressesList = false
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isCur) GoldContainer.copy(alpha = 0.4f) else CardDark
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isCur) GoldPrimary else CardBorderGold.copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = "${addr.type.iconEmoji} ${addr.title}", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                if (addr.isDefault) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(text = "(افتراضي ⭐)", color = TextGold, fontSize = 10.sp)
                                                }
                                            }
                                            Text(text = "${addr.governorate} — ${addr.city} — ${addr.addressDetails}", color = TextMuted, fontSize = 11.sp, maxLines = 1)
                                        }

                                        Row {
                                            IconButton(onClick = {
                                                editingAddress = addr
                                                showAddressEditor = true
                                            }) {
                                                Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Google Maps Location Section (Big Prominent Button & Preview)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📍 موقع التوصيل عبر Google Maps",
                                color = TextGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (latitude != null) {
                            Surface(
                                color = StatusDelivered.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "تم التحديد 🎯",
                                    color = StatusDelivered,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "حدد موقع منزلك أو متجرك بدقة على خريطة Google Maps لتسريع وصول مندوب التوصيل بدون اتصال.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Big Action Button: "📍 تحديد موقعي على الخريطة"
                    Button(
                        onClick = { showMapPicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditLocation,
                            contentDescription = null,
                            tint = ObsidianBlack,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (latitude != null) "📍 تعديل موقعي على الخريطة" else "📍 تحديد موقعي على الخريطة",
                            color = ObsidianBlack,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Display Extracted / Pinpointed Location Card if available
                    if (latitude != null && longitude != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GoldContainer.copy(alpha = 0.35f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(10.dp),
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
                                    Text(
                                        text = "📍 الموقع المحدد بدقة:",
                                        color = GoldPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .clickable {
                                                LocationAndGeocodingHelper.openInGoogleMaps(context, latitude!!, longitude!!)
                                            }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.OpenInNew, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "فتح في Google Maps", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Text(
                                    text = "الجمهورية اليمنية، $currentGov — $city",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "الإحداثيات: ${String.format("%.5f", latitude)}, ${String.format("%.5f", longitude)}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )

                                val mapsLink = googleMapsUrl.ifBlank { "https://www.google.com/maps?q=$latitude,$longitude" }
                                Text(
                                    text = mapsLink,
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Customer Delivery Information & Manual Writing Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تفاصيل العنوان وكتابة العنوان يدوياً",
                            color = TextGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Full Name
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it; validationError = null },
                        label = { Text("الاسم الثلاثي أو الرباعي") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it; validationError = null },
                        label = { Text("رقم الهاتف (واتساب للتأكيد)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // Governorate Selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedZone?.governorateName ?: "صنعاء",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("المحافظة") },
                            trailingIcon = {
                                IconButton(onClick = { showZoneDropdown = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "قائمة المحافظات", tint = GoldPrimary)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showZoneDropdown = true },
                            colors = royalTextFieldColors()
                        )
                        DropdownMenu(
                            expanded = showZoneDropdown,
                            onDismissRequest = { showZoneDropdown = false },
                            modifier = Modifier.background(CardDark)
                        ) {
                            deliveryZones.forEach { zone ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = zone.governorateName, color = TextPrimary)
                                            Text(
                                                text = "${zone.deliveryFeeYer.toInt()} ر.ي",
                                                color = GoldPrimary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.selectDeliveryZone(zone)
                                        showZoneDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // City / Area
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it; validationError = null },
                        label = { Text("المدينة / المنطقة") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = GoldPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // District / Neighborhood
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("الحي (اختياري)") },
                        placeholder = { Text("مثال: حي الجامعة، حارة المعاين", color = TextMuted.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // Street
                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("الشارع (اختياري)") },
                        placeholder = { Text("مثال: شارع تعز، شارع الثلاثين", color = TextMuted.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // Nearest Landmark
                    OutlinedTextField(
                        value = nearestLandmark,
                        onValueChange = { nearestLandmark = it },
                        label = { Text("أقرب معلم (اختياري ولكن يفضل كتابته)") },
                        placeholder = { Text("مثال: أمام مستشفى الثورة، بجانب سوبرماركت...", color = TextMuted.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        singleLine = true
                    )

                    // Detailed Address
                    OutlinedTextField(
                        value = addressDetails,
                        onValueChange = { addressDetails = it; validationError = null },
                        label = { Text("العنوان بالتفصيل (رقم المنزل، الطابق، تفاصيل إضافية)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors(),
                        maxLines = 3
                    )

                    // Order Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("ملاحظات خاصة بالتوصيل (اختياري)") },
                        leadingIcon = { Icon(Icons.Default.Note, contentDescription = null, tint = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = royalTextFieldColors()
                    )

                    // Checkbox to save address for future orders
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { saveToAddresses = !saveToAddresses }
                    ) {
                        Checkbox(
                            checked = saveToAddresses,
                            onCheckedChange = { saveToAddresses = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "حفظ هذا العنوان في حسابي للطلبات القادمة 💾",
                            color = if (saveToAddresses) TextGold else Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (saveToAddresses) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    // Delivery fee calculation notice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GoldContainer.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (deliveryFeeYer == 0.0) {
                                "تهانينا! طلبك مؤهل للتوصيل المجاني داخل $currentGov 🎉"
                            } else {
                                "رسوم الشحن والتوصيل لمحافظة $currentGov: ${deliveryFeeYer.toInt()} ريال يمني"
                            },
                            color = TextGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 3. 💳 اختر طريقة الدفع (Dynamic Payment Method Selector)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💳 اختر طريقة الدفع",
                                color = GoldPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Governorate indicator badge
                        Box(
                            modifier = Modifier
                                .background(GoldContainer, RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "موقعك: $currentGov", color = TextGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Text(
                        text = "تتغير خيارات الدفع المتاحة تلقائياً بحسب المحافظة المحددة.",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    // COD Notice if not available in selected governorate
                    val hasCod = availableMethods.any { it.type == PaymentType.COD }
                    if (!hasCod) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF2D1E12), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF854D0E), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFACC15), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الدفع عند الاستلام متاح فقط لمحافظات محددة (صنعاء، إب). لمحافظة $currentGov يرجى اختيار التحويل الإلكتروني أو الصرافة.",
                                color = Color(0xFFFEF08A),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Radio list of available payment methods
                    availableMethods.forEach { method ->
                        val isSelected = selectedPaymentMethod?.id == method.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) GoldContainer.copy(alpha = 0.7f) else ObsidianBlack
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) GoldPrimary else CardBorderGold.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPaymentMethod = method
                                    validationError = null
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        selectedPaymentMethod = method
                                        validationError = null
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = GoldPrimary,
                                        unselectedColor = TextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when (method.type) {
                                                PaymentType.COD -> "🚚 ${method.name}"
                                                PaymentType.BANK_TRANSFER -> "🏦 ${method.name}"
                                                PaymentType.E_WALLET -> "📱 ${method.name}"
                                                PaymentType.REMITTANCE -> "💸 ${method.name}"
                                            },
                                            color = if (isSelected) GoldPrimary else TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        if (method.type == PaymentType.COD) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(StatusDelivered.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("متاح", color = StatusDelivered, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    if (method.instructions.isNotBlank()) {
                                        Text(
                                            text = method.instructions,
                                            color = TextMuted,
                                            fontSize = 11.sp,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Details & Transfer Proof Upload (If Electronic/Remittance Method is Selected)
        if (selectedPaymentMethod != null && selectedPaymentMethod!!.type != PaymentType.COD) {
            val method = selectedPaymentMethod!!
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Title
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "بيانات التحويل إلى: ${method.name}",
                                color = GoldPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Account Info Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (method.accountNumber.isNotBlank()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (method.type == PaymentType.REMITTANCE) "رقم الحوالة / الهاتف:" else "رقم الحساب / المحفظة:",
                                                color = TextMuted,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = method.accountNumber,
                                                color = TextGold,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { copyToClipboard(method.accountNumber, "رقم الحساب") },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("نسخ", fontSize = 12.sp)
                                        }
                                    }
                                }

                                if (method.accountHolder.isNotBlank()) {
                                    Divider(color = CardBorderGold.copy(alpha = 0.3f), thickness = 0.5.dp)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = "اسم المستفيد المعتمد:", color = TextMuted, fontSize = 11.sp)
                                            Text(
                                                text = method.accountHolder,
                                                color = TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { copyToClipboard(method.accountHolder, "اسم المستفيد") },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "نسخ الاسم", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("نسخ", fontSize = 12.sp)
                                        }
                                    }
                                }

                                if (method.currency != "ANY") {
                                    Divider(color = CardBorderGold.copy(alpha = 0.3f), thickness = 0.5.dp)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "العملة المطلوبة:", color = TextMuted, fontSize = 11.sp)
                                        Text(
                                            text = if (method.currency == "SAR") "ريال سعودي (SAR)" else "ريال يمني (YER)",
                                            color = GoldPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // 📤 إرفاق إثبات الدفع (Mandatory Receipt Upload Section)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "📤 إرفاق إثبات الدفع (مطلوب)",
                                    color = TextGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "يرجى تحويل المبلغ ثم إرفاق صورة السند أو إشعار التحويل لتأكيد الطلب.",
                                color = TextMuted,
                                fontSize = 11.sp
                            )

                            // Upload Container / Preview
                            if (paymentProofUriString != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusDelivered)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        AsyncImage(
                                            model = paymentProofUriString,
                                            contentDescription = "إثبات الدفع",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        // Badge
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(8.dp)
                                                .background(StatusDelivered, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("تم إرفاق الإثبات", color = ObsidianBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        // Change / Remove button
                                        IconButton(
                                            onClick = { paymentProofUriString = null },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(8.dp)
                                                .background(ObsidianBlack.copy(alpha = 0.7f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "حذف الصورة", tint = Color.White)
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .border(
                                            width = 1.5.dp,
                                            color = if (validationError != null) ErrorRed else GoldPrimary,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .background(GoldContainer.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .clickable {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "رفع صورة إثبات الدفع",
                                            tint = GoldPrimary,
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "انقر هنا لاختيار صورة إشعار أو سند التحويل 📸",
                                            color = TextGold,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "صورة واضحة توضح رقم العملية والمبلغ وتاريخ التحويل",
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            // Transaction Number
                            OutlinedTextField(
                                value = paymentTxNumber,
                                onValueChange = { paymentTxNumber = it; validationError = null },
                                label = { Text("رقم العملية / رقم الحوالة إن وجد") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = royalTextFieldColors(),
                                singleLine = true
                            )

                            // Payment Notes
                            OutlinedTextField(
                                value = paymentNotes,
                                onValueChange = { paymentNotes = it },
                                label = { Text("ملاحظات الدفع (مثلاً: اسم المحوّل)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = royalTextFieldColors()
                            )
                        }
                    }
                }
            }
        }

        // 5. COD Explanatory Card (If COD is chosen)
        if (selectedPaymentMethod != null && selectedPaymentMethod!!.type == PaymentType.COD) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusDelivered.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusDelivered, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "الدفع نقداً عند الاستلام",
                                color = StatusDelivered,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "لا يتطلب رفع إثبات دفع الآن. سيتم تسجيل طلبك بحالة 'بانتظار التحصيل'، وسيقوم مندوب التوصيل باستلام المبلغ نقداً عند تسليمك الشحنة.",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 6. Order Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGold),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "ملخص الحساب الملكي",
                        color = GoldPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "مجموع المنتجات (${cartItems.size} منتجات):", color = TextSecondary, fontSize = 13.sp)
                        Text(text = "${subtotalYer.toInt()} ر.ي", color = TextPrimary, fontSize = 13.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "رسوم التوصيل لمحافظة $currentGov:", color = TextSecondary, fontSize = 13.sp)
                        Text(
                            text = if (deliveryFeeYer == 0.0) "مجاني" else "${deliveryFeeYer.toInt()} ر.ي",
                            color = if (deliveryFeeYer == 0.0) StatusDelivered else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = if (deliveryFeeYer == 0.0) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    if (discountAmountYer > 0.0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "الخصم بالكوبون (${appliedCoupon?.code}):", color = StatusDelivered, fontSize = 13.sp)
                            Text(text = "-${discountAmountYer.toInt()} ر.ي", color = StatusDelivered, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Divider(color = CardBorderGold.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "المبلغ الإجمالي النهائي:",
                            color = GoldPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${totalYer.toInt()} ريال يمني",
                            color = GoldPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // 7. Error Banner (if any)
        if (validationError != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ErrorRed.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, ErrorRed, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = validationError!!,
                        color = ErrorRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 8. Confirm Order Button
        item {
            Button(
                onClick = {
                    // Validations
                    if (customerName.isBlank()) {
                        validationError = "يرجى كتابة اسم العميل لإكمال الطلب"
                        return@Button
                    }
                    if (customerPhone.isBlank() || customerPhone.length < 9) {
                        validationError = "يرجى إدخال رقم هاتف صحيح (9 أرقام على الأقل)"
                        return@Button
                    }
                    if (city.isBlank() || addressDetails.isBlank()) {
                        validationError = "يرجى تحديد الحي والعنوان بالتفصيل لضمان دقة التوصيل"
                        return@Button
                    }
                    if (selectedPaymentMethod == null) {
                        validationError = "يرجى اختيار طريقة دفع لإكمال الطلب."
                        return@Button
                    }

                    // Strict electronic payment validation
                    if (selectedPaymentMethod!!.type != PaymentType.COD) {
                        if (paymentProofUriString.isNullOrBlank() && paymentTxNumber.isBlank()) {
                            validationError = "يرجى إرفاق إثبات الدفع لإكمال الطلب."
                            Toast.makeText(context, "يرجى إرفاق إثبات الدفع لإكمال الطلب.", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                    }

                    isSubmitting = true
                    viewModel.placeOrder(
                        customerName = customerName,
                        customerPhone = customerPhone,
                        governorate = currentGov,
                        city = city,
                        district = district,
                        street = street,
                        nearestLandmark = nearestLandmark,
                        addressDetails = addressDetails,
                        latitude = latitude,
                        longitude = longitude,
                        googleMapsUrl = googleMapsUrl,
                        saveToUserAddresses = saveToAddresses,
                        saveAsDefaultAddress = isAddressConfirmed,
                        notes = notes,
                        paymentMethod = selectedPaymentMethod!!,
                        paymentProofUri = paymentProofUriString,
                        paymentTransactionNumber = paymentTxNumber,
                        paymentNotes = paymentNotes,
                        onSuccess = { orderWithItems ->
                            isSubmitting = false
                            // Open official WhatsApp with the formatted template to store number 777128378
                            WhatsAppHelper.openWhatsAppForOrder(context, orderWithItems.order, orderWithItems.items)
                            onOrderSuccess(orderWithItems)
                        },
                        onError = { err ->
                            isSubmitting = false
                            validationError = err
                        }
                    )
                },
                enabled = !isSubmitting && cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        tint = ObsidianBlack,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSubmitting) "جاري تسجيل الطلب..." else "تأكيد الطلب وإرساله لواتساب المتجر 👑",
                        color = ObsidianBlack,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun royalTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = CardBorderGold,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextMuted,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = ObsidianBlack,
    unfocusedContainerColor = ObsidianBlack
)
