package com.example.myapplication.ui.navigation

/**
 * 应用导航屏幕定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Categories : Screen("categories")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object WallpaperDetail : Screen("wallpaper_detail/{wallpaperId}") {
        fun createRoute(wallpaperId: String) = "wallpaper_detail/$wallpaperId"
    }
    object CategoryDetail : Screen("category_detail/{categoryId}") {
        fun createRoute(categoryId: String) = "category_detail/$categoryId"
    }
}