package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.categories.CategoriesScreen
import com.example.myapplication.ui.screens.categories.CategoryDetailScreen
import com.example.myapplication.ui.screens.favorites.FavoritesScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.settings.SettingsScreen
import com.example.myapplication.ui.screens.wallpaper.WallpaperDetailScreen

/**
 * 应用导航图
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                onWallpaperClick = { wallpaperId ->
                    navController.navigate(Screen.WallpaperDetail.createRoute(wallpaperId))
                },
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                }
            )
        }
        
        // 分类页
        composable(Screen.Categories.route) {
            CategoriesScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                }
            )
        }
        
        // 收藏页
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onWallpaperClick = { wallpaperId ->
                    navController.navigate(Screen.WallpaperDetail.createRoute(wallpaperId))
                }
            )
        }
        
        // 设置页
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        
        // 壁纸详情页
        composable(
            route = Screen.WallpaperDetail.route,
            arguments = listOf(navArgument("wallpaperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val wallpaperId = backStackEntry.arguments?.getString("wallpaperId") ?: ""
            WallpaperDetailScreen(
                wallpaperId = wallpaperId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // 分类详情页
        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryDetailScreen(
                categoryId = categoryId,
                onBackClick = {
                    navController.popBackStack()
                },
                onWallpaperClick = { wallpaperId ->
                    navController.navigate(Screen.WallpaperDetail.createRoute(wallpaperId))
                }
            )
        }
    }
}