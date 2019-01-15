package com.example.xujianhua.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glGenVertexArrays;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;

/**
 * Created by xujianhua on 1/15/19.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    float[] dots = new float[]{
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f};
    int program;
    private int mPositionHandle;
    private int mMatrixHandle;
    private final float[] mMVPMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glClearColor(1, 0, 0, 1);
//        glActiveTexture(GL_TEXTURE0);\
//        IntBuffer buffer = IntBuffer.allocate(width*height);
//        glGenTextures(1,buffer);
//        glBindTexture(GL_TEXTURE_2D,buffer.get());
//        glTexParameterx(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);//添加过滤
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.wenli);
//        Buffer piexs = ByteBuffer.allocate(bitmap.getRowBytes()*bitmap.getByteCount());
//        bitmap.copyPixelsToBuffer(piexs);
//        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,width,height,0,GL_RGB,GL_UNSIGNED_BYTE,piexs);
//        glGenerateMipmap(GL_TEXTURE_2D);
//        glBindTexture(GL_TEXTURE_2D, 0);
        FloatBuffer vertices = ByteBuffer.allocateDirect(4*dots.length)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(dots);
        vertices.position(0);


//        glGenBuffers(1, VBO);
//        glBindBuffer(GL_ARRAY_BUFFER, VBO.get());
//        glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), vertices, GL_STATIC_DRAW);
//
//        glGenVertexArrays(1, VAO);
//        glBindVertexArray(VAO.get());
//
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.SIZE/8, 0);
//        glEnableVertexAttribArray(0);


//        String shaderSource =  "attribute vec4 vPosition;\n"
//                + "void main() {\n"
//                + "  gl_Position = vPosition;\n"
//                + "}";
//        int shader = glCreateShader(GL_VERTEX_SHADER);
//        glShaderSource(shader, shaderSource);
//        glCompileShader(shader);


        String fragmentShaderSource =  "precision mediump float;\n"
                + "void main() {\n"
                + "  gl_FragColor = vec4(0.5, 0, 0, 1);\n"
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

        glEnableVertexAttribArray(mPositionHandle);
        glVertexAttribPointer(mPositionHandle, 3, GL_FLOAT, false,
                12, vertices);




    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) width / height, 0.1f, 100f);
        Matrix.translateM(mMVPMatrix,0,0,-0.5f,-2.5f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
