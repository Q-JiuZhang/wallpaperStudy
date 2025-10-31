package com.example.myapplication.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;

import com.example.myapplication.Services.ImageWallpaperService;
import com.example.myapplication.R;

/**
 * @author Vermouth
 * @date 2024.07.28 13:32
 */
public class MainActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // 去掉启动即打开系统壁纸选择器的行为，改为通过按钮触发
    }

    // 第一个按钮：启动系统的 Live Wallpaper 选择器
    public void openLivePicker(View view) {
        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        //Intent.ACTION_SET_WALLPAPER
        //WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
        startActivity(intent);
    }

    // 第二个按钮：切换到本应用的图片壁纸服务
    public void startImageWallpaper(View view) {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, ImageWallpaperService.class));
        startActivity(intent);
    }

    // 第三个按钮：跳转到 ViewPager 演示页面
    public void openPagerDemo(View view) {
        startActivity(new Intent(this, ViewpagerBottomNavacationView.class));
    }
}
