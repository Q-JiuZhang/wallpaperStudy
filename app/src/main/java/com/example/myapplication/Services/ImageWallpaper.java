package com.example.myapplication.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.example.myapplication.R;

public class ImageWallpaper extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new ImageWallpaperEngine();
    }

    class ImageWallpaperEngine extends Engine {
        private Bitmap mBitmap;
        private final Paint mPaint = new Paint();

        public ImageWallpaperEngine() {
            //初始化画笔或加载壁纸资源
            mBitmap = BitmapFactory.decodeResource(
                    getResources(), R.drawable.my_wallpaper_1);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            drawWallpaper(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            // 释放资源
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
        }

        public void drawWallpaper(SurfaceHolder holder) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                // 适配屏幕尺寸
                int screenWidth = canvas.getWidth();
                int screenHeight = canvas.getHeight();
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                        mBitmap, screenWidth, screenHeight, true);

                // 绘制壁纸
                canvas.drawBitmap(scaledBitmap, 0, 0, mPaint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}