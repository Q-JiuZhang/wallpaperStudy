package com.example.myapplication.ui.screens.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myapplication.ui.components.WallpaperCard

/**
 * 分类详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    onBackClick: () -> Unit,
    onWallpaperClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    val categoryDetailUiState by viewModel.categoryDetailUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    when (categoryDetailUiState) {
                        is CategoryDetailUiState.Success -> {
                            val category = (categoryDetailUiState as CategoryDetailUiState.Success).category
                            Text(text = category.name)
                        }
                        else -> Text(text = "分类详情")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (categoryDetailUiState) {
            is CategoryDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is CategoryDetailUiState.Success -> {
                val successState = categoryDetailUiState as CategoryDetailUiState.Success
                val category = successState.category
                val wallpapers = successState.wallpapers
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 分类头部
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 分类图片
                            AsyncImage(
                                model = category.thumbnailUrl,
                                contentDescription = category.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // 分类描述
                            if (!category.description.isNullOrBlank()) {
                                Text(
                                    text = category.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
                            // 壁纸数量
                            Text(
                                text = "${category.wallpaperCount} 张壁纸",
                                style = MaterialTheme.typography.labelLarge
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    
                    // 壁纸列表
                    if (wallpapers.isNotEmpty()) {
                        items(wallpapers) { wallpaper ->
                            WallpaperCard(
                                wallpaper = wallpaper,
                                onClick = { onWallpaperClick(wallpaper.id) },
                                onFavoriteClick = {
                                    viewModel.toggleFavorite(wallpaper.id, !wallpaper.isFavorite)
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "该分类下没有壁纸",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
            
            is CategoryDetailUiState.Error -> {
                val errorState = categoryDetailUiState as CategoryDetailUiState.Error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorState.message)
                }
            }
        }
    }
}