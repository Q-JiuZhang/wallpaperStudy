package com.example.myapplication.ui.screens.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    
    private val _settingsUiState = MutableStateFlow(
        SettingsUiState(
            isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false),
            downloadQuality = sharedPreferences.getString(KEY_DOWNLOAD_QUALITY, "HIGH") ?: "HIGH",
            autoChangeWallpaper = sharedPreferences.getBoolean(KEY_AUTO_CHANGE, false),
            autoChangeInterval = sharedPreferences.getInt(KEY_AUTO_CHANGE_INTERVAL, 24)
        )
    )
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()
    
    /**
     * 更新深色主题设置
     */
    fun updateDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            _settingsUiState.value = _settingsUiState.value.copy(isDarkTheme = isDarkTheme)
            sharedPreferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply()
        }
    }
    
    /**
     * 更新下载质量设置
     */
    fun updateDownloadQuality(quality: String) {
        viewModelScope.launch {
            _settingsUiState.value = _settingsUiState.value.copy(downloadQuality = quality)
            sharedPreferences.edit().putString(KEY_DOWNLOAD_QUALITY, quality).apply()
        }
    }
    
    /**
     * 更新自动更换壁纸设置
     */
    fun updateAutoChangeWallpaper(autoChange: Boolean) {
        viewModelScope.launch {
            _settingsUiState.value = _settingsUiState.value.copy(autoChangeWallpaper = autoChange)
            sharedPreferences.edit().putBoolean(KEY_AUTO_CHANGE, autoChange).apply()
        }
    }
    
    /**
     * 更新自动更换壁纸间隔设置
     */
    fun updateAutoChangeInterval(hours: Int) {
        viewModelScope.launch {
            _settingsUiState.value = _settingsUiState.value.copy(autoChangeInterval = hours)
            sharedPreferences.edit().putInt(KEY_AUTO_CHANGE_INTERVAL, hours).apply()
        }
    }
    
    companion object {
        private const val PREFERENCES_NAME = "wallpaper_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_DOWNLOAD_QUALITY = "download_quality"
        private const val KEY_AUTO_CHANGE = "auto_change"
        private const val KEY_AUTO_CHANGE_INTERVAL = "auto_change_interval"
    }
}

/**
 * 设置页面UI状态
 */
data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val downloadQuality: String = "HIGH",
    val autoChangeWallpaper: Boolean = false,
    val autoChangeInterval: Int = 24
)