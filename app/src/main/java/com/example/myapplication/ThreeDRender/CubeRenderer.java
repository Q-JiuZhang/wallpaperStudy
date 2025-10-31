package com.example.myapplication.ThreeDRender;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.TextureUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "CubeRenderer";
    private final Context context;
    private Cube cube;
    private int frameCount = 0;

    public CubeRenderer(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");
        GLES30.glClearColor(1,1,1,1.0f);
        int textureId = TextureUtils.loadTexture(context, R.drawable.wenli2);
        Log.d(TAG, "Texture loaded id=" + textureId);
        cube = new Cube(context, textureId);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        Log.d(TAG, "Depth test enabled");
        int err = GLES30.glGetError();
        if (err != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "GL error in onSurfaceCreated: " + err);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged w=" + width + " h=" + height);
        GLES30.glViewport(0,0,width,height);
        float ratio = (float)width/height;
        Matrix.frustumM(Cube.mProjectMatirx,0, -ratio, ratio, -1, 1, 1, 10);
        Matrix.setLookAtM(Cube.mVMatrix,0, 0,0,5, 0,0,0, 1.0f,0f,0f);
        int err = GLES30.glGetError();
        if (err != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "GL error in onSurfaceChanged: " + err);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT|GLES30.GL_COLOR_BUFFER_BIT);
        if (cube != null) {
            cube.drawAdraw();
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
}