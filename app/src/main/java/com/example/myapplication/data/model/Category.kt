package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 壁纸分类数据模型
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val wallpaperCount: Int = 0
)