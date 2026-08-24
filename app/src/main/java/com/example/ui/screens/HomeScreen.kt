package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.AuthenticAzkarProvider
import com.example.data.model.AzkarCategory
import com.example.ui.components.AdhkariEmblemLogo
import com.example.ui.components.AdhkariHeroBackgroundCard
import com.example.ui.components.CategoryCard
import com.example.ui.components.IslamicBannerCard
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldMedium
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AdhkariViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AdhkariViewModel,
    onCategoryClick: (AzkarCategory) -> Unit,
    onTasbihClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onResumeLastRead: (AzkarCategory, Int) -> Unit
) {
    val progressMap by viewModel.progressMap.collectAsState()
    val lastRead by viewModel.lastRead.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val totalRepetitions by viewModel.totalRepetitionsCompleted.collectAsState()

    val currentDateFormatted = SimpleDateFormat("EEEE ، d MMMM", Locale("ar")).format(Date())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AdhkariEmblemLogo(
                            size = 36.dp,
                            showArabicText = false
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "أذكاري | Adhkari",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "تذكّر قلبك.. ورتّل يومك",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onFavoritesClick,
                        modifier = Modifier.testTag("favorites_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "المفضلة",
                            tint = if (favoriteIds.isNotEmpty()) GoldPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.testTag("settings_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "الإعدادات",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Adhkari Hero Background & Logo Artwork
            item {
                AdhkariHeroBackgroundCard()
            }

            // 2. Search Bar Entry
            item {
                Surface(
                    onClick = onSearchClick,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_trigger")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "ابحث في الأذكار والأدعية وفضلها...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // 3. Quick Stats & Last Read Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Daily repetitions counter
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(1.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$totalRepetitions",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "تسبيحة وذكر منجز",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Favorites Count
                    Surface(
                        onClick = onFavoritesClick,
                        modifier = Modifier
                            .weight(1f)
                            .shadow(1.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${favoriteIds.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
                                )
                            )
                            Text(
                                text = "أذكار في المفضلة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            // 4. Last Read Resume Card (if present)
            if (lastRead != null) {
                item {
                    val read = lastRead!!
                    Surface(
                        onClick = {
                            val cat = AzkarCategory.values().find { it.id == read.categoryId } ?: AzkarCategory.MORNING
                            val items = AuthenticAzkarProvider.getAzkarByCategory(cat)
                            val index = items.indexOfFirst { it.id == read.dhikrId }.coerceAtLeast(0)
                            onResumeLastRead(cat, index)
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("resume_last_read_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "آخر ما تمت قراءته: ${read.categoryTitle}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = read.snippet,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "متابعة ←",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            // 5. Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "أقسام الأذكار والأدعية",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // 6. Categories List
            val categories = listOf(
                AzkarCategory.MORNING to Icons.Default.WbSunny,
                AzkarCategory.EVENING to Icons.Default.NightsStay,
                AzkarCategory.AFTER_PRAYER to Icons.Default.Mosque,
                AzkarCategory.SLEEP to Icons.Default.Bedtime,
                AzkarCategory.WAKE_UP to Icons.Default.Alarm,
                AzkarCategory.SELECTED_DUAS to Icons.Default.MenuBook,
                AzkarCategory.TASBIH to Icons.Default.Fingerprint
            )

            items(categories) { (category, icon) ->
                if (category == AzkarCategory.TASBIH) {
                    CategoryCard(
                        title = category.titleArabic,
                        subtitle = category.subtitleArabic,
                        itemCountText = "مسبحة رقمية وأدعية مأثورة",
                        badgeText = "إلكتروني",
                        icon = icon,
                        progressPercentage = 0f,
                        onClick = onTasbihClick,
                        modifier = Modifier.testTag("category_tasbih_card")
                    )
                } else {
                    val itemsInCat = AuthenticAzkarProvider.getAzkarByCategory(category)
                    val completedInCat = itemsInCat.count { item ->
                        progressMap[item.id]?.isFullyCompleted == true
                    }
                    val progressRatio = if (itemsInCat.isNotEmpty()) completedInCat.toFloat() / itemsInCat.size.toFloat() else 0f

                    CategoryCard(
                        title = category.titleArabic,
                        subtitle = category.subtitleArabic,
                        itemCountText = "${itemsInCat.size} ذكراً ودعاءً",
                        badgeText = category.recommendedTime,
                        icon = icon,
                        progressPercentage = progressRatio,
                        onClick = { onCategoryClick(category) },
                        modifier = Modifier.testTag("category_${category.id}_card")
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
