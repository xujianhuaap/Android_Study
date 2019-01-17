package com.example.xujianhua.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final  int RENDER_BASIC = 0;
    private static final  int RENDER_RECTANGLE = 1;
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    private GLSurfaceView.Renderer renderer;
    private int render = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_view);
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();

        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));

        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setEGLConfigChooser(8,8,8,8,16,0);
            glSurfaceView.setRenderer(getRenderer());
            glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }


    }

    @Override
    protected void onPause() {
        if(rendererSet)glSurfaceView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(rendererSet)glSurfaceView.onResume();
        super.onResume();
    }

    public GLSurfaceView.Renderer getRenderer(){
        if(render == RENDER_RECTANGLE){
            return  new RectangleRender(this);
        }else {
            return new BasicRender();
        }
    }
}
