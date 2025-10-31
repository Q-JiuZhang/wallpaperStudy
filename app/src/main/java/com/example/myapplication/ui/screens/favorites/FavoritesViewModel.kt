package com.example.myapplication.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Wallpaper
import com.example.myapplication.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 收藏页面ViewModel
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {
    
    val favoritesUiState: StateFlow<FavoritesUiState> = repository.getFavoriteWallpapers()
        .map { wallpapers ->
            if (wallpapers.isEmpty()) {
                FavoritesUiState.Empty
            } else {
                FavoritesUiState.Success(wallpapers)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Loading
        )
    
    /**
     * 切换壁纸收藏状态
     */
    fun toggleFavorite(wallpaperId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(wallpaperId, isFavorite)
        }
    }
}

/**
 * 收藏页面UI状态
 */
sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    object Empty : FavoritesUiState()
    data class Success(val wallpapers: List<Wallpaper>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}