package com.example.myapplication.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Wallpaper
import com.example.myapplication.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // 组合壁纸和分类数据
    val homeUiState = combine(
        repository.getAllWallpapers(),
        repository.getAllCategories(),
        _searchQuery
    ) { wallpapers, categories, query ->
        val filteredWallpapers = if (query.isBlank()) {
            wallpapers
        } else {
            wallpapers.filter { it.title.contains(query, ignoreCase = true) }
        }
        HomeUiState.Success(filteredWallpapers, categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )
    
    /**
     * 更新搜索查询
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
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
 * 首页UI状态
 */
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val wallpapers: List<Wallpaper>,
        val categories: List<Category>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}