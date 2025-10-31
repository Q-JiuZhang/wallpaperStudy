package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.Wallpaper
import kotlinx.coroutines.flow.Flow

/**
 * 壁纸数据访问对象
 */
@Dao
interface WallpaperDao {
    
    @Query("SELECT * FROM wallpapers ORDER BY createdAt DESC")
    fun getAllWallpapers(): Flow<List<Wallpaper>>
    
    @Query("SELECT * FROM wallpapers WHERE category = :categoryId ORDER BY createdAt DESC")
    fun getWallpapersByCategory(categoryId: String): Flow<List<Wallpaper>>
    
    @Query("SELECT * FROM wallpapers WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteWallpapers(): Flow<List<Wallpaper>>
    
    @Query("SELECT * FROM wallpapers WHERE id = :wallpaperId")
    suspend fun getWallpaperById(wallpaperId: String): Wallpaper?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Wallpaper)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)
    
    @Update
    suspend fun updateWallpaper(wallpaper: Wallpaper)
    
    @Delete
    suspend fun deleteWallpaper(wallpaper: Wallpaper)
    
    @Query("DELETE FROM wallpapers WHERE id = :wallpaperId")
    suspend fun deleteWallpaperById(wallpaperId: String)
    
    @Query("UPDATE wallpapers SET isFavorite = :isFavorite WHERE id = :wallpaperId")
    suspend fun updateFavoriteStatus(wallpaperId: String, isFavorite: Boolean)
    
    @Query("SELECT * FROM wallpapers WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchWallpapers(query: String): Flow<List<Wallpaper>>
}