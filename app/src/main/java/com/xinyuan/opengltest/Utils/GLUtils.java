package com.xinyuan.opengltest.Utils;

import android.content.res.Resources;
import android.opengl.GLES20;

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


}
