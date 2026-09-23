package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.data.model.OrderStatus
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
import com.example.ui.viewmodel.StoreViewModel
import com.example.data.model.PaymentStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customerOrders by viewModel.customerOrders.collectAsState()

    if (customerOrders.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(GoldContainer)
                        .border(1.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "لا توجد طلبات",
                        tint = GoldPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ليس لديك طلبات سابقة",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "عند إتمام أي طلب ستتمكن من متابعته وتتبعه هنا",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 90.dp)
    ) {
        item {
            Text(
                text = "طلباتي وسجل المشتريات 📦",
                color = GoldPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        items(customerOrders, key = { it.id }) { order ->
            CustomerOrderCard(order = order, onContactWhatsApp = {
                val msg = "السلام عليكم، بخصوص طلبي رقم ${order.orderNumber} بمبلغ ${order.totalYer.toInt()} ر.ي"
                WhatsAppHelper.openWhatsApp(context, message = msg)
            })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun CustomerOrderCard(
    order: OrderEntity,
    onContactWhatsApp: () -> Unit
) {
    val dateStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(order.createdAt))

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorderGold, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.orderNumber,
                        color = GoldPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Status Badge
                val (statusColor, statusText) = when (order.status) {
                    OrderStatus.NEW -> Pair(StatusNew, "طلب جديد")
                    OrderStatus.UNDER_REVIEW -> Pair(Color(0xFF6366F1), "قيد المراجعة")
                    OrderStatus.CONFIRMED -> Pair(StatusDelivered, "تم تأكيد الطلب ✅")
                    OrderStatus.PREPARING -> Pair(StatusPreparing, "جاري التجهيز 📦")
                    OrderStatus.SHIPPING -> Pair(StatusShipping, "جاري الشحن 🚚")
                    OrderStatus.OUT_FOR_DELIVERY -> Pair(WarningAmber, "خرج للتوصيل 🛵")
                    OrderStatus.DELIVERED -> Pair(StatusDelivered, "تم التسليم 👑")
                    OrderStatus.CANCELLED -> Pair(ErrorRed, "تم الإلغاء ❌")
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "التاريخ: $dateStr",
                color = TextMuted,
                fontSize = 11.sp
            )

            val context = androidx.compose.ui.platform.LocalContext.current
            Text(
                text = "المحافظة: ${order.governorate} • ${order.city}",
                color = TextSecondary,
                fontSize = 11.sp
            )

            val fullAddress = order.formattedDetailedAddress()
            if (fullAddress.isNotBlank()) {
                Text(
                    text = "العنوان: $fullAddress",
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 2
                )
            }

            if (order.latitude != null && order.longitude != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GoldContainer.copy(alpha = 0.3f))
                        .clickable {
                            com.example.util.LocationAndGeocodingHelper.openInGoogleMaps(context, order.latitude, order.longitude)
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "موقع التوصيل على Google Maps ↗",
                        color = GoldPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Payment method & payment status row
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "طريقة الدفع: ${order.paymentMethodName}",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                val (payBg, payText, payLabel) = when (order.paymentStatus) {
                    PaymentStatus.VERIFIED -> Triple(StatusDelivered.copy(alpha = 0.15f), StatusDelivered, "تم التحقق ✅")
                    PaymentStatus.VERIFICATION_PENDING -> Triple(WarningAmber.copy(alpha = 0.15f), WarningAmber, "قيد التحقق ⏳")
                    PaymentStatus.COD_PENDING -> Triple(Color(0xFF38BDF8).copy(alpha = 0.15f), Color(0xFF38BDF8), "عند الاستلام 🚚")
                    PaymentStatus.REJECTED -> Triple(ErrorRed.copy(alpha = 0.15f), ErrorRed, "مرفوض ❌")
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(payBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = payLabel,
                        color = payText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (order.paymentStatus == PaymentStatus.REJECTED && order.paymentRejectionReason.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "سبب رفض الدفع: ${order.paymentRejectionReason}",
                    color = ErrorRed,
                    fontSize = 11.sp
                )
            }

            Divider(color = CardBorderGold.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 8.dp))

            // Timeline Steps Bar
            OrderStepProgress(currentStatus = order.status)

            Spacer(modifier = Modifier.height(10.dp))

            // Price & WhatsApp Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "المبلغ الإجمالي:", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = "${order.totalYer.toInt()} ر.ي",
                        color = GoldPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(WhatsAppGreen.copy(alpha = 0.15f))
                        .border(1.dp, WhatsAppGreen, RoundedCornerShape(10.dp))
                        .clickable { onContactWhatsApp() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = "واتساب", tint = WhatsAppGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "استفسار بالواتساب", color = WhatsAppGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStepProgress(currentStatus: OrderStatus) {
    val steps = listOf("جديد", "مراجعة", "تجهيز", "شحن", "تسليم")
    val currentIndex = when (currentStatus) {
        OrderStatus.NEW -> 0
        OrderStatus.UNDER_REVIEW -> 1
        OrderStatus.CONFIRMED -> 1
        OrderStatus.PREPARING -> 2
        OrderStatus.SHIPPING -> 3
        OrderStatus.OUT_FOR_DELIVERY -> 3
        OrderStatus.DELIVERED -> 4
        OrderStatus.CANCELLED -> -1
    }

    if (currentStatus == OrderStatus.CANCELLED) {
        Text(
            text = "تم إلغاء هذا الطلب",
            color = ErrorRed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { idx, label ->
            val isPassed = idx <= currentIndex
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(if (isPassed) GoldPrimary else CardDark)
                        .border(1.dp, if (isPassed) GoldAccent else TextMuted, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ObsidianBlack,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = label,
                    color = if (isPassed) TextGold else TextMuted,
                    fontSize = 9.sp,
                    fontWeight = if (isPassed) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
