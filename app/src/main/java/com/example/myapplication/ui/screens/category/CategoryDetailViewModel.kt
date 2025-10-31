package com.example.myapplication.ui.screens.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Wallpaper
import com.example.myapplication.data.repository.WallpaperRepository
import com.example.myapplication.ui.navigation.Screen
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
 * 分类详情页面ViewModel
 */
@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val categoryId: String = checkNotNull(savedStateHandle[Screen.CategoryDetail.ARG_CATEGORY_ID])
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 组合分类和壁纸数据
    val categoryDetailUiState = combine(
        repository.getCategoryById(categoryId),
        repository.getWallpapersByCategory(categoryId)
    ) { category, wallpapers ->
        if (category != null) {
            CategoryDetailUiState.Success(category, wallpapers)
        } else {
            CategoryDetailUiState.Error("分类不存在")
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryDetailUiState.Loading
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
 * 分类详情页面UI状态
 */
sealed class CategoryDetailUiState {
    object Loading : CategoryDetailUiState()
    data class Success(
        val category: Category,
        val wallpapers: List<Wallpaper>
    ) : CategoryDetailUiState()
    data class Error(val message: String) : CategoryDetailUiState()
}