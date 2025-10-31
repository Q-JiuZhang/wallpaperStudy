package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 壁纸数据模型
 */
@Entity(tableName = "wallpapers")
data class Wallpaper(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String,
    val thumbnailUrl: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val width: Int,
    val height: Int,
    val isFavorite: Boolean = false,
    val downloadUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)