package com.xinyuan.opengltest.tranglegl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xinyuan.opengltest.Utils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * @author Brandon on 2018/5/13
 **/
public class TrangleRender implements GLSurfaceView.Renderer {

    float triangleCoords[] = {
            0.0f, 1.0f, 0.0f,
            -1.0f,-1f, 0.0f,
            1.0f,-1f,0.0f
    };
    float trangleColor[] = {
            1.0f,
            1.0f,
            1.0f,
            1.0f
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        createProgram();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        drawOn();
    }

    private final String verTexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    FloatBuffer vertexBuffer;
    int mProgram;
    void createProgram(){
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        int vertexShader = GLUtils.loadShader(GLES20.GL_VERTEX_SHADER,verTexShaderCode);
        int fragmentShader = GLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    int mPositionHandler;
    int mColorHandler;
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    static final int COORDS_PER_VERTEX = 3;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    void drawOn(){
        GLES20.glUseProgram(mProgram);
        mPositionHandler = GLES20.glGetAttribLocation(mProgram,"vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler,COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,vertexStride, vertexBuffer);
        mColorHandler = GLES20.glGetUniformLocation(mProgram,"vColor");
        GLES20.glUniform4fv(mColorHandler,1,color,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandler);
    }


}
