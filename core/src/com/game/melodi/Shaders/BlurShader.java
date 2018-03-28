package com.game.melodi.Shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Created by RabitHole on 3/27/2018.
 */

public class BlurShader {

    FrameBuffer blurTargetA;
    FrameBuffer blurTargetB;
    SpriteBatch batch;
    TextureRegion tex;
    ShaderProgram blurShader;

    public BlurShader(TextureRegion tex) {
        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        batch = new SpriteBatch();
        blurShader = new ShaderProgram(vertShader(),fragShader());
        blurShader.pedantic = false;
        this.tex = tex;

    }

    public void applyBlur(){
        //Bind FBO target A
        blurTargetA.begin();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(tex,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.flush();
        blurTargetA.end();

        //swap the shaders
//this will send the batch's (FBO-sized) projection matrix to our blur shader
        batch.setShader(blurShader);

//ensure the direction is along the X-axis only
        blurShader.setUniformf("dir", 1f, 0f);

//determine radius of blur based on mouse position
        //float mouseXAmt = Mouse.getX() / (float)Display.getWidth();
        //blurShader.setUniformf("radius", mouseXAmt * MAX_BLUR);

//start rendering to target B
        blurTargetB.begin();

//render target A (the scene) using our horizontal blur shader
//it will be placed into target B
        batch.draw(blurTargetA.getColorBufferTexture(), 0, 0);

//flush the batch before ending target B
        batch.flush();

//finish rendering target B
        blurTargetB.end();

        //apply the blur only along Y-axis
        blurShader.setUniformf("dir", 0f, 1f);

        //draw the horizontally-blurred FBO target B to the screen, applying the vertical blur as we go
        batch.draw(blurTargetB.getColorBufferTexture(), 0, 0);

//end of frame -- finish the batch
        batch.end();
    }

    public String fragShader(){
        String blur = "//\"in\" attributes from our vertex shader\n" +
                "varying vec4 vColor;\n" +
                "varying vec2 vTexCoord;\n" +
                "\n" +
                "//declare uniforms\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float resolution;\n" +
                "uniform float radius;\n" +
                "uniform vec2 dir;\n" +
                "\n" +
                "void main() {\n" +
                "\t//this will be our RGBA sum\n" +
                "\tvec4 sum = vec4(0.0);\n" +
                "\t\n" +
                "\t//our original texcoord for this fragment\n" +
                "\tvec2 tc = vTexCoord;\n" +
                "\t\n" +
                "\t//the amount to blur, i.e. how far off center to sample from \n" +
                "\t//1.0 -> blur by one pixel\n" +
                "\t//2.0 -> blur by two pixels, etc.\n" +
                "\tfloat blur = radius/resolution; \n" +
                "    \n" +
                "\t//the direction of our blur\n" +
                "\t//(1.0, 0.0) -> x-axis blur\n" +
                "\t//(0.0, 1.0) -> y-axis blur\n" +
                "\tfloat hstep = dir.x;\n" +
                "\tfloat vstep = dir.y;\n" +
                "    \n" +
                "\t//apply blurring, using a 9-tap filter with predefined gaussian weights\n" +
                "    \n" +
                "\tsum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;\n" +
                "\t\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270;\n" +
                "\t\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;\n" +
                "\tsum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;\n" +
                "\n" +
                "\t//discard alpha for our simple demo, multiply by vertex color and return\n" +
                "\tgl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" +
                "}";

        return blur;
    }

    public String vertShader(){
        String blur = "//combined projection and view matrix\n" +
                "uniform mat4 u_projView;\n" +
                "\n" +
                "//\"in\" attributes from our SpriteBatch\n" +
                "attribute vec2 Position;\n" +
                "attribute vec2 TexCoord;\n" +
                "attribute vec4 Color;\n" +
                "\n" +
                "//\"out\" varyings to our fragment shader\n" +
                "varying vec4 vColor;\n" +
                "varying vec2 vTexCoord;\n" +
                " \n" +
                "void main() {\n" +
                "\tvColor = Color;\n" +
                "\tvTexCoord = TexCoord;\n" +
                "\tgl_Position = u_projView * vec4(Position, 0.0, 1.0);\n" +
                "}";

        return blur;
    }
}
