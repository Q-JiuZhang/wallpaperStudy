package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.components.BottomNavItem
import com.example.myapplication.ui.navigation.AppNavigation
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WallpaperApp()
                }
            }
        }
    }
}

/**
 * 壁纸应用主界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // 判断当前路由是否为主要导航项（底部导航栏显示的页面）
    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Categories.route, Screen.Favorites.route, Screen.Settings.route -> true
        else -> false
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick = { navItem ->
                        navigateToBottomNavItem(navController, navItem, currentRoute)
                    }
                )
            }
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * 导航到底部导航栏项目
 */
private fun navigateToBottomNavItem(
    navController: NavHostController,
    navItem: BottomNavItem,
    currentRoute: String?
) {
    // 如果点击的是当前页面，不做任何操作
    if (navItem.route == currentRoute) return
    
    // 导航到目标页面，并清除返回栈
    navController.navigate(navItem.route) {
        // 弹出到导航图的起始目的地
        popUpTo(Screen.Home.route) {
            saveState = true
        }
        // 避免创建相同目的地的多个副本
        launchSingleTop = true
        // 恢复状态
        restoreState = true
    }
}