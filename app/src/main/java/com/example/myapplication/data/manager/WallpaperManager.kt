package com.example.myapplication.data.manager

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 壁纸管理器，负责设置壁纸和锁屏
 */
@Singleton
class WallpaperManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val wallpaperManager = WallpaperManager.getInstance(context)
    
    /**
     * 设置壁纸
     * @param bitmap 壁纸图像
     * @param location 设置位置（主屏幕、锁屏或两者）
     * @return 是否设置成功
     */
    suspend fun setWallpaper(bitmap: Bitmap, location: WallpaperLocation): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    when (location) {
                        WallpaperLocation.HOME -> {
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                        }
                        WallpaperLocation.LOCK -> {
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                        }
                        WallpaperLocation.BOTH -> {
                            wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                        }
                    }
                } else {
                    // 旧版本Android只支持设置主屏幕壁纸
                    wallpaperManager.setBitmap(bitmap)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }
    
    /**
     * 获取当前壁纸
     * @return 当前壁纸的Bitmap
     */
    suspend fun getCurrentWallpaper(): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                wallpaperManager.drawable?.let { drawable ->
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = android.graphics.Canvas(bitmap)
                    drawable.setBounds(0, 0, width, height)
                    drawable.draw(canvas)
                    bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

/**
 * 壁纸设置位置枚举
 */
enum class WallpaperLocation {
    HOME,   // 主屏幕
    LOCK,   // 锁屏
    BOTH    // 主屏幕和锁屏
}