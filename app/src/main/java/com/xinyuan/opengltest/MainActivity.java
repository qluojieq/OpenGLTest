package com.xinyuan.opengltest;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.xinyuan.opengltest.tranglegl.PictureRender;
import com.xinyuan.opengltest.tranglegl.TrangleRender;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new PictureRender(this));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
    }

}
