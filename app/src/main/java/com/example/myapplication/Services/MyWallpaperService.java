package com.example.myapplication.Services;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.example.myapplication.Renderer.Render1;
import com.example.myapplication.Renderer.Render2;
import com.example.myapplication.TextureUtils;
import com.example.myapplication.ThreeDRender.Cube;
import com.example.myapplication.ThreeDRender.MyGLSurfaceView;

/**
 * @author Vermouth
 * @date 2024.08.12 20:39
 */
public class MyWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MyEngine(this);
    }

    public class MyEngine extends Engine {
        private final Context mContext;
        private MyGLSurfaceView glView;
        MyEngine(Context context) {
            this.mContext = context;
            glView = new MyGLSurfaceView(mContext) {
                @Override
                public SurfaceHolder getHolder() {
                    return MyEngine.this.getSurfaceHolder();
                }
            };
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            // GLSurfaceView 已在构造器中注册到 Engine 的 SurfaceHolder，
            // 系统会通过其 Callback 驱动 EGL 初始化与渲染线程启动。
            if (glView != null) {
                glView.onResume();
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (glView != null) {
                if (visible) glView.onResume();
                else glView.onPause();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (glView != null) {
                glView.onPause();
                glView = null;
            }
        }

        public class MySurfaceView extends GLSurfaceView implements SurfaceHolder.Callback {

            public MySurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return MyEngine.this.getSurfaceHolder();
            }
        }
    }
}
