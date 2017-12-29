package com.game.melodi.Shaders;

/**
 * Created by Paradox on 5/21/2017.
 */

public class VertShader {

    //our attributes
    public static final String VERT_SHADER =
                    "attribute vec2 a_position;\n"+
                    "attribute vec4 a_color;\n"+
                    "attribute vec2 a_texCoord0;\n"+
                    "uniform mat4 u_projTrans;\n"+
                    "varying vec4 v_color;\n"+
                    "varying vec2 v_texCoords;\n"+
                    "void main() {\n"+
                    "v_color = a_color;\n"+
                    "v_texCoords = a_texCoord0;"+
                    "gl_Position = u_projTrans * vec4(a_position);\n"+
                    "}";

}
