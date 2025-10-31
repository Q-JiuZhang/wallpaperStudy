package com.example.myapplication.Renderer;

import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.GL_UNSIGNED_INT;
import static android.opengl.GLES30.glBindBuffer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glBufferData;
import static android.opengl.GLES30.glDrawElements;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glGenBuffers;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glUseProgram;
import static android.opengl.GLES30.glVertexAttribPointer;
import static android.opengl.GLES30.glViewport;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.ShaderUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render1 implements GLSurfaceView.Renderer {
    private final Context context;
    private int program;
    private int[] VAO = new int[1];
    private int[] VBO = new int[1];
    private int[] EBO = new int[1];

    public Render1(Context context) {
        this.context = context;
    }

    private float[] VERTEX = {
            -1, -1,
            1, -1,
            1, 1,
            -1, 1
    };

    private float[] frag = {
            0, 1,
            1, 1,
            1, 0,
            0, 0
    };

    private int[] index = {
            0, 1, 2,
            0, 2, 3
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("lzj", "program");
        program = ShaderUtils.loadShaderProgram(R.raw.render1_ver, R.raw.render1_frag, context);
        glUseProgram(program);
        glGenVertexArrays(1, VAO, 0);
        glBindVertexArray(VAO[0]);
        glGenBuffers(1, VBO, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBO[0]);
        glBufferData(GL_ARRAY_BUFFER, 4 * VERTEX.length, FloatBuffer.wrap(VERTEX), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0);
        glEnableVertexAttribArray(1);

        glGenBuffers(1, EBO, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, 4 * index.length, IntBuffer.wrap(index), GL_STATIC_DRAW);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("lzj", "onSurfaceChanged ++++++     ");
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("lzj", "onDrawFrame ++++");
        glDrawElements(GL_TRIANGLES, index.length, GL_UNSIGNED_INT, 0);
    }
}