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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.ProductSortOption
import com.example.ui.components.ProductCard
import com.example.ui.theme.CardBorderGold
import com.example.ui.theme.CardDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun CategoriesScreen(
    viewModel: StoreViewModel,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCatId by viewModel.selectedCategoryFilter.collectAsState()
    val currentSort by viewModel.sortOption.collectAsState()
    val inStockOnly by viewModel.filterInStockOnly.collectAsState()
    val discountOnly by viewModel.filterDiscountOnly.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(bottom = 80.dp)
    ) {
        // Search Input Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = {
                    Text(
                        text = "ابحث بالاسم، القسم، SKU، أو الكلمات المفتاحية...",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "بحث", tint = GoldPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح", tint = TextMuted)
                        }
                    }
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

        // Category Horizontal Tabs
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "All" tab
            item {
                CategoryTabChip(
                    title = "الكل",
                    isSelected = selectedCatId == null,
                    onClick = { viewModel.selectedCategoryFilter.value = null }
                )
            }

            items(categories) { cat ->
                CategoryTabChip(
                    title = cat.nameAr,
                    isSelected = selectedCatId == cat.id,
                    onClick = {
                        viewModel.selectedCategoryFilter.value = if (selectedCatId == cat.id) null else cat.id
                    }
                )
            }
        }

        // Sort & Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sort Dropdown Button
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardDark)
                        .border(0.5.dp, CardBorderGold, RoundedCornerShape(10.dp))
                        .clickable { showSortMenu = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "ترتيب",
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentSort.titleAr,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(CardDark)
                ) {
                    ProductSortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.titleAr,
                                    color = if (currentSort == option) GoldPrimary else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (currentSort == option) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                viewModel.sortOption.value = option
                                showSortMenu = false
                            }
                        )
                    }
                }
            }

            // In-stock & Discount toggles
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // In Stock Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (inStockOnly) GoldContainer else CardDark)
                        .border(0.5.dp, if (inStockOnly) GoldPrimary else CardBorderGold, RoundedCornerShape(10.dp))
                        .clickable { viewModel.filterInStockOnly.value = !inStockOnly }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "متوفر فقط",
                        color = if (inStockOnly) TextGold else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (inStockOnly) FontWeight.Bold else FontWeight.Normal
                    )
                }

                // Discount Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (discountOnly) GoldContainer else CardDark)
                        .border(0.5.dp, if (discountOnly) GoldPrimary else CardBorderGold, RoundedCornerShape(10.dp))
                        .clickable { viewModel.filterDiscountOnly.value = !discountOnly }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "عروض خاصة 🔥",
                        color = if (discountOnly) TextGold else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (discountOnly) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Product Count Indicator
        Text(
            text = "عرض ${filteredProducts.size} منتج",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )

        // Products Grid
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد منتجات مطابقة للبحث",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "جرب كلمات بحث مختلفة أو أزل الفلاتر",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldContainer)
                            .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
                            .clickable {
                                viewModel.searchQuery.value = ""
                                viewModel.selectedCategoryFilter.value = null
                                viewModel.filterInStockOnly.value = false
                                viewModel.filterDiscountOnly.value = false
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "إعادة ضبط الفلاتر", color = TextGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        selectedCurrency = currency,
                        isFavorite = favoriteIds.contains(product.id),
                        onProductClick = { onProductClick(product.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(product.id) },
                        onAddToCart = { viewModel.addToCart(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTabChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) GoldPrimary else CardDark)
            .border(
                1.dp,
                if (isSelected) GoldAccent else CardBorderGold,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) ObsidianBlack else TextPrimary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
