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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CurrencyType
import com.example.data.model.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.CardDarkElevated
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
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper
import com.example.ui.viewmodel.MainNavTab
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun ProductDetailScreen(
    productId: Long,
    viewModel: StoreViewModel,
    onBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.firstOrNull { it.id == productId }
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isFav = product != null && favoriteIds.contains(product.id)

    var orderQuantity by remember { mutableIntStateOf(1) }

    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(ObsidianBlack),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "المنتج غير موجود", color = TextPrimary)
        }
        return
    }

    val isOutOfStock = product.stockQuantity <= 0

    val relatedProducts = allProducts.filter {
        it.categoryId == product.categoryId && it.id != product.id
    }.take(6)

    Box(modifier = modifier.fillMaxSize().background(ObsidianBlack)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // 1. Top Header with Back & Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = GoldPrimary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { WhatsAppHelper.shareProduct(context, product) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CardDark)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "مشاركة",
                                tint = TextPrimary
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleFavorite(product.id) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CardDark)
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "المفضلة",
                                tint = if (isFav) ErrorRed else TextPrimary
                            )
                        }
                    }
                }
            }

            // 2. Product Visual Canvas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF2E2714), Color(0xFF141312))
                            )
                        )
                        .border(1.dp, CardBorderGold, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val catIcon = when (product.categoryId) {
                        1L -> Icons.Default.Devices
                        2L -> Icons.Default.Spa
                        3L -> Icons.Default.Smartphone
                        4L -> Icons.Default.CardGiftcard
                        else -> Icons.Default.Category
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(GoldContainer)
                                .border(1.5.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = catIcon,
                                contentDescription = product.name,
                                tint = GoldPrimary,
                                modifier = Modifier.size(52.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (product.sku.isNotBlank()) {
                            Text(
                                text = "كود المنتج SKU: ${product.sku}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Discount tag
                    if (product.discountPercent > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ErrorRed)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "-${product.discountPercent}% خصم",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (product.isBestSeller) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldContainer)
                                .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "الأكثر طلباً 👑",
                                color = TextGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3. Product Information Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Title
                        Text(
                            text = product.name,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dual Price Showcase
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "السعر:",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${product.priceYer.toInt()} ر.ي",
                                        color = GoldPrimary,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "(${product.priceSar.toInt()} ر.س)",
                                        color = TextGold,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (product.originalPriceYer > product.priceYer) {
                                    Text(
                                        text = "السعر السابق: ${product.originalPriceYer.toInt()} ر.ي",
                                        color = TextMuted,
                                        fontSize = 12.sp,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }
                            }

                            // Stock Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isOutOfStock -> ErrorRed.copy(alpha = 0.15f)
                                            product.stockQuantity <= 5 -> WarningAmber.copy(alpha = 0.15f)
                                            else -> StatusDelivered.copy(alpha = 0.15f)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isOutOfStock -> ErrorRed
                                            product.stockQuantity <= 5 -> WarningAmber
                                            else -> StatusDelivered
                                        },
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = when {
                                        isOutOfStock -> "❌ غير متوفر حالياً"
                                        product.stockQuantity <= 5 -> "⚠️ متبقي ${product.stockQuantity} قطع فقط"
                                        else -> "✅ متوفر: ${product.stockQuantity} قطعة"
                                    },
                                    color = when {
                                        isOutOfStock -> ErrorRed
                                        product.stockQuantity <= 5 -> WarningAmber
                                        else -> StatusDelivered
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Quantity selector (if in stock)
                        if (!isOutOfStock) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "الكمية المطلوبة:",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(ObsidianBlack)
                                        .border(1.dp, CardBorderGold, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (orderQuantity > 1) orderQuantity--
                                        },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "تقليل",
                                            tint = GoldPrimary
                                        )
                                    }

                                    Text(
                                        text = orderQuantity.toString(),
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            if (orderQuantity < product.stockQuantity) {
                                                orderQuantity++
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "عذراً، أقصى كمية متوفرة هي ${product.stockQuantity} قطع",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "زيادة",
                                            tint = GoldPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Full Description
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📖 وصف المنتج",
                            color = GoldPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = product.description,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // 5. Specifications
            if (product.specifications.isNotBlank()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .border(1.dp, CardBorderGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "⚙️ المواصفات التقنية",
                                color = GoldPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            product.specifications.lines().forEach { specLine ->
                                if (specLine.isNotBlank()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(GoldPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = specLine,
                                            color = TextPrimary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Direct WhatsApp Order Button
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardDark),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .border(1.dp, WhatsAppGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable {
                            WhatsAppHelper.openWhatsAppChat(
                                context = context,
                                message = WhatsAppHelper.buildProductInquiryMessage(product)
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "واتساب",
                            tint = WhatsAppGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "اطلب عبر واتساب مباشرة (777128378)",
                            color = WhatsAppGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 7. Similar / Related Products
            if (relatedProducts.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = "منتجات مشابهة قد تعجبك",
                            color = GoldPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(relatedProducts) { relProd ->
                                Box(modifier = Modifier.width(170.dp)) {
                                    ProductCard(
                                        product = relProd,
                                        selectedCurrency = CurrencyType.YER,
                                        isFavorite = favoriteIds.contains(relProd.id),
                                        onProductClick = {
                                            viewModel.selectProduct(relProd.id)
                                        },
                                        onFavoriteToggle = { viewModel.toggleFavorite(relProd.id) },
                                        onAddToCart = { viewModel.addToCart(relProd.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Sticky Action Bar (Add to Cart / Buy Now)
        Surface(
            color = CardDark,
            tonalElevation = 10.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(GoldPrimary.copy(alpha = 0.3f), Color.Transparent)),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add to Cart Button
                Button(
                    onClick = {
                        if (!isOutOfStock) {
                            viewModel.addToCart(product.id, orderQuantity) { success ->
                                if (success) {
                                    Toast.makeText(context, "تمت إضافة المنتج إلى السلة بنجاح 🛒", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = !isOutOfStock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldContainer,
                        contentColor = TextGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "أضف للسلة",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "إضافة للسلة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Buy Now Button
                Button(
                    onClick = {
                        if (!isOutOfStock) {
                            viewModel.addToCart(product.id, orderQuantity) { success ->
                                if (success) {
                                    viewModel.setNavTab(MainNavTab.CART)
                                    onNavigateToCheckout()
                                }
                            }
                        }
                    },
                    enabled = !isOutOfStock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "شراء الآن",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "شراء الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
