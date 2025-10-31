package com.example.myapplication.data.repository

import com.example.myapplication.data.local.CategoryDao
import com.example.myapplication.data.local.WallpaperDao
import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Wallpaper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 壁纸仓库实现类
 */
class WallpaperRepositoryImpl @Inject constructor(
    private val wallpaperDao: WallpaperDao,
    private val categoryDao: CategoryDao
) : WallpaperRepository {
    
    // 壁纸相关操作实现
    override fun getAllWallpapers(): Flow<List<Wallpaper>> {
        return wallpaperDao.getAllWallpapers()
    }
    
    override fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>> {
        return wallpaperDao.getWallpapersByCategory(categoryId)
    }
    
    override fun getFavoriteWallpapers(): Flow<List<Wallpaper>> {
        return wallpaperDao.getFavoriteWallpapers()
    }
    
    override suspend fun getWallpaperById(wallpaperId: String): Wallpaper? {
        return wallpaperDao.getWallpaperById(wallpaperId)
    }
    
    override suspend fun insertWallpaper(wallpaper: Wallpaper) {
        wallpaperDao.insertWallpaper(wallpaper)
        // 更新分类中的壁纸计数
        incrementWallpaperCount(wallpaper.category)
    }
    
    override suspend fun insertWallpapers(wallpapers: List<Wallpaper>) {
        wallpaperDao.insertWallpapers(wallpapers)
        // 更新分类中的壁纸计数
        wallpapers.groupBy { it.category }.forEach { (categoryId, _) ->
            incrementWallpaperCount(categoryId)
        }
    }
    
    override suspend fun updateWallpaper(wallpaper: Wallpaper) {
        val existingWallpaper = wallpaperDao.getWallpaperById(wallpaper.id)
        if (existingWallpaper != null && existingWallpaper.category != wallpaper.category) {
            // 如果分类发生变化，更新两个分类的计数
            decrementWallpaperCount(existingWallpaper.category)
            incrementWallpaperCount(wallpaper.category)
        }
        wallpaperDao.updateWallpaper(wallpaper)
    }
    
    override suspend fun deleteWallpaper(wallpaper: Wallpaper) {
        wallpaperDao.deleteWallpaper(wallpaper)
        // 更新分类中的壁纸计数
        decrementWallpaperCount(wallpaper.category)
    }
    
    override suspend fun deleteWallpaperById(wallpaperId: String) {
        val wallpaper = wallpaperDao.getWallpaperById(wallpaperId)
        wallpaper?.let {
            wallpaperDao.deleteWallpaperById(wallpaperId)
            // 更新分类中的壁纸计数
            decrementWallpaperCount(it.category)
        }
    }
    
    override suspend fun updateFavoriteStatus(wallpaperId: String, isFavorite: Boolean) {
        wallpaperDao.updateFavoriteStatus(wallpaperId, isFavorite)
    }
    
    override fun searchWallpapers(query: String): Flow<List<Wallpaper>> {
        return wallpaperDao.searchWallpapers(query)
    }
    
    // 分类相关操作实现
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }
    
    override suspend fun getCategoryById(categoryId: String): Category? {
        return categoryDao.getCategoryById(categoryId)
    }
    
    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
    
    override suspend fun insertCategories(categories: List<Category>) {
        categoryDao.insertCategories(categories)
    }
    
    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }
    
    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
    
    override suspend fun deleteCategoryById(categoryId: String) {
        categoryDao.deleteCategoryById(categoryId)
    }
    
    override suspend fun incrementWallpaperCount(categoryId: String) {
        categoryDao.incrementWallpaperCount(categoryId)
    }
    
    override suspend fun decrementWallpaperCount(categoryId: String) {
        categoryDao.decrementWallpaperCount(categoryId)
    }
}