package com.example.myapplication.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.ui.navigation.Screen

/**
 * 底部导航栏项目
 */
sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val icon: @Composable () -> Unit
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        titleResId = R.string.home,
        icon = { Icon(imageVector = androidx.compose.material.icons.Icons.Default.Home, contentDescription = null) }
    )
    
    object Categories : BottomNavItem(
        route = Screen.Categories.route,
        titleResId = R.string.categories,
        icon = { Icon(imageVector = androidx.compose.material.icons.Icons.Default.List, contentDescription = null) }
    )
    
    object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        titleResId = R.string.favorites,
        icon = { Icon(imageVector = androidx.compose.material.icons.Icons.Default.Favorite, contentDescription = null) }
    )
    
    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        titleResId = R.string.settings,
        icon = { Icon(imageVector = androidx.compose.material.icons.Icons.Default.Settings, contentDescription = null) }
    )
}

/**
 * 底部导航栏列表
 */
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Categories,
    BottomNavItem.Favorites,
    BottomNavItem.Settings
)

/**
 * 底部导航栏组件
 */
@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { item.icon() },
                label = { Text(text = stringResource(id = item.titleResId)) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // 避免创建多个实例
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // 避免相同目的地多次入栈
                            launchSingleTop = true
                            // 恢复状态
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}