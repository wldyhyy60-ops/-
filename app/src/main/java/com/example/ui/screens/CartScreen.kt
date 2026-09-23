package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemWithProduct
import com.example.data.model.CurrencyType
import com.example.data.model.DiscountType
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper
import com.example.ui.viewmodel.MainNavTab
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun CartScreen(
    viewModel: StoreViewModel,
    onNavigateToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartItems by viewModel.userCartItems.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val deliveryZones by viewModel.deliveryZones.collectAsState()
    val selectedZone by viewModel.selectedDeliveryZone.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponMessage by viewModel.couponMessage.collectAsState()

    var couponInput by remember { mutableStateOf("") }
    var showZoneDropdown by remember { mutableStateOf(false) }

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
    val totalSar = totalYer / 148.0 // Approximate conversion for preview

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "السلة فارغة",
                        tint = GoldPrimary,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "سلة التسوق فارغة",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "تصفح الأقسام والمنتجات الملكية وأضف ما يناسبك",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.setNavTab(MainNavTab.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(46.dp)
                ) {
                    Text(text = "ابدأ التسوق الآن 👑", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "سلة المشتريات",
                        color = GoldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${cartItems.size} منتجات)",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = "إفراغ السلة",
                    color = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { viewModel.clearCart() }
                )
            }
        }

        // Cart Items
        items(cartItems, key = { it.cartItem.id }) { item ->
            CartItemRow(
                item = item,
                currency = currency,
                onIncrease = {
                    if (item.cartItem.quantity < item.product.stockQuantity) {
                        viewModel.updateCartQuantity(item.cartItem.id, item.cartItem.quantity + 1, item.product.stockQuantity)
                    } else {
                        Toast.makeText(context, "الكمية المتاحة في المخزون هي ${item.product.stockQuantity}", Toast.LENGTH_SHORT).show()
                    }
                },
                onDecrease = {
                    viewModel.updateCartQuantity(item.cartItem.id, item.cartItem.quantity - 1, item.product.stockQuantity)
                },
                onRemove = {
                    viewModel.removeFromCart(item.cartItem.id)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Delivery Zone Selector Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = "التوصيل",
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "محافظة التوصيل:",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ObsidianBlack)
                                    .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                                    .clickable { showZoneDropdown = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedZone?.governorateName ?: "اختر المحافظة",
                                    color = TextGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "قائمة المحافظات",
                                    tint = GoldPrimary
                                )
                            }

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
                                                Text(text = zone.governorateName, color = TextPrimary, fontSize = 12.sp)
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text(text = "${zone.deliveryFeeYer.toInt()} ر.ي", color = GoldPrimary, fontSize = 12.sp)
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectedDeliveryZone.value = zone
                                            showZoneDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (selectedZone != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "رسوم التوصيل: ${deliveryFeeYer.toInt()} ر.ي • المدة المتوقعة: ${selectedZone!!.estimatedDays}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                        if (subtotalYer >= selectedZone!!.freeDeliveryThresholdYer) {
                            Text(
                                text = "🎉 مبروك! مؤهل للتوصيل المجاني (طلبك تجاوز ${selectedZone!!.freeDeliveryThresholdYer.toInt()} ر.ي)",
                                color = StatusDelivered,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Coupon Code Field
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "كود الخصم / الكوبون",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = couponInput,
                            onValueChange = { couponInput = it },
                            placeholder = { Text(text = "مثال: ROYAL20 أو ALMALAKI", color = TextMuted, fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ObsidianBlack,
                                unfocusedContainerColor = ObsidianBlack,
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = CardBorderGold,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(50.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (couponInput.isNotBlank()) {
                                    viewModel.applyCoupon(couponInput)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text(text = "تطبيق", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (couponMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = couponMessage!!,
                                color = if (appliedCoupon != null) StatusDelivered else ErrorRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (appliedCoupon != null) {
                                Text(
                                    text = "إلغاء الكوبون",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    modifier = Modifier.clickable {
                                        viewModel.removeCoupon()
                                        couponInput = ""
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Price Summary
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, CardBorderGold, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ملخص الحساب",
                        color = GoldPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SummaryRow(title = "إجمالي المنتجات:", value = "${subtotalYer.toInt()} ر.ي")
                    SummaryRow(title = "رسوم التوصيل:", value = if (deliveryFeeYer == 0.0) "مجاني" else "${deliveryFeeYer.toInt()} ر.ي")
                    if (discountAmountYer > 0) {
                        SummaryRow(
                            title = "خصم الكوبون:",
                            value = "-${discountAmountYer.toInt()} ر.ي",
                            isHighlight = true
                        )
                    }

                    Divider(color = CardBorderGold, modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الإجمالي النهائي:",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${totalYer.toInt()} ر.ي",
                                color = GoldPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ما يعادل تقريباً ${totalSar.toInt()} ر.س",
                                color = TextGold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                // Primary Checkout Button
                Button(
                    onClick = onNavigateToCheckout,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBlack),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "متابعة إتمام الطلب 👑", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // WhatsApp Quick Order Button
                Button(
                    onClick = {
                        // Quick order summary via WhatsApp
                        val message = StringBuilder("👑 المتجر الملكي | طلب سريع من السلة\n\n")
                        cartItems.forEachIndexed { idx, it ->
                            message.append("${idx + 1}. ${it.product.name} (الكمية: ${it.cartItem.quantity}) - ${it.totalYer.toInt()} ر.ي\n")
                        }
                        message.append("\n💰 الإجمالي: ${totalYer.toInt()} ر.ي")
                        if (selectedZone != null) {
                            message.append("\n📍 المحافظة: ${selectedZone!!.governorateName}")
                        }
                        WhatsAppHelper.openWhatsAppChat(context, message = message.toString())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = "واتساب", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "طلب سريع عبر واتساب 777128378", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItemWithProduct,
    currency: CurrencyType,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorderGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon thumbnail
            val icon = when (item.product.categoryId) {
                1L -> Icons.Default.Devices
                2L -> Icons.Default.Spa
                3L -> Icons.Default.Smartphone
                4L -> Icons.Default.CardGiftcard
                else -> Icons.Default.Category
            }

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GoldContainer)
                    .border(0.5.dp, GoldPrimary, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.product.name,
                    tint = GoldPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                val priceUnit = if (currency == CurrencyType.YER) {
                    "${item.product.priceYer.toInt()} ر.ي"
                } else {
                    "${item.product.priceSar.toInt()} ر.س"
                }

                Text(
                    text = "سعر القطعة: $priceUnit",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "الإجمالي: ${item.totalYer.toInt()} ر.ي",
                    color = GoldPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Stepper & Delete
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = ErrorRed, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ObsidianBlack)
                        .border(1.dp, CardBorderGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(26.dp)) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "نقصان", tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = item.cartItem.quantity.toString(),
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(onClick = onIncrease, modifier = Modifier.size(26.dp)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "زيادة", tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    title: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = if (isHighlight) StatusDelivered else TextSecondary,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = if (isHighlight) StatusDelivered else TextPrimary,
            fontSize = 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}
