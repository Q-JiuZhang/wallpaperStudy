package com.example.myapplication.di

import com.example.myapplication.data.manager.DownloadManager
import com.example.myapplication.data.manager.WallpaperManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 提供Manager相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {
    
    /**
     * 提供WallpaperManager单例
     */
    @Provides
    @Singleton
    fun provideWallpaperManager(): WallpaperManager {
        return WallpaperManager()
    }
    
    /**
     * 提供DownloadManager单例
     */
    @Provides
    @Singleton
    fun provideDownloadManager(): DownloadManager {
        return DownloadManager()
    }
}