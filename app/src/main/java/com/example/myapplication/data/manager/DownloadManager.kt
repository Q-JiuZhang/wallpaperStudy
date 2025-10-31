package com.example.myapplication.data.manager

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 壁纸下载管理器，负责下载和保存壁纸
 */
@Singleton
class DownloadManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * 保存壁纸到设备
     * @param bitmap 壁纸图像
     * @param fileName 文件名
     * @return 保存的文件URI或null（如果保存失败）
     */
    suspend fun saveWallpaper(bitmap: Bitmap, fileName: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                var fos: OutputStream? = null
                var imageUri: Uri? = null
                
                // 根据Android版本使用不同的保存方式
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10及以上使用MediaStore API
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Wallpapers")
                    }
                    
                    val contentResolver = context.contentResolver
                    imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    imageUri?.let { uri ->
                        fos = contentResolver.openOutputStream(uri)
                    }
                } else {
                    // Android 9及以下使用传统文件系统API
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + "Wallpapers"
                    val file = File(imagesDir)
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    
                    val image = File(imagesDir, fileName)
                    fos = FileOutputStream(image)
                    
                    // 通知媒体库更新
                    imageUri = Uri.fromFile(image)
                    context.sendBroadcast(android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
                }
                
                fos?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                
                imageUri
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}