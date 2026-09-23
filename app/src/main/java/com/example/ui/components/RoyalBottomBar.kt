package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainNavTab

@Composable
fun RoyalBottomBar(
    currentTab: MainNavTab,
    cartItemCount: Int,
    favoritesCount: Int,
    onTabSelected: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CardDark,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(GoldPrimary.copy(alpha = 0.3f), Color.Transparent)
                ),
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
            )
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                title = "الرئيسية",
                isSelected = currentTab == MainNavTab.HOME,
                selectedIcon = Icons.Default.Home,
                unselectedIcon = Icons.Outlined.Home,
                onClick = { onTabSelected(MainNavTab.HOME) }
            )

            BottomBarItem(
                title = "الأقسام",
                isSelected = currentTab == MainNavTab.CATEGORIES,
                selectedIcon = Icons.Default.Category,
                unselectedIcon = Icons.Outlined.Category,
                onClick = { onTabSelected(MainNavTab.CATEGORIES) }
            )

            BottomBarItem(
                title = "السلة",
                isSelected = currentTab == MainNavTab.CART,
                selectedIcon = Icons.Default.ShoppingCart,
                unselectedIcon = Icons.Outlined.ShoppingCart,
                badgeCount = cartItemCount,
                onClick = { onTabSelected(MainNavTab.CART) }
            )

            BottomBarItem(
                title = "المفضلة",
                isSelected = currentTab == MainNavTab.FAVORITES,
                selectedIcon = Icons.Default.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                badgeCount = favoritesCount,
                onClick = { onTabSelected(MainNavTab.FAVORITES) }
            )

            BottomBarItem(
                title = "حسابي",
                isSelected = currentTab == MainNavTab.ACCOUNT,
                selectedIcon = Icons.Default.Person,
                unselectedIcon = Icons.Outlined.Person,
                onClick = { onTabSelected(MainNavTab.ACCOUNT) }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    title: String,
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(48.dp)
    ) {
        Box(
            modifier = if (isSelected) {
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(GoldContainer)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            } else {
                Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            },
            contentAlignment = Alignment.Center
        ) {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = GoldPrimary,
                            contentColor = ObsidianBlack
                        ) {
                            Text(text = badgeCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                    contentDescription = title,
                    tint = if (isSelected) GoldAccent else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = title,
            color = if (isSelected) TextGold else TextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
