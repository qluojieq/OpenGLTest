package com.xinyuan.opengltest.Utils;

import android.opengl.GLES20;

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
}
