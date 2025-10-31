package com.example.myapplication.Services;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

/**
 * 一个简单的图片壁纸服务：在壁纸画布上绘制一个前景 Drawable。
 */
public class ImageWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new ImageEngine();
    }

    private class ImageEngine extends Engine {
        private void drawFrame() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas == null) return;
                // 背景填充
                canvas.drawColor(Color.BLACK);
                // 绘制一个前景图（使用项目已有的 ic_launcher_foreground）
                Drawable drawable = ContextCompat.getDrawable(ImageWallpaperService.this, R.drawable.my_wallpaper_1);
                if (drawable != null) {
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                }
                // 简单文字标记
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(Color.WHITE);
                p.setTextSize(48f);
                canvas.drawText("图片壁纸服务", 40f, 80f, p);
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                drawFrame();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            drawFrame();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }
    }
}