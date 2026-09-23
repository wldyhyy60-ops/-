package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.OrderEntity
import com.example.data.model.OrderItemEntity
import com.example.data.model.OrderWithItems
import com.example.data.model.ProductEntity
import java.net.URLEncoder

object WhatsAppHelper {
    const val STORE_PHONE = "777128378"
    const val STORE_PHONE_INTL = "967777128378"
    const val STORE_NAME = "المتجر الملكي | ALMALAKI STORE"

    fun buildOrderMessage(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ): String {
        val itemsText = items.joinToString("\n") { "  • ${it.productName} × ${it.quantity} (${it.totalPrice.toInt()} ر.ي)" }
        val proofText = if (order.paymentProofUri != null || order.paymentTransactionNumber.isNotBlank()) {
            "مرفق في الطلب داخل النظام" + if (order.paymentTransactionNumber.isNotBlank()) " (رقم العملية: ${order.paymentTransactionNumber})" else ""
        } else {
            "غير مطلوب (دفع عند الاستلام)"
        }

        val locationUrl = order.getEffectiveMapsUrl()
        val mapsText = if (locationUrl.isNotBlank()) {
            "\n\n🗺️ موقع العميل على Google Maps:\n$locationUrl"
        } else {
            ""
        }

        val detailedAddress = order.formattedDetailedAddress().ifEmpty { order.addressDetails }

        return """
👑 المتجر الملكي

🛒 طلب جديد

🔢 رقم الطلب:
${order.orderNumber}

👤 اسم العميل:
${order.customerName}

📱 رقم العميل:
${order.customerPhone}

📍 المحافظة والمدينة:
${order.governorate} — ${order.city}

🏠 العنوان بالتفصيل:
$detailedAddress$mapsText

💳 طريقة الدفع:
${order.paymentMethodName}

💰 الإجمالي:
${order.totalYer.toInt()} ريال يمني

📦 المنتجات:
$itemsText

💵 حالة الدفع:
${order.paymentStatus.titleAr}

📎 إثبات الدفع:
$proofText
        """.trimIndent()
    }

    fun buildOrderWhatsAppMessage(orderWithItems: OrderWithItems): String {
        return buildOrderMessage(orderWithItems.order, orderWithItems.items)
    }

    fun openWhatsApp(context: Context, phone: String = STORE_PHONE_INTL, message: String) {
        try {
            val formattedPhone = when {
                phone.startsWith("+967") -> phone.removePrefix("+")
                phone.startsWith("967") -> phone
                phone.length == 9 && phone.startsWith("7") -> "967$phone"
                else -> STORE_PHONE_INTL
            }
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$formattedPhone&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب. تأكد من تثبيت واتساب على هاتفك.", Toast.LENGTH_LONG).show()
        }
    }

    fun openWhatsAppChat(context: Context, phone: String = STORE_PHONE_INTL, message: String) {
        openWhatsApp(context, phone, message)
    }

    fun openWhatsAppForOrder(context: Context, order: OrderEntity, items: List<OrderItemEntity>) {
        val msg = buildOrderMessage(order, items)
        openWhatsApp(context, STORE_PHONE_INTL, msg)
    }

    fun openWhatsAppCustomerSupport(context: Context, message: String = "السلام عليكم، أتواصل معكم بخصوص المتجر الملكي 👑") {
        openWhatsApp(context, STORE_PHONE_INTL, message)
    }

    fun buildProductInquiryMessage(product: ProductEntity): String {
        return """
👑 المتجر الملكي | استفسار عن منتج

السلام عليكم، أود الاستفسار عن هذا المنتج:
🔹 اسم المنتج: ${product.name}
💰 السعر: ${product.priceYer.toInt()} ريال يمني
🏷️ الكود: ${product.id}

هل المنتج متوفر حالياً للتوصيل؟
        """.trimIndent()
    }

    fun shareProduct(context: Context, product: ProductEntity) {
        val shareText = """
👑 المتجر الملكي — ALMALAKI STORE
كل ما تحتاجه في مكان واحد

✨ ${product.name}
💰 السعر: ${product.priceYer.toInt()} ريال يمني

للطلب والاستفسار تواصل عبر واتساب المتجر الملكي:
https://wa.me/$STORE_PHONE_INTL
        """.trimIndent()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة المنتج")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }
}
