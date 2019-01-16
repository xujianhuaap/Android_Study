package com.example.xujianhua.myapplication;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;

/**
 * Created by xujianhua on 1/15/19.
 */

public class RectangleRender implements GLSurfaceView.Renderer {
    float[] vertexDots = new float[]{
            1, 1, 0,   // top right
            -1, 1, 0,  // top left
            -1, -1, 0, // bottom left
            1, -1, 0, }; // bottom right

    private static final short[] VERTEX_INDEX = { 0, 1, 2, 0, 2, 3 };
    int program;
    private int mPositionHandle;
    private int mMatrixHandle;
    private final float[] mMVPMatrix = new float[16];
    private ShortBuffer mIndexVertices;

    /***
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // 必须调用allocateDirect ,分配的是系统级内存,如果调用allocate 是分配的jvm的内存会报错NUll
        FloatBuffer vertices = ByteBuffer.allocateDirect(Float.SIZE/8* vertexDots.length)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexDots);

        mIndexVertices = ByteBuffer.allocateDirect(Short.SIZE/8*VERTEX_INDEX.length)
                .order(ByteOrder.nativeOrder()).asShortBuffer().put(VERTEX_INDEX);
        mIndexVertices.position(0);

        String fragmentShaderSource =  "precision mediump float;\n"
                + "void main() {\n"
                + "  gl_FragColor = vec4(1,1,0,1);\n"
                + "}";
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        String vertexShaderSource  =
                "attribute vec4 vPosition;\n"
                        + "uniform mat4 uMVPMatrix;\n"
                        + "void main() {\n"
                        + "  gl_Position = uMVPMatrix * vPosition;\n"
                        + "}";
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        program = glCreateProgram();
//        glAttachShader(program, shader);
        glAttachShader(program, fragmentShader);
        glAttachShader(program,vertexShader);
        glLinkProgram(program);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glUseProgram(program);


        mPositionHandle = glGetAttribLocation(program, "vPosition");
        mMatrixHandle = glGetUniformLocation(program, "uMVPMatrix");

        vertices.position(0);
        glEnableVertexAttribArray(mPositionHandle);
        glVertexAttribPointer(mPositionHandle, 3, GL_FLOAT, false,
                3*Float.SIZE/8, vertices);


    }


    /***
     * z 必须在[-zFar,-zNear]之间才能可以看见
     * @param gl
     * @param width
     * @param height
     */

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) width / height, 0.1f, 100f);
        Matrix.translateM(mMVPMatrix,0,0,0f,-2.5f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        glDrawElements(GL_TRIANGLES,VERTEX_INDEX.length,GL_UNSIGNED_SHORT, mIndexVertices);
    }
}
