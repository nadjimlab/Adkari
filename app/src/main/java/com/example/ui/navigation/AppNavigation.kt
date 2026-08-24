package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.data.model.AzkarCategory
import com.example.ui.screens.CategoryAzkarScreen
import com.example.ui.screens.DhikrReaderScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TasbihScreen
import com.example.ui.viewmodel.AdhkariViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AdhkariViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(220)) },
        exitTransition = { fadeOut(animationSpec = tween(220)) },
        popEnterTransition = { fadeIn(animationSpec = tween(220)) },
        popExitTransition = { fadeOut(animationSpec = tween(220)) }
    ) {
        // 1. Home
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onCategoryClick = { category ->
                    viewModel.selectCategory(category)
                    navController.navigate(Screen.CategoryAzkar.createRoute(category))
                },
                onTasbihClick = {
                    navController.navigate(Screen.Tasbih.route)
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onResumeLastRead = { category, index ->
                    viewModel.openDhikrReader(category, index)
                    navController.navigate(Screen.DhikrReader.createRoute(category, index))
                }
            )
        }

        // 2. Category Azkar List
        composable(
            route = Screen.CategoryAzkar.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val category = AzkarCategory.values().find { it.id == categoryId } ?: AzkarCategory.MORNING

            CategoryAzkarScreen(
                category = category,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onOpenReader = { index ->
                    viewModel.openDhikrReader(category, index)
                    navController.navigate(Screen.DhikrReader.createRoute(category, index))
                }
            )
        }

        // 3. Dhikr Focus Reader Mode
        composable(
            route = Screen.DhikrReader.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("initialIndex") { type = NavType.IntType }
            )
        ) {
            DhikrReaderScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. Tasbih Screen
        composable(Screen.Tasbih.route) {
            TasbihScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 5. Favorites Screen
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onOpenReader = { category, index ->
                    viewModel.openDhikrReader(category, index)
                    navController.navigate(Screen.DhikrReader.createRoute(category, index))
                }
            )
        }

        // 6. Search Screen
        composable(Screen.Search.route) {
            SearchScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onOpenReader = { category, index ->
                    viewModel.openDhikrReader(category, index)
                    navController.navigate(Screen.DhikrReader.createRoute(category, index))
                }
            )
        }

        // 7. Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
