package com.example.ui.navigation

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
import com.example.ui.screens.*
import com.example.ui.viewmodel.AdhkariViewModel
import com.example.ui.viewmodel.ThabatViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AdhkariViewModel, thabatViewModel: ThabatViewModel, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "thabat", modifier = modifier, enterTransition = { fadeIn(tween(220)) }, exitTransition = { fadeOut(tween(220)) }) {
        composable("thabat") { ThabatApp(onOpenAdhkar = { navController.navigate(Screen.Home.route) }, viewModel = thabatViewModel) }
        composable(Screen.Home.route) { HomeScreen(viewModel, { category -> viewModel.selectCategory(category); navController.navigate(Screen.CategoryAzkar.createRoute(category)) }, { navController.navigate(Screen.Tasbih.route) }, { navController.navigate(Screen.Favorites.route) }, { navController.navigate(Screen.Search.route) }, { navController.navigate(Screen.Settings.route) }, { category, index -> viewModel.openDhikrReader(category, index); navController.navigate(Screen.DhikrReader.createRoute(category, index)) }) }
        composable(Screen.CategoryAzkar.route, arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) { entry -> val category = AzkarCategory.values().find { it.id == entry.arguments?.getString("categoryId") } ?: AzkarCategory.MORNING; CategoryAzkarScreen(category, viewModel, { navController.popBackStack() }, { viewModel.openDhikrReader(category, it); navController.navigate(Screen.DhikrReader.createRoute(category, it)) }) }
        composable(Screen.DhikrReader.route, arguments = listOf(navArgument("categoryId") { type = NavType.StringType }, navArgument("initialIndex") { type = NavType.IntType })) { DhikrReaderScreen(viewModel) { navController.popBackStack() } }
        composable(Screen.Tasbih.route) { TasbihScreen(viewModel) { navController.popBackStack() } }
        composable(Screen.Favorites.route) { FavoritesScreen(viewModel, { navController.popBackStack() }) { category, index -> viewModel.openDhikrReader(category, index); navController.navigate(Screen.DhikrReader.createRoute(category, index)) } }
        composable(Screen.Search.route) { SearchScreen(viewModel, { navController.popBackStack() }) { category, index -> viewModel.openDhikrReader(category, index); navController.navigate(Screen.DhikrReader.createRoute(category, index)) } }
        composable(Screen.Settings.route) { SettingsScreen(viewModel) { navController.popBackStack() } }
    }
}
