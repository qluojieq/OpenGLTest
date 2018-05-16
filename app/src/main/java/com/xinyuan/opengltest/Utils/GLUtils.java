package com.xinyuan.opengltest.Utils;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL;

/**
 * @author Brandon on 2018/5/13
 **/
public class GLUtils {

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static int  createProgram(String vertexSource,String fragmentSource){
    int vertex = loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
    if (vertex==0)
        return 0;
    int fragment = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
    if (fragment == 0)
        return 0;
    int program = GLES20.glCreateProgram();
    if (program!=0){
        GLES20.glAttachShader(program  ,vertex);
        GLES20.glAttachShader(program,fragment);
        GLES20.glLinkProgram(program);
    }
    return program;
    }

    public static int createProgram(Resources res,String vertexRes,String fragmentRes){
        return createProgram(loadFromAssetsFile(vertexRes,res),loadFromAssetsFile(fragmentRes,res));
    }

    public static String loadFromAssetsFile(String fname, Resources res){
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=res.getAssets().open(fname);
            int ch;
            byte[] buffer=new byte[1024];
            while (-1!=(ch=is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        }catch (Exception e){
            return null;
        }
        return result.toString().replaceAll("\\r\\n","\n");
    }
    public static void getShowMatrix(float[] matrix,int imgWidth,int imgHeight,int viewWidth,int
            viewHeight){
        if(imgHeight>0&&imgWidth>0&&viewWidth>0&&viewHeight>0){
            float sWhView=(float)viewWidth/viewHeight;
            float sWhImg=(float)imgWidth/imgHeight;
            float[] projection=new float[16];
            float[] camera=new float[16];
            if(sWhImg>sWhView){
                Matrix.orthoM(projection,0,-sWhView/sWhImg,sWhView/sWhImg,-1,1,1,3);
            }else{
                Matrix.orthoM(projection,0,-1,1,-sWhImg/sWhView,sWhImg/sWhView,1,3);
            }
            Matrix.setLookAtM(camera,0,0,0,1,0,0,0,0,1,0);
            Matrix.multiplyMM(matrix,0,projection,0,camera,0);
        }
    }

    public static float[] getOriginalMatrix(){
        return new float[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        };
    }
    public static float[] rotate(float[] m,float angle){
        Matrix.rotateM(m,0,angle,0,0,1);
        return m;
    }

    public static float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

}
