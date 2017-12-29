package com.game.melodi.Shaders;

/**
 * Created by Paradox on 5/21/2017.
 */

public class FragShader {
    public static final String FRAG_SHADER =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n"+
                    "uniform mat4 u_projTrans;\n"+
                    "void main() {\n" +
                    "vec3 color = texture2D(u_texture, v_texCoords).rgb;\n"+
                    "float grey = (color.r + color.g + color.b) / 3.0;\n"+
                    "vec3 grayscale = vec3(gray);\n"+
                    "	gl_FragColor = v_color(grayscale,1.0);\n" +
                    "}";
}
