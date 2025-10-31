package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.CategoryDao
import com.example.myapplication.data.local.WallpaperDao
import com.example.myapplication.data.local.WallpaperDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * 提供Room数据库实例
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WallpaperDatabase {
        return Room.databaseBuilder(
            context,
            WallpaperDatabase::class.java,
            WallpaperDatabase.DATABASE_NAME
        ).build()
    }
    
    /**
     * 提供壁纸DAO
     */
    @Provides
    @Singleton
    fun provideWallpaperDao(database: WallpaperDatabase): WallpaperDao {
        return database.wallpaperDao()
    }
    
    /**
     * 提供分类DAO
     */
    @Provides
    @Singleton
    fun provideCategoryDao(database: WallpaperDatabase): CategoryDao {
        return database.categoryDao()
    }
}