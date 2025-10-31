package com.example.myapplication.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.data.manager.WallpaperManager

/**
 * 壁纸详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WallpaperDetailViewModel = hiltViewModel()
) {
    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    
    // 显示消息
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.wallpaper_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (detailUiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                is DetailUiState.Success -> {
                    val wallpaper = (detailUiState as DetailUiState.Success).wallpaper
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 壁纸图片
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = wallpaper.imageUrl,
                                contentDescription = wallpaper.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp),
                                contentScale = ContentScale.Crop
                            )
                            
                            // 收藏按钮
                            IconButton(
                                onClick = { viewModel.toggleFavorite(!wallpaper.isFavorite) },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = if (wallpaper.isFavorite) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                    contentDescription = "收藏",
                                    tint = if (wallpaper.isFavorite) {
                                        Color.Red
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                        
                        // 壁纸信息
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = wallpaper.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = wallpaper.description ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                overflow = TextOverflow.Ellipsis
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // 壁纸标签
                            if (!wallpaper.tags.isNullOrEmpty()) {
                                Text(
                                    text = "标签: ${wallpaper.tags.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                            // 壁纸尺寸
                            Text(
                                text = "尺寸: ${wallpaper.width} x ${wallpaper.height}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // 操作按钮
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // 设置壁纸按钮
                                var showWallpaperMenu by remember { mutableStateOf(false) }
                                
                                Box {
                                    Button(
                                        onClick = { showWallpaperMenu = true },
                                        enabled = !isLoading
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Wallpaper,
                                            contentDescription = null
                                        )
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Text(text = stringResource(id = R.string.set_wallpaper))
                                    }
                                    
                                    DropdownMenu(
                                        expanded = showWallpaperMenu,
                                        onDismissRequest = { showWallpaperMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.home_screen)) },
                                            onClick = {
                                                viewModel.setWallpaper(WallpaperManager.WallpaperLocation.HOME_SCREEN)
                                                showWallpaperMenu = false
                                            }
                                        )
                                        
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.lock_screen)) },
                                            onClick = {
                                                viewModel.setWallpaper(WallpaperManager.WallpaperLocation.LOCK_SCREEN)
                                                showWallpaperMenu = false
                                            }
                                        )
                                        
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.both_screens)) },
                                            onClick = {
                                                viewModel.setWallpaper(WallpaperManager.WallpaperLocation.BOTH)
                                                showWallpaperMenu = false
                                            }
                                        )
                                    }
                                }
                                
                                // 下载壁纸按钮
                                Button(
                                    onClick = { viewModel.downloadWallpaper() },
                                    enabled = !isLoading
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    Text(text = stringResource(id = R.string.download))
                                }
                            }
                        }
                    }
                }
                
                is DetailUiState.Error -> {
                    val errorState = detailUiState as DetailUiState.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorState.message)
                    }
                }
            }
            
            // 加载指示器
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}