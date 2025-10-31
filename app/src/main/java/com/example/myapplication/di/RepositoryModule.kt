package com.example.myapplication.di

import com.example.myapplication.data.local.CategoryDao
import com.example.myapplication.data.local.WallpaperDao
import com.example.myapplication.data.repository.WallpaperRepository
import com.example.myapplication.data.repository.WallpaperRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    /**
     * 提供壁纸仓库实现
     */
    @Provides
    @Singleton
    fun provideWallpaperRepository(
        wallpaperDao: WallpaperDao,
        categoryDao: CategoryDao
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(wallpaperDao, categoryDao)
    }
}