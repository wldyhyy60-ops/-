package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AddressType
import com.example.data.model.UserAddressEntity
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.util.LocationAndGeocodingHelper

@Composable
fun RoyalAddressEditorDialog(
    initialAddress: UserAddressEntity? = null,
    userId: Long = 1L,
    defaultName: String = "",
    defaultPhone: String = "",
    onDismiss: () -> Unit,
    onSaveAddress: (UserAddressEntity) -> Unit
) {
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf(initialAddress?.type ?: AddressType.HOME) }
    var title by remember { mutableStateOf(initialAddress?.title ?: selectedType.titleAr) }
    var recipientName by remember { mutableStateOf(initialAddress?.recipientName ?: defaultName) }
    var recipientPhone by remember { mutableStateOf(initialAddress?.recipientPhone ?: defaultPhone) }
    var governorate by remember { mutableStateOf(initialAddress?.governorate ?: "صنعاء") }
    var city by remember { mutableStateOf(initialAddress?.city ?: "صنعاء") }
    var district by remember { mutableStateOf(initialAddress?.district ?: "") }
    var street by remember { mutableStateOf(initialAddress?.street ?: "") }
    var nearestLandmark by remember { mutableStateOf(initialAddress?.nearestLandmark ?: "") }
    var addressDetails by remember { mutableStateOf(initialAddress?.addressDetails ?: "") }
    var latitude by remember { mutableStateOf(initialAddress?.latitude) }
    var longitude by remember { mutableStateOf(initialAddress?.longitude) }
    var googleMapsUrl by remember { mutableStateOf(initialAddress?.googleMapsUrl ?: "") }
    var isDefault by remember { mutableStateOf(initialAddress?.isDefault ?: false) }

    var showGovDropdown by remember { mutableStateOf(false) }
    var showMapPicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                    governorate = geocoded.governorate
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
                showMapPicker = false
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp)),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialAddress == null) "📍 إضافة عنوان توصيل جديد" else "✏️ تعديل عنوان التوصيل",
                        color = TextGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Address Type Selection (Home, Work, Other)
                Text(text = "نوع العنوان", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AddressType.values().forEach { type ->
                        val isSelected = selectedType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) GoldPrimary else DarkCard)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldPrimary else TextMuted.copy(alpha = 0.3f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedType = type
                                    if (title.isBlank() || title in AddressType.values().map { it.titleAr }) {
                                        title = type.titleAr
                                    }
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${type.iconEmoji} ${type.titleAr}",
                                color = if (isSelected) RoyalBlack else Color.White,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Map Selection Button & Preview Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (latitude != null) GoldPrimary else TextMuted.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (latitude != null) "تم تحديد الموقع على الخريطة ✅" else "تحديد الموقع عبر Google Maps",
                                    color = if (latitude != null) TextGold else Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { showMapPicker = true },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.EditLocation,
                                    contentDescription = null,
                                    tint = RoyalBlack,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (latitude != null) "تغيير" else "فتح الخريطة",
                                    color = RoyalBlack,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (latitude != null && longitude != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "الإحداثيات: ${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        LocationAndGeocodingHelper.openInGoogleMaps(context, latitude!!, longitude!!)
                                    }
                                    .padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.OpenInNew,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "معاينة الرابط في Google Maps",
                                    color = GoldPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recipient Name
                OutlinedTextField(
                    value = recipientName,
                    onValueChange = { recipientName = it },
                    label = { Text("اسم المستلم", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Recipient Phone
                OutlinedTextField(
                    value = recipientPhone,
                    onValueChange = { recipientPhone = it },
                    label = { Text("رقم هاتف المستلم (للتوصيل)", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Governorate Selector Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = governorate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("المحافظة", color = TextMuted) },
                        trailingIcon = {
                            IconButton(onClick = { showGovDropdown = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = GoldPrimary)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = TextMuted
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showGovDropdown = true },
                        shape = RoundedCornerShape(10.dp)
                    )

                    DropdownMenu(
                        expanded = showGovDropdown,
                        onDismissRequest = { showGovDropdown = false },
                        modifier = Modifier.background(DarkCard)
                    ) {
                        LocationAndGeocodingHelper.YEMEN_GOVERNORATES.forEach { gov ->
                            DropdownMenuItem(
                                text = { Text(gov, color = Color.White, fontWeight = if (governorate == gov) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    governorate = gov
                                    showGovDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // City / Area
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("المدينة / المنطقة", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // District / Neighborhood
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("الحي (اختياري)", color = TextMuted) },
                    placeholder = { Text("مثال: حي الجامعة، حارة المعاين", color = TextMuted.copy(alpha = 0.5f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Street
                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("الشارع (اختياري)", color = TextMuted) },
                    placeholder = { Text("مثال: شارع تعز، شارع الثلاثين", color = TextMuted.copy(alpha = 0.5f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Nearest Landmark
                OutlinedTextField(
                    value = nearestLandmark,
                    onValueChange = { nearestLandmark = it },
                    label = { Text("أقرب معلم (اختياري ولكن يفضل كتابته)", color = TextMuted) },
                    placeholder = { Text("مثال: أمام مدرسة الفجر، بجانب مستشفى الثورة", color = TextMuted.copy(alpha = 0.5f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed address notes
                OutlinedTextField(
                    value = addressDetails,
                    onValueChange = { addressDetails = it },
                    label = { Text("العنوان بالتفصيل / ملاحظات إضافية", color = TextMuted) },
                    placeholder = { Text("رقم العمارة، الطابق، تفاصيل الموقع...", color = TextMuted.copy(alpha = 0.5f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Default Address Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDefault = !isDefault }
                ) {
                    Checkbox(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تعيين كعنوان توصيل افتراضي ⭐",
                        color = if (isDefault) TextGold else Color.White,
                        fontSize = 13.sp,
                        fontWeight = if (isDefault) FontWeight.Bold else FontWeight.Normal
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFE53935),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Save and Cancel Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TextMuted),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("إلغاء", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            if (recipientName.isBlank()) {
                                errorMessage = "يرجى كتابة اسم المستلم"
                                return@Button
                            }
                            if (recipientPhone.isBlank()) {
                                errorMessage = "يرجى كتابة رقم هاتف المستلم"
                                return@Button
                            }
                            if (governorate.isBlank()) {
                                errorMessage = "يرجى اختيار المحافظة"
                                return@Button
                            }

                            val calculatedMapsUrl = when {
                                googleMapsUrl.isNotBlank() -> googleMapsUrl
                                latitude != null && longitude != null -> "https://www.google.com/maps?q=$latitude,$longitude"
                                else -> ""
                            }

                            val addressToSave = (initialAddress ?: UserAddressEntity(
                                userId = userId,
                                recipientName = recipientName.trim(),
                                recipientPhone = recipientPhone.trim(),
                                governorate = governorate.trim(),
                                city = city.trim()
                            )).copy(
                                title = title.ifBlank { selectedType.titleAr },
                                type = selectedType,
                                recipientName = recipientName.trim(),
                                recipientPhone = recipientPhone.trim(),
                                governorate = governorate.trim(),
                                city = city.trim().ifEmpty { governorate.trim() },
                                district = district.trim(),
                                street = street.trim(),
                                nearestLandmark = nearestLandmark.trim(),
                                addressDetails = addressDetails.trim(),
                                latitude = latitude,
                                longitude = longitude,
                                googleMapsUrl = calculatedMapsUrl,
                                isDefault = isDefault
                            )

                            onSaveAddress(addressToSave)
                        },
                        modifier = Modifier.weight(2f),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "💾 حفظ العنوان",
                            color = RoyalBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
