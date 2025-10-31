package com.example.myapplication.ThreeDRender;


import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.example.myapplication.OpenGLUtils;
import com.example.myapplication.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube extends OpenGLUtils {
    public static float[] mProjectMatirx = new float[16];//4x4投影矩阵
    public static float[] mVMatrix = new float[16]; //摄像机位置朝向的参数矩阵
    public static float[] mMVPMatrix = new float[16];//总变换矩阵
    private static final String TAG = "Cube";

    private int mProgram;
    private int muMVPMatrixHandle;//总变换矩阵的引用
    private int maPositionHandle; //顶点位置的引用
    private int maTexCoorHandle; //顶点纹理坐标属性引用
    private int muSamplerHandle; //纹理采样器引用
    private FloatBuffer mTexCoorBuffer; //顶点纹理坐标数据缓冲
    private ShortBuffer mIndexBuffer; //顶点索引数据缓冲
    private FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    private int mTextureId; //纹理ID
    private int mvCount = 0;//顶点数量
    private short[] indices; //顶点索引数组
    private float mxAngle = 0; //绕X轴旋转的角度
    private float myAngle = 0; //绕Y轴旋转的角度
    private int drawCount = 0;

    private String mVertexShader = "#version 300 es\n" +
            "uniform mat4 uMVPMatrix;\n" +
            "in vec3 aPosition;\n" +
            "in vec2 aTexCoor;\n" +
            "out vec2 vTexCoor;\n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);\n" +
            "    vTexCoor = aTexCoor;\n" +
            "}";

    private String mFragmentShader = "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec2 vTexCoor;\n" +
            "uniform sampler2D uSampler;\n" +
            "out vec4 fragColor;\n" +
            "void main()\n" +
            "{\n" +
            "    fragColor = texture(uSampler, vTexCoor);\n" +
            "}";

    public static float[] mMMatrix = new float[16];//具体物体的3D变换矩阵

    private void initVertexData() {
        // 立方体有8个顶点
        mvCount = 8;

        // 顶点坐标数组（x, y, z）
        // 立方体顶点坐标 (x, y, z)
        float vertices[] = new float[]{
                -0.5f, -0.5f, -0.5f,  // 顶点0
                0.5f, -0.5f, -0.5f,   // 顶点1
                0.5f, 0.5f, -0.5f,    // 顶点2
                -0.5f, 0.5f, -0.5f,   // 顶点3
                -0.5f, -0.5f, 0.5f,   // 顶点4
                0.5f, -0.5f, 0.5f,    // 顶点5
                0.5f, 0.5f, 0.5f,     // 顶点6
                -0.5f, 0.5f, 0.5f     // 顶点7
        };
        mVertexBuffer = getFloatbuffer(vertices);

        // 纹理坐标数组，每个顶点对应两个纹理坐标(S, T)
        float[] texCoors = new float[]{
                0.0f, 0.0f,  // 顶点0的纹理坐标
                1.0f, 0.0f,  // 顶点1的纹理坐标
                1.0f, 1.0f,  // 顶点2的纹理坐标
                0.0f, 1.0f,  // 顶点3的纹理坐标
                0.0f, 0.0f,  // 顶点4的纹理坐标
                1.0f, 0.0f,  // 顶点5的纹理坐标
                1.0f, 1.0f,  // 顶点6的纹理坐标
                0.0f, 1.0f   // 顶点7的纹理坐标
        };
        mTexCoorBuffer = getFloatbuffer(texCoors);

        // 顶点索引数组，定义立方体的6个面（每个面由2个三角形组成）
        indices = new short[]{
                // 前面
                0, 1, 2,
                0, 2, 3,
                // 后面
                4, 5, 6,
                4, 6, 7,
                // 左面
                0, 3, 7,
                0, 7, 4,
                // 右面
                1, 2, 6,
                1, 6, 5,
                // 顶面
                3, 2, 6,
                3, 6, 7,
                // 底面
                0, 1, 5,
                0, 5, 4
        };

        // 初始化索引缓冲区
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    private void initShader() {
        mProgram = ShaderUtils.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        muSamplerHandle = GLES30.glGetUniformLocation(mProgram, "uSampler");
        Log.d(TAG, "program=" + mProgram + " aPosition=" + maPositionHandle + " aTexCoor=" + maTexCoorHandle + " uMVPMatrix=" + muMVPMatrixHandle + " uSampler=" + muSamplerHandle);
        if (maPositionHandle < 0) Log.e(TAG, "aPosition location not found");
        if (maTexCoorHandle < 0) Log.e(TAG, "aTexCoor location not found");
        if (muMVPMatrixHandle < 0) Log.e(TAG, "uMVPMatrix location not found");
        if (muSamplerHandle < 0) Log.e(TAG, "uSampler location not found");
    }

    public Cube(Context context, int textureId) {
        initVertexData();
        initShader();
        mTextureId = textureId;
        Log.d(TAG, "Cube ctor textureId=" + textureId);
        initProgram();
    }

    public void initProgram() {
        GLES30.glUseProgram(mProgram);
        Log.d(TAG, "initProgram use program=" + mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0); // 初始化变换矩阵
        Matrix.translateM(mMMatrix, 0, 0, 0, 1); // 向z轴平移1位
        Matrix.rotateM(mMMatrix, 0, mxAngle, 1, 0, 0); // 绕X轴旋转
        Matrix.rotateM(mMMatrix, 0, myAngle, 0, 1, 0); // 绕Y轴旋转
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1,
                false, getFinalMatrix(mMMatrix), 0); // 将最终的变换矩阵传进渲染管线

        // 设置顶点位置数据
        GLES30.glVertexAttribPointer(maPositionHandle,
                3, GLES30.GL_FLOAT,
                false, 3 * 4, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);

        // 设置纹理坐标数据
        GLES30.glVertexAttribPointer(maTexCoorHandle,
                2, GLES30.GL_FLOAT,
                false, 2 * 4, mTexCoorBuffer);
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);

        // 绑定纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
        GLES30.glUniform1i(muSamplerHandle, 0);
        Log.d(TAG, "bindTexture id=" + mTextureId + " unit=0");
        int err = GLES30.glGetError();
        if (err != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "GL error in initProgram: " + err);
        }
    }

    public void drawAdraw() {
        // 执行绘制（使用索引绘制）
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length,
                GLES30.GL_UNSIGNED_SHORT, mIndexBuffer);
        drawCount++;
        if (drawCount % 120 == 1) {
            Log.d(TAG, "drawAdraw count=" + drawCount + " indices=" + indices.length);
        }
        int err = GLES30.glGetError();
        if (err != GLES30.GL_NO_ERROR) {
            Log.e(TAG, "GL error in drawAdraw: " + err);
        }
    }

    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0,
                mVMatrix, 0, spec, 0); //摄像机矩阵乘以变换矩阵

        Matrix.multiplyMM(mMVPMatrix, 0,
                mProjectMatirx, 0, mMVPMatrix, 0); //投影矩阵乘以上一步的结果
        return mMVPMatrix;
    }

    public void setXAngle(float degree) {
        mxAngle += degree;
    }

    public void setYAngle(float degree) {
        myAngle += degree;
    }
}
