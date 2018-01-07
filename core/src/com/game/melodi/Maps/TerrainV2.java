package com.game.melodi.Maps;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Characters.Dpad;
import com.game.melodi.Characters.Elide;
import com.game.melodi.Graphics.MixRender;
import com.game.melodi.Melodi;
import com.game.melodi.Physics.GameWorld;
import com.game.melodi.Shaders.FragShader;
import com.game.melodi.Shaders.VertShader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.game.melodi.Melodi.HEIGHT;
import static com.game.melodi.Melodi.WIDTH;
import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 5/21/2017.
 */

public class TerrainV2  {
    final float PTM_RATIO = 32;
    SpriteBatch batch;
    Texture img;
    float[] verts;
    Sprite sprite;
    ShaderProgram shaderProgram;
    Mesh quad,mesh;
    private String vshade,fshade;
    private int[] feed;
    int maxPhysics = 20000;
    private float[] normfeed;
    Vector2[] xyPoints;
    Array<Vector2> xyArray;
    Melodi game;
    Image testimg;
    Elide elide;
    Dpad dpad;
    FileHandle output;
    ChainShape chain1;
    FixtureDef fixdef;

    String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "uniform mat4 u_projTrans;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";
    String fragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  gl_FragColor = v_color;\n" //
            + "}";
    //+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords).a;\n" //

    public void init(Melodi game) {
        this.game = game;
        batch = new SpriteBatch();
        elide = new Elide(game.world,game);
        dpad = new Dpad(game,elide.getElideBody(),elide.getBoardBody());
        maxPhysics = Gdx.graphics.getWidth()/20;
        img = new Texture("badlogic.jpg");

        testimg = new Image(img);
        sprite = new Sprite(img);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        chain1 = new ChainShape();
        fixdef = new FixtureDef();

        testimg.setSize(.5f,.5f);
        //dpad.setSize(.5f,.5f);
        //dpad.setPosition(elide.getImage().getOriginX(),elide.getImage().getOriginY());
        //game.world.stage.addActor(testimg);
        //game.world.stage.addActor(dpad);

        vshade = Gdx.files.internal("shaders/defaultvertex.glsl").readString();
        fshade = Gdx.files.internal("shaders/defaultfrag.glsl").readString();

        System.out.println("world stage height + width" + game.world.stage.getHeight() + " " + game.world.stage.getWidth());
        System.out.println("stage height + width" + game.stage.getHeight() + " " + game.stage.getWidth());

        feed = player.player.getInfo();
        shaderProgram = new ShaderProgram(vshade,fshade);
        normfeed = new float[feed.length];
        for(int i=0;i<normfeed.length;i++){
            normfeed[i]=(float)feed[i];
        }
        for(int i=0;i<normfeed.length-1;i++){
            normfeed[i] -= Math.min(feed[80], feed[feed.length - 1]);
        }
        for(int i=0;i<normfeed.length;i++){
            normfeed[i] /= Math.max(feed[80], feed[feed.length - 1] - Math.min(feed[80], feed[feed.length - 1]));
        }
        System.out.println("NORMALIZED FEED" + Arrays.toString(normfeed));
        output = Gdx.files.local("output.txt");
        System.out.println(output.exists());
        output.writeString(Arrays.toString(normfeed),false);
        String test = output.readString();
        System.out.println(test);
        game.world.stage.getCamera().position.x+=6f;
        //TODO set to first vertice on map


        /*for(int i=0;i<normfeed.length;i++){
            normfeed[i]*=100;
        }*/
        trackSync();
        verts = new float[feed.length*20];

        //float[] verts2 = new float[500000];
        short[] indices = new short[feed.length*6];
        int j = 0;
        int i = 0;
        int id = 0;
        float x,y; // Mesh location in the world
        float width,height; // Mesh width and height

        x = y = 0f;
        //width = height = 600f;
        width = 600f;
        height = 600f;
        //Top Left Vertex Triangle 1
        /*verts[i++] = x;   //X
        //verts[i++] = y + height; //Y
        verts[i++] = y + normfeed[125];
        verts[i++] = 0;    //Z
        verts[i++] = 0f;   //U
        verts[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = x + width;
        //verts[i++] = y + height;
        verts[i++] = y + normfeed[150];
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i++] = 1f;

        //Top Right Vertex Triangle 2
        verts[i++] = x + width;
        //verts[i++] = y + height;
        verts[i++] = y + normfeed[200];
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        verts[i++] = x + width;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 1f;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i++] = 1f;
        */
        //batch.setProjectionMatrix(cam.combined);
        //cam.setToOrtho(true);
        //TODO 40-80 standard for elise, more testing
        /*for(i=100;i<normfeed.length;i++){
            verts[id++] = x/80;
            verts[id++] = y/80;
            //verts[id++] = normfeed[i] - MathUtils.sin(i) * 60  * i / 50;
            verts[id++] = 0;
            verts[id++] = 0f;
            verts[id++] = 1f;

            verts[id++] = (x+=20)/80;
            verts[id++] = normfeed[i];
            //verts[id++] =  feed[i];
            verts[id++] = 0;
            verts[id++] = 0f;
            verts[id++] = 1f;

            /*verts[id++] = (x+=40)/80;
            verts[id++] = y/80;
            //verts[id++] = normfeed[i] - MathUtils.sin(i) * 60  * i / 50;
            verts[id++] = 0;
            verts[id++] = 1f;
            verts[id++] = 0f;//testone

            verts[id++] = (x+=30)/80;
            //verts[id++] = feed[i] + MathUtils.sin(i) * 60 * i / 100;
            verts[id++] = normfeed[i]/500;
            verts[id++] = 0;
            verts[id++] = 0f;
            verts[id++] = 1f;

            //x=0;*/
            //ForTestingPurposes
            for(i=0;i<normfeed.length-1;i++){
                verts[id++] = x/80;
                verts[id++] = normfeed[i];
                //verts[id++] = normfeed[i] - MathUtils.sin(i) * 60  * i / 50;
                verts[id++] = 0;
                verts[id++] = 0f;
                verts[id++] = 1f;

                verts[id++] = (x+=35)/80;
                verts[id++] = normfeed[i+1];
                //verts[id++] =  feed[i];
                verts[id++] = 0;
                verts[id++] = 0f;
                verts[id++] = 1f;

            /*verts[id++] = (x+=40)/80;
            verts[id++] = y/80;
            //verts[id++] = normfeed[i] - MathUtils.sin(i) * 60  * i / 50;
            verts[id++] = 0;
            verts[id++] = 1f;
            verts[id++] = 0f;//testone

            verts[id++] = (x+=30)/80;
            //verts[id++] = feed[i] + MathUtils.sin(i) * 60 * i / 100;
            verts[id++] = normfeed[i]/500;
            verts[id++] = 0;
            verts[id++] = 0f;
            verts[id++] = 1f;

            //x=0;*/

        }

        for(j=0;j<feed.length;j+=3){
            indices[j]=(short)(j);
            indices[j+1]=(short)(j+1);
            indices[j+2]=(short)(j+2);
            indices[j+3]=(short)(j+2);
            indices[j+4]=(short)(j+3);
            indices[j+5]=(short)(j);
        }

        id=0;
        x=0;
        y=0;

        /*for(i=100;i<normfeed.length;i++) {
            verts2[id++] = x;
            verts2[id++] = y-12;
            //verts[id++] = normfeed[i] - MathUtils.sin(i) * 60  * i / 50;
            verts2[id++] = 0;
            verts2[id++] = 0f;
            verts2[id++] = 1f;

            verts2[id++] = x;
            //verts[id++] = feed[i] + MathUtils.sin(i) * 60  * i / 100;
            verts2[id++] = y-5f;
            verts2[id++] = 0;
            verts2[id++] = 0f;
            verts2[id++] = 1f;

            verts[id++] = x;
            //verts[id++] = feed[i] + MathUtils.sin(i) * 60 * i / 100;
            verts[id++] = y-5;
            verts[id++] = 0;
            verts[id++] = 0f;
            verts[id++] = 1f;

              for(j=0;j<normfeed.length;j++){
                indices[j]=(short)(i);
                indices[j+1]=(short)(i+3);
                indices[j+2]=(short)(i+2);
                indices[j+3]=(short)(i+2);
                indices[j+4]=(short)(i+3);
                indices[j+5]=(short)(i);
            }
        }*/
        quad = new Mesh( true, verts.length, verts.length,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
        // Create a mesh out of two triangles rendered clockwise without indices
        mesh = new Mesh( true, verts.length, 0,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

        mesh.setVertices(verts);
        //quad.setVertices(verts2);
        //quad.setIndices(indices);

        //mesh.setIndices(indices);
        //mesh.setIndices(new short[]{0,1,2,2,3,0,4,5,6,6,7,4,8,9,10,10,11,8});
        /*quad = new Mesh(true, 4,6,new VertexAttribute(VertexAttributes.Usage.Position,3,"a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked,4,"a_color"));
        quad.setVertices(new float[] {
                -1.0f,-1.0f,0,Color.toFloatBits(255,0,0,255),
                1.0f,1.0f,0,Color.toFloatBits(0,255,0,255),
                1.0f,1.0f,0,Color.toFloatBits(0,0,255,255),
                -1.0f,1.0f,0,Color.toFloatBits(255,255,0,255)});
        quad.setIndices(new short[]{
                0,2,2,3,1,0
        });*/
        generateXY();
        updatePhysics(0,normfeed.length);
        System.out.println("Vertices "+ Arrays.toString(verts));
        System.out.println("chain info " + Arrays.toString(xyPoints));
    }

    public void trackSync(){
        //vertslength
        //normfeedlength
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                //game.world.cam.position.x += 25;
            }
        },0,3f);
    }

    public void updatePhysics(int startIndex, int endIndex){
        EdgeShape shape;
        shape = new EdgeShape();
        int j = 0;

        for(int i=startIndex;i < endIndex;i+=5){
            Vector2 p1 = new Vector2(verts[i],verts[i+1]);
            Vector2 p2 = new Vector2(verts[i+5],verts[i+6]);
            shape.set(p1,p2);
            game.world.fixtures.add(game.world.body.createFixture(shape,5));
        }

        /*for(int i=startIndex;i < endIndex;i+=5){
            Vector2 p1 = new Vector2(verts[i],verts[i+6]);
            Vector2 p2 = new Vector2(verts[i+5],verts[i+6]);
            shape.set(p1,p2);
            game.world.fixtures.add(game.world.body.createFixture(shape,5));
        }*/

        //this is test img ball
        //game.world.body2.createFixture(game.world.fixdef);
        //game.world.body2.setUserData(testimg);


        //while(fixtures.size() > maxPhysics){
            //body.destroyFixture(fixtures.get(0));
            //fixtures.remove(0);
        //}

        //chain1.createChain(new Vector2[] {new Vector2(0,0f),new Vector2(10,4),new Vector2(20,2),new Vector2(0,100)});

        /*MassData m = new MassData();
        m.I = 100;
        chain1.createChain(xyPoints);
        fixdef.shape = chain1;
        game.world.body.setSleepingAllowed(false);
        game.world.body.setMassData(m);
        game.world.body.createFixture(fixdef);
        */
        //game.world.fixtures.add(game.world.body.createFixture(shape,0));
    }

    public Vector2[] generateXY() {
        float yOffset = 350f;
        int index = 0;
        int j;
        int m = 0;
        //System.out.println(j);
        //xyPoints[8646] = new Vector2(0,0);

        xyPoints = new Vector2[1000];

        for(j=0; j < 1000; j++){
            xyPoints[j] = new Vector2(0,0);

        }
        for (int i = 0; m < 1000; i+=5, m++) {
            //xyPoints.add(new Vector2(verts[i],verts[i++]));
            xyPoints[m].set(verts[i],verts[i+1]);
            //xyPoints[i].x = verts[i]/80;
            //yOffset = player.player.getInfo()[i];
            //xyPoints[i].y = yOffset - MathUtils.sin(i) * 30 * i / 6;
            //xyPoints[i].y = verts[i++]/80;
            //index += 20;

        }



        System.out.println("LEN OF XY AND NORM " + xyPoints.length + "snd " + normfeed.length);

        return xyPoints;
    }


    public void render () {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        float dt = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //batch.begin();
        //batch.setShader(shaderProgram);
        //batch.draw(sprite,sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
        //batch.end();
        //quad.render(shaderProgram,GL20.GL_TRIANGLES);
        //img.bind();
        game.r.render();
        //System.out.println("CAMERA: " + game.world.stage.getCamera().position);
        //System.out.println("ELIDE: " + elide.getX() + " " + elide.getY());

        /*shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", game.world.stage.getCamera().combined);
        shaderProgram.setUniformi("u_texture", 0);
        mesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
        //quad.render(shaderProgram,GL20.GL_TRIANGLE_STRIP);
        shaderProgram.end();*/
        elide.act(dt);
        dpad.act(dt);
        game.world.update(dt);
        dpad.debug.begin();
        dpad.drawDebug(dpad.debug);
        dpad.debug.end();
        //testimg.setPosition(game.world.body2.getPosition().x,game.world.body2.getPosition().y);
        game.world.viewport.getCamera().position.set(elide.getX(),elide.getY(),0);
        //game.test.setAutoShapeType(true);

    }

}
