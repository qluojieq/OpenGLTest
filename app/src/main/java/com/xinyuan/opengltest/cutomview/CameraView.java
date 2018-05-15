package com.xinyuan.opengltest.cutomview;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.xinyuan.opengltest.Utils.GLUtils;
import com.xinyuan.opengltest.camera.KitkatCamera;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Brandon on 2018/5/14
 **/
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private final static String TAG = GLSurfaceView.class.getSimpleName();

    //顶点坐标
    private float pos[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f,  -1.0f,
    };

    //纹理坐标
    private float[] coord={
            0.0f, 0.0f,
            0.0f,  1.0f,
            1.0f,  0.0f,
            1.0f, 1.0f,
    };

    private KitkatCamera mCamera2;
    private float[] matrix=new float[16];
    private int width;
    private int height;
    ;
    private int dataWidth;
    private int dataHeight;
    private void calculateMatrix(){
        GLUtils.getShowMatrix(matrix,this.dataWidth,this.dataHeight,this.width,this.height);
    }

    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG," onCreate ");
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG," onSurfaceCrated ");
        init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG," onSurfaceChanged ");
        this.width = width;
        this.height = height;
        calculateMatrix();
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG," onDraw ");
        if(surfaceTexture!=null){
            surfaceTexture.updateTexImage();
        }
        ondraw();
    }

    private void ondraw() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mHMatrix,1,false,matrix,0);
        GLES20.glUniformMatrix4fv(mHCoordMatrix,1,false,mCoordMatrix,0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+textureType);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureType);
        GLES20.glUniform1i(mHTexture,textureType);

        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,2, GLES20.GL_FLOAT, false, 0,mVerBuffer);
        GLES20.glEnableVertexAttribArray(mHCoord);
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHCoord);

    }

    int cameraId = 0;
    /**
     * 总变换矩阵句柄
     */
    protected int mHMatrix;

    /**
     * 单位矩阵
     */
    public static final float[] OM= GLUtils.getOriginalMatrix();
    private int mHCoordMatrix;
    private float[] mCoordMatrix= Arrays.copyOf(OM,16);
    protected int mProgram;

    private int textureType=0;      //默认使用Texture2D0
    private int textureId=0;
    /**
     * 顶点坐标句柄
     */
    protected int mHPosition;
    /**
     * 纹理坐标句柄
     */
    protected int mHCoord;

    protected int mHTexture;
    private void init(){
        initBuffer();
        mCamera2=new KitkatCamera();
        textureId = createTextureID();
        surfaceTexture=new SurfaceTexture(textureId);

        mCamera2.open(cameraId);
        Point point=mCamera2.getPreviewSize();
        dataWidth = point.x;
        dataHeight = point.y;

        mCamera2.setPreviewTexture(surfaceTexture);
        mProgram = GLUtils.createProgram("shader/oes_base_vertex.sh","shader/oes_base_fragment.sh");

        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord=GLES20.glGetAttribLocation(mProgram,"vCoord");
        mHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        mHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");

        mHCoordMatrix=GLES20.glGetUniformLocation(mProgram,"vCoordMatrix");
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mCamera2.preview();

    }
    private SurfaceTexture surfaceTexture;
    private int createTextureID(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }


    /**
     * 顶点坐标Buffer
     */
    protected FloatBuffer mVerBuffer;

    /**
     * 纹理坐标Buffer
     */
    protected FloatBuffer mTexBuffer;
    /**
     * Buffer初始化
     */
    protected void initBuffer(){
        ByteBuffer a=ByteBuffer.allocateDirect(32);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer=a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
        ByteBuffer b=ByteBuffer.allocateDirect(32);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer=b.asFloatBuffer();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

}
