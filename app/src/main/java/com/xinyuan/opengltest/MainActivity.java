package com.xinyuan.opengltest;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.xinyuan.opengltest.tranglegl.TrangleRender;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new TrangleRender());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
    }

}
