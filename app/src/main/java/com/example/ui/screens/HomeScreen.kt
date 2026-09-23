package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CategoryEntity
import com.example.data.model.CurrencyType
import com.example.data.model.ProductEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.CardDarkElevated
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
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
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: StoreViewModel,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()
    val bestSellers by viewModel.bestSellers.collectAsState()
    val specialOffers by viewModel.specialOffers.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val activeProducts by viewModel.activeProducts.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCatFilter by viewModel.selectedCategoryFilter.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Search Bar Field
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        viewModel.searchQuery.value = it
                        if (it.isNotBlank()) {
                            viewModel.setNavTab(MainNavTab.CATEGORIES) // Switch to Search/Catalog tab
                        }
                    },
                    placeholder = {
                        Text(
                            text = "ابحث عن منتج، قسم، أو كود SKU...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = GoldPrimary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardDark,
                        unfocusedContainerColor = CardDark,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = CardBorderGold,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 2. Hero Luxury Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(GoldAccent, GoldPrimary, Color.Transparent)),
                        RoundedCornerShape(18.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_royal_store),
                    contentDescription = "بانر المتجر الملكي",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                // Overlay Gradient
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC0C0C0E), Color(0xFA0C0C0E))
                            )
                        )
                )

                // Banner Content & CTA
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "👑 المتجر الملكي",
                            color = GoldPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldContainer)
                                .border(0.5.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "اليمن",
                                color = TextGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "جودة تستحق الثقة • تشكيلة واسعة بأسعار تنافسية",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // WhatsApp Order CTA inside banner
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(WhatsAppGreen)
                            .clickable {
                                WhatsAppHelper.openWhatsAppChat(
                                    context = context,
                                    message = "السلام عليكم، أود الطلب من عروض المتجر الملكي"
                                )
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "واتساب",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اطلب الآن عبر واتساب 777128378",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 3. Store Features Trust Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FeatureBadge(icon = Icons.Default.Verified, title = "أصلي 100%")
                FeatureBadge(icon = Icons.Default.LocalShipping, title = "توصيل لكافة المحافظات")
                FeatureBadge(icon = Icons.Default.SupportAgent, title = "خدمة 24/7")
            }
        }

        // 4. Main Categories Chips / Circles
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الأقسام الرئيسية",
                        color = GoldPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "عرض الكل",
                        color = TextGold,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            viewModel.setNavTab(MainNavTab.CATEGORIES)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { cat ->
                        CategoryCircleItem(
                            category = cat,
                            onClick = {
                                viewModel.selectedCategoryFilter.value = cat.id
                                viewModel.setNavTab(MainNavTab.CATEGORIES)
                            }
                        )
                    }
                }
            }
        }

        // 5. Special Offers & Flash Deals
        if (specialOffers.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "🔥 عروض وخصومات خاصة",
                    subTitle = "خصومات تصل حتى 25% لفترة محدودة",
                    onViewAll = { viewModel.setNavTab(MainNavTab.CATEGORIES) }
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(specialOffers) { prod ->
                        Box(modifier = Modifier.width(180.dp)) {
                            ProductCard(
                                product = prod,
                                selectedCurrency = currency,
                                isFavorite = favoriteIds.contains(prod.id),
                                onProductClick = { onProductClick(prod.id) },
                                onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                onAddToCart = { viewModel.addToCart(prod.id) }
                            )
                        }
                    }
                }
            }
        }

        // 6. Best Sellers
        if (bestSellers.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "👑 الأكثر مبيعاً",
                    subTitle = "المنتجات الأكثر طلباً وإعجاباً من عملائنا",
                    onViewAll = { viewModel.setNavTab(MainNavTab.CATEGORIES) }
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bestSellers) { prod ->
                        Box(modifier = Modifier.width(180.dp)) {
                            ProductCard(
                                product = prod,
                                selectedCurrency = currency,
                                isFavorite = favoriteIds.contains(prod.id),
                                onProductClick = { onProductClick(prod.id) },
                                onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                onAddToCart = { viewModel.addToCart(prod.id) }
                            )
                        }
                    }
                }
            }
        }

        // 7. New Arrivals
        if (newArrivals.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "✨ وصل حديثاً",
                    subTitle = "أحدث المنتجات المضافة لمتجرنا",
                    onViewAll = { viewModel.setNavTab(MainNavTab.CATEGORIES) }
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(newArrivals) { prod ->
                        Box(modifier = Modifier.width(180.dp)) {
                            ProductCard(
                                product = prod,
                                selectedCurrency = currency,
                                isFavorite = favoriteIds.contains(prod.id),
                                onProductClick = { onProductClick(prod.id) },
                                onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                onAddToCart = { viewModel.addToCart(prod.id) }
                            )
                        }
                    }
                }
            }
        }

        // 8. Direct WhatsApp Contact Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp)
                    .border(1.dp, WhatsAppGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable {
                        WhatsAppHelper.openWhatsAppChat(
                            context = context,
                            message = "السلام عليكم، أريد الاستفسار عن منتجات المتجر الملكي"
                        )
                    }
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(WhatsAppGreen.copy(alpha = 0.2f))
                                .border(1.dp, WhatsAppGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "واتساب",
                                tint = WhatsAppGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "خدمة العملاء والطلب الفوري",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "واتساب: 777128378",
                                color = WhatsAppGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(WhatsAppGreen)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "تواصل الآن",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subTitle: String,
    onViewAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = GoldPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "عرض المزيد",
                color = TextGold,
                fontSize = 11.sp,
                modifier = Modifier.clickable { onViewAll() }
            )
        }
        Text(
            text = subTitle,
            color = TextMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun FeatureBadge(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CardDark)
            .border(0.5.dp, CardBorderGold, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = GoldPrimary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CategoryCircleItem(
    category: CategoryEntity,
    onClick: () -> Unit
) {
    val icon = when (category.iconKey) {
        "bolt" -> Icons.Default.Devices
        "cosmetics" -> Icons.Default.Spa
        "phone" -> Icons.Default.Smartphone
        "gift" -> Icons.Default.CardGiftcard
        else -> Icons.Default.Category
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .width(76.dp)
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF2C2410), Color(0xFF141312))
                    )
                )
                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.nameAr,
                tint = GoldAccent,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = category.nameAr,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
