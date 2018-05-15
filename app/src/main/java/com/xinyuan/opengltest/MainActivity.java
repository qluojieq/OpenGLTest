package com.xinyuan.opengltest;


import android.app.Activity;
import android.os.Bundle;

import com.xinyuan.opengltest.cutomview.CameraView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraView glSurfaceView = new CameraView(this);
//        glSurfaceView.setEGLContextClientVersion(2);
//        glSurfaceView.setRenderer(new PictureRender(this));
//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
    }

}
