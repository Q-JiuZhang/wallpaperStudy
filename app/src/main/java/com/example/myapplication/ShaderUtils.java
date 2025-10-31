package com.example.myapplication;

import static android.opengl.EGL14.eglGetError;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUseProgram;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Vermouth
 * @date 2024.08.13 14:37
 */
public class ShaderUtils {
    private static final String TAG = "Lzj";
    private static final String ERROR   = "ES30_ERROR";

    public static int bitmapTexture(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return -1;
        }

        int[] textureId = new int[1];
        glGenTextures(1, textureId, 0);
        if (textureId[0] == 0) {
            return -1;
        }
        glBindTexture(GL_TEXTURE_2D, textureId[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureId[0];
    }

    public static String readTextFileFromResource(int resourceId, Context context) {
        Resources res = context.getResources();
        StringBuilder code = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(res.openRawResource(resourceId)))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                code.append(nextLine).append("\n");
            }
        } catch (IOException | Resources.NotFoundException ex) {
            Log.d("lzj", "Can not read the shader source", ex);
            code = null;
        }

        return code == null ? "" : code.toString();
    }

    /*
     通过调用loadShader方法，分别加载顶点着色器与片元着色器的源代码进GPU，并分别进行编译
     然后创建一个着色器程序，分别将相应的顶点与片元着色器添加其中
     ，最后将两个着色器链接为一个整体着色器程序
     */
    public static int createProgram(String vertexSource,String fragmentSource) {//加载顶点着色器
        //加载顶点着色器
        int vextexShder = getShaderHandle(GLES30.GL_VERTEX_SHADER,vertexSource);
        if(vextexShder == 0){
            return  0;
        }

        //加载片元着色器
        int fragmentShader = getShaderHandle(GLES30.GL_FRAGMENT_SHADER,fragmentSource);
        if(fragmentShader == 0){
            return  0;
        }

        int program = GLES30.glCreateProgram();
        if(program != 0){
            GLES30.glAttachShader(program,vextexShder);
            checkGlError("glAttachShader");
            GLES30.glAttachShader(program,fragmentShader);
            checkGlError("glAttachShader");
            GLES30.glLinkProgram(program);//链接程序

            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program,GLES30.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0] != GLES30.GL_TRUE){
                Log.e(ERROR, "Could not link program" );
                Log.e(ERROR, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return  program;
    }

    public static int getShaderHandle(int type, String src) {
        final int shader = glCreateShader(type);
        if (shader == 0) {
            Log.d(TAG, "Create shader failed, type=" + type + " error message: " +
                    GLUtils.getEGLErrorString(eglGetError()));
            return 0;
        }
        glShaderSource(shader, src);
        glCompileShader(shader);
        return shader;
    }

    public static int getProgramHandle(int vertexHandle, int fragmentHandle) {
        final int program = glCreateProgram();
        if (program == 0) {
            Log.d(TAG, "Can not create OpenGL ES program error message: " +
                    GLUtils.getEGLErrorString(eglGetError()));
            return 0;
        }

        glAttachShader(program, vertexHandle);
        glAttachShader(program, fragmentHandle);
        glLinkProgram(program);
        return program;
    }

    public static String getShaderResource(int shaderId, Context context) {
        Resources res = context.getResources();
        StringBuilder code = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(res.openRawResource(shaderId)))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                code.append(nextLine).append("\n");
            }
        } catch (IOException | Resources.NotFoundException ex) {
            Log.d(TAG, "Can not read the shader source", ex);
            code = null;
        }

        return code == null ? "" : code.toString();
    }

    public static int loadShaderProgram(int vertexId, int fragmentId, Context context) {
        final String vertexSrc = getShaderResource(vertexId, context);
        final String fragmentSrc = getShaderResource(fragmentId, context);
        final int vertexHandle = getShaderHandle(GL_VERTEX_SHADER, vertexSrc);
        final int fragmentHandle = getShaderHandle(GL_FRAGMENT_SHADER, fragmentSrc);
        return getProgramHandle(vertexHandle, fragmentHandle);
    }

    public static int useGLProgram(int vertexResId, int fragmentResId, Context context) {
        int mProgramHandle = loadShaderProgram(vertexResId, fragmentResId, context);
        glUseProgram(mProgramHandle);
        return mProgramHandle;
    }

    public  static  void checkGlError(String op){
        int error;
        if((error = GLES30.glGetError())!=GLES30.GL_NO_ERROR){
            Log.e(ERROR,op+": glError "+error);
            throw  new RuntimeException(op+": glError "+error);
        }
    }
}