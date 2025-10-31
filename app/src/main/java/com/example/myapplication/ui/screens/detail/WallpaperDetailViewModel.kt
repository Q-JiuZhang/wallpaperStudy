package com.example.myapplication.ui.screens.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.manager.DownloadManager
import com.example.myapplication.data.manager.WallpaperManager
import com.example.myapplication.data.model.Wallpaper
import com.example.myapplication.data.repository.WallpaperRepository
import com.example.myapplication.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 壁纸详情页面ViewModel
 */
@HiltViewModel
class WallpaperDetailViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val wallpaperManager: WallpaperManager,
    private val downloadManager: DownloadManager,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val wallpaperId: String = checkNotNull(savedStateHandle[Screen.WallpaperDetail.ARG_WALLPAPER_ID])
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    val detailUiState: StateFlow<DetailUiState> = repository.getWallpaperById(wallpaperId)
        .map { wallpaper ->
            if (wallpaper != null) {
                DetailUiState.Success(wallpaper)
            } else {
                DetailUiState.Error("壁纸不存在")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailUiState.Loading
        )
    
    /**
     * 切换壁纸收藏状态
     */
    fun toggleFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(wallpaperId, isFavorite)
        }
    }
    
    /**
     * 设置壁纸
     */
    fun setWallpaper(location: WallpaperManager.WallpaperLocation) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wallpaper = (detailUiState.value as? DetailUiState.Success)?.wallpaper
                if (wallpaper != null) {
                    val result = wallpaperManager.setWallpaper(context, wallpaper.imageUrl, location)
                    _message.value = if (result) "壁纸设置成功" else "壁纸设置失败"
                } else {
                    _message.value = "壁纸不存在"
                }
            } catch (e: Exception) {
                _message.value = "壁纸设置失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 下载壁纸
     */
    fun downloadWallpaper() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val wallpaper = (detailUiState.value as? DetailUiState.Success)?.wallpaper
                if (wallpaper != null) {
                    val result = downloadManager.saveWallpaper(context, wallpaper.imageUrl, wallpaper.title)
                    _message.value = if (result) "壁纸保存成功" else "壁纸保存失败"
                } else {
                    _message.value = "壁纸不存在"
                }
            } catch (e: Exception) {
                _message.value = "壁纸保存失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 清除消息
     */
    fun clearMessage() {
        _message.value = null
    }
}

/**
 * 壁纸详情页面UI状态
 */
sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val wallpaper: Wallpaper) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}