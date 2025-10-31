package com.example.myapplication.Renderer;

import static android.opengl.GLES20.GL_RGB;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameterfv;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import static com.example.myapplication.OpenGLUtils.getFloatbuffer;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.ShaderUtils;
import com.example.myapplication.TextureUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render2 implements GLSurfaceView.Renderer {
    private static final String TAG = "Render2";
    private final Context context;
    private int frameCount;
    private Bitmap bitmap;
    private int program;
    private int mTextureId;
    private int maTexCoorHandle;
    private int maPositionHandle;
    private int muMVPMatrixHandle;
    private int muSamplerHandle; //纹理采样器引用

    protected static final float[] VERTICES = {
            -1.0f, -1.0f, 0.0f,
            +1.0f, -1.0f, 0.0f,
            +1.0f, +1.0f, 0.0f,
            +1.0f, +1.0f, 0.0f,
            -1.0f, +1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f
    };// 画布大小，确认绘制位置，

    // Texture coordinates that maps to vertices.
    private static final float[] TEXTURES = {
            0f, 1f,
            1f, 1f,
            1f, 0f,
            1f, 0f,
            0f, 0f,
            0f, 1f
    };//采样范围，相当于裁切

    // 顶点索引数组，定义立方体的6个面（每个面由2个三角形组成）
    private static final short[] indices = {
        // 前面
        0, 1, 2,
        0, 2, 3
    };

    public Render2(Context context) {
        this.context = context;
        // 在构造函数中不进行任何 GL 相关操作，GL 上下文尚未建立
        mTextureId = -1;
    }

    public void initprogram() {
        FloatBuffer mVertexBuffer = getFloatbuffer(VERTICES);
        FloatBuffer mTextureBuffer = getFloatbuffer(TEXTURES);
        program = ShaderUtils.loadShaderProgram(R.raw.render2_ver, R.raw.render2_frag, context);
        maTexCoorHandle = glGetAttribLocation(program, "aTexCoor");
        maPositionHandle = glGetAttribLocation(program, "aPosition");
    //        muMVPMatrixHandle = glGetUniformLocation(program, "uMVPMatrix");
        muSamplerHandle = glGetUniformLocation(program, "uSampler");

        glUseProgram(program);
        // 设置顶点位置数据
        glVertexAttribPointer(maPositionHandle,
                3, GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        glEnableVertexAttribArray(maPositionHandle);

        // 设置纹理坐标数据
        glVertexAttribPointer(maTexCoorHandle,
                2, GL_FLOAT,
                false, 2 * 4, mTextureBuffer);
        glEnableVertexAttribArray(maTexCoorHandle);


        mTextureId = TextureUtils.loadTexture(context, R.drawable.my_wallpaper_1);
        Log.d(TAG, "Texture created in GL thread, id=" + mTextureId);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, mTextureId);
        glUniform1i(muSamplerHandle, 0); // 设置纹理采样器引用

        Log.d(TAG, "Program=" + program
                + " aPosition=" + maPositionHandle
                + " aTexCoor=" + maTexCoorHandle
                + " uSampler=" + muSamplerHandle);
        if (maPositionHandle < 0) Log.e(TAG, "aPosition location not found");
        if (maTexCoorHandle < 0) Log.e(TAG, "aTexCoor location not found");
        if (muSamplerHandle < 0) Log.e(TAG, "uSampler location not found");
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        initprogram();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d("cjt", "onSurfaceChanged ++++++     ");
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        if ((frameCount++ % 60) == 0) {
            Log.d(TAG, "onDrawFrame, frame=" + frameCount);
        }
    }

    public Bitmap getBitmap() {
        return null;
    }
}
