package com.example.xujianhua.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_REPEAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
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
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLES20.glTexParameterfv;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform1iv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_MAG_FILTER;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_MIN_FILTER;

/**
 * Created by xujianhua on 1/15/19.
 */

public class RectangleRender implements GLSurfaceView.Renderer {
    static float radio_vertex = 2.5f;
    float[] vertexDots = new float[]{
            1.0f/radio_vertex, 1.0f/radio_vertex, 0,   // top right
            -1.0f/radio_vertex,1.0f/radio_vertex, 0,  // top left
            -1.0f/radio_vertex, -1.0f/radio_vertex, 0, // bottom left
            1.0f/radio_vertex, -1.0f/radio_vertex, 0, }; // bottom right

    private static final short[] VERTEX_INDEX = { 0, 1, 2, 0, 2, 3 };

    // GL_TEXTURE_WRAP_S 若为 GL_CLAMP_TO_EDGE 值只能位于[0,1] 左上顶点的坐标必为(0,0) 右下顶点的坐标(1,1) 值越小纹理越模糊
    //GL_TEXTURE_WRAP_S 若为GL_REPEAT ,GL_MIRRORED_REPEAT 则值可以在[0,1]之外
    private static final float[] TEX_VERTEX = {   // in clockwise order:
            1f, 0,  // right top 值越大越往左
            0, 0,  // left top
            0, 1f,  // left bottom 值越大越往上
           1f, 1f,  //  right bottom
    };

    int program;
    private int mPositionHandle;
    private int mMatrixHandle;
    private int mSTextureHandle;
    private int mTextureSampleHandle;
    private final float[] mMVPMatrix = new float[16];
    private ShortBuffer mIndexVertices;


    private Context mContext;

    public RectangleRender(Context mContext) {
        this.mContext = mContext;
    }

    /***
     *
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int[] textBuffers = new int[1];
        int texture_1 = textBuffers[0];
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.wenli);
//        glGenTextures(1,textBuffers,0);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D,texture_1);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();




        // 必须调用allocateDirect ,分配的是系统级内存,如果调用allocate 是分配的jvm的内存会报错NUll
        FloatBuffer vertices = ByteBuffer.allocateDirect(Float.SIZE/8* vertexDots.length)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexDots);

        mIndexVertices = ByteBuffer.allocateDirect(Short.SIZE/8*VERTEX_INDEX.length)
                .order(ByteOrder.nativeOrder()).asShortBuffer().put(VERTEX_INDEX);
        mIndexVertices.position(0);

        FloatBuffer tex_vertices = ByteBuffer.allocateDirect(Float.SIZE/8*TEX_VERTEX.length)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(TEX_VERTEX);
        tex_vertices.position(0);

        String fragmentShaderSource =  "precision mediump float;\n"
                + "varying vec2 v_tex;\n"
                +  "uniform sampler2D s_texture;"
                + "void main() {\n"
                + "  gl_FragColor = texture2D(s_texture,v_tex);\n"
                + "}";
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        String vertexShaderSource  =
                "attribute vec4 vPosition;\n"
                        + "attribute vec2 a_tex;\n"
                        + "varying vec2 v_tex;\n"
                        + "uniform mat4 uMVPMatrix;\n"
                        + "void main() {\n"
                        + "  gl_Position = uMVPMatrix * vPosition;\n"
                        + "  v_tex = a_tex;\n"
                        + "}";
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        program = glCreateProgram();
        glAttachShader(program, fragmentShader);
        glAttachShader(program,vertexShader);
        glLinkProgram(program);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glUseProgram(program);


        mPositionHandle = glGetAttribLocation(program, "vPosition");
        mMatrixHandle = glGetUniformLocation(program, "uMVPMatrix");
        mSTextureHandle = glGetAttribLocation(program, "a_tex");
        mTextureSampleHandle = glGetUniformLocation(program, "s_texture");

        vertices.position(0);
        glEnableVertexAttribArray(mPositionHandle);
        glVertexAttribPointer(mPositionHandle, 3, GL_FLOAT, false,
                3*Float.SIZE/8, vertices);

        glEnableVertexAttribArray(mSTextureHandle);
        glVertexAttribPointer(mSTextureHandle,4,GL_FLOAT,false,
                2*Float.SIZE/8,tex_vertices);



    }


    /***
     * z 必须在[-zFar,-zNear]之间才能可以看见
     * @param gl
     * @param width
     * @param height
     */

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height-30);
        Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) width / height, 0.1f, 100f);
        Matrix.translateM(mMVPMatrix,0,0,0f,-2.5f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(1,0,0,1);
        glUniformMatrix4fv(mMatrixHandle,1,false,mMVPMatrix,0);
        glUniform1i(mTextureSampleHandle, 0);
        glDrawElements(GL_TRIANGLES,VERTEX_INDEX.length,GL_UNSIGNED_SHORT, mIndexVertices);
    }
}
