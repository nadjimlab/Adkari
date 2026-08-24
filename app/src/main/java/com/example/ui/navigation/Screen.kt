package com.example.ui.navigation

import com.example.data.model.AzkarCategory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CategoryAzkar : Screen("category/{categoryId}") {
        fun createRoute(category: AzkarCategory) = "category/${category.id}"
    }
    object DhikrReader : Screen("reader/{categoryId}/{initialIndex}") {
        fun createRoute(category: AzkarCategory, index: Int = 0) = "reader/${category.id}/$index"
    }
    object Tasbih : Screen("tasbih")
    object Favorites : Screen("favorites")
    object Search : Screen("search")
    object Settings : Screen("settings")
}
