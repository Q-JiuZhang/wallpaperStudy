package com.example.myapplication.ThreeDRender;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.example.myapplication.R;
import com.example.myapplication.TextureUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class MyGLSurfaceView extends android.opengl.GLSurfaceView {
    private MyRenderer mRender;
    private final Context context;
    private static final float ANGLE_SPAN = 0.375F;
    private static final String TAG = "MyGLSurfaceView";

    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Log.d(TAG, "ctor attrs: externalHolder=null context=" + context);
        this.setEGLContextClientVersion(3);
        mRender = new MyRenderer();
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setPreserveEGLContextOnPause(true);
    }


    private class MyRenderer implements Renderer {
        public  Cube mTriangle;
        private int frameCount = 0;
        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT|GLES30.GL_COLOR_BUFFER_BIT);
            if (mTriangle != null) {
                mTriangle.drawAdraw();
            }
            frameCount++;
            if (frameCount % 60 == 1) {
                Log.d(TAG, "onDrawFrame frame=" + frameCount);
            }
            int err = GLES30.glGetError();
            if (err != GLES30.GL_NO_ERROR) {
                Log.e(TAG, "GL error in onDrawFrame: " + err);
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.d(TAG, "onSurfaceChanged w=" + width + " h=" + height);
            GLES30.glViewport(0,0,width,height);
            float ration = (float)width/height;
            Matrix.frustumM(Cube.mProjectMatirx,0,
                    -ration,ration,-1,1,1,10);//设置透视投影

            Matrix.setLookAtM(Cube.mVMatrix,0,
                    0,0,5,0,
                    0f,0,0f,1.0f,0f);//设置摄像机，增加z轴距离以更好地查看三棱锥
            int err = GLES30.glGetError();
            if (err != GLES30.GL_NO_ERROR) {
                Log.e(TAG, "GL error in onSurfaceChanged: " + err);
            }
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(TAG, "onSurfaceCreated");
            GLES30.glClearColor(1,1,1,1.0f);
            // 加载纹理
            int textureId = TextureUtils.loadTexture(context, R.drawable.wenli2);
            Log.d(TAG, "Texture loaded wenli2 id=" + textureId);
            // 使用纹理ID初始化Pyramid
            mTriangle = new Cube(context, textureId);

            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            Log.d(TAG, "Depth test enabled");
            int err = GLES30.glGetError();
            if (err != GLES30.GL_NO_ERROR) {
                Log.e(TAG, "GL error in onSurfaceCreated: " + err);
            }
        }
    }

    public  class RotateThread extends  Thread{
        public  boolean flag = true;
        @Override
        public void run() {
            while (flag){
                mRender.mTriangle.setXAngle(ANGLE_SPAN);
                mRender.mTriangle.setYAngle(ANGLE_SPAN / 2);
                try {
                    Thread.sleep(20);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

