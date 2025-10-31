package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Wallpaper
import kotlinx.coroutines.flow.Flow

/**
 * 壁纸仓库接口
 */
interface WallpaperRepository {
    
    // 壁纸相关操作
    fun getAllWallpapers(): Flow<List<Wallpaper>>
    fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>>
    fun getFavoriteWallpapers(): Flow<List<Wallpaper>>
    suspend fun getWallpaperById(wallpaperId: String): Wallpaper?
    suspend fun insertWallpaper(wallpaper: Wallpaper)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)
    suspend fun updateWallpaper(wallpaper: Wallpaper)
    suspend fun deleteWallpaper(wallpaper: Wallpaper)
    suspend fun deleteWallpaperById(wallpaperId: String)
    suspend fun updateFavoriteStatus(wallpaperId: String, isFavorite: Boolean)
    fun searchWallpapers(query: String): Flow<List<Wallpaper>>
    
    // 分类相关操作
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(categoryId: String): Category?
    suspend fun insertCategory(category: Category)
    suspend fun insertCategories(categories: List<Category>)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun deleteCategoryById(categoryId: String)
    suspend fun incrementWallpaperCount(categoryId: String)
    suspend fun decrementWallpaperCount(categoryId: String)
}