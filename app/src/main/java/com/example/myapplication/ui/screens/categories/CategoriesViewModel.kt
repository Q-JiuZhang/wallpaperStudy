package com.example.myapplication.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Category
import com.example.myapplication.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 分类页面ViewModel
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {
    
    val categoriesUiState: StateFlow<CategoriesUiState> = repository.getAllCategories()
        .map { categories ->
            if (categories.isEmpty()) {
                CategoriesUiState.Empty
            } else {
                CategoriesUiState.Success(categories)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoriesUiState.Loading
        )
}

/**
 * 分类页面UI状态
 */
sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    object Empty : CategoriesUiState()
    data class Success(val categories: List<Category>) : CategoriesUiState()
    data class Error(val message: String) : CategoriesUiState()
}