package com.game.melodi.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Audiostream.AudioDevicePlayer2;
import com.game.melodi.Characters.Dpad;
import com.game.melodi.Characters.Elide;
import com.game.melodi.Input.SwipeHandler;
import com.game.melodi.Input.SwipeTriStrip;
import com.game.melodi.Melodi;

import java.util.Arrays;
import java.util.Random;

import static com.game.melodi.Characters.Elide.MAX_VELOCITY;
import static com.game.melodi.Melodi.player;
import static com.game.melodi.Physics.GameWorld.UNIT_HEIGHT;
import static com.game.melodi.Physics.GameWorld.UNIT_WIDTH;

/**
 * Created by Paradox on 5/21/2017.
 */

public class TerrainV2  {
    SpriteBatch batch;
    Image img;
    float[] verts,meshverts;
    ShaderProgram shaderProgram;
    Mesh quad;
    private String vshade,fshade;
    private int[] feed;
    int maxPhysics = 20000;
    private float[] normfeed;

    Texture groundTexture,surfaceTexture;

    Vector2[] xyPoints;
    Vector2 p1,p2;

    Melodi game;
    Elide elide;
    Dpad dpad;
    ChainShape chain1;
    FixtureDef fixdef;

    int j = 0;
    int i = 0;
    int id = 0;
    float x,y; // Mesh location in the world
    short[] indices;

    SwipeTriStrip tris;
    Texture tex;
    ShapeRenderer shapes;

    public void init(Melodi game) {
        this.game = game;
        img = new Image(game.manager.get("darkcave.png",Texture.class));
        img.setBounds(0,0,UNIT_WIDTH,UNIT_HEIGHT);
        //game.world.stage.addActor(img);
        batch = new SpriteBatch();
        elide = new Elide(game.world,game);
        dpad = new Dpad(game,elide.getElideBody(),elide.getBoardBody());
        maxPhysics = Gdx.graphics.getWidth()/20;

        tris = new SwipeTriStrip();
        tex = new Texture("glosser.png");

        chain1 = new ChainShape();
        fixdef = new FixtureDef();

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
        game.world.stage.getCamera().position.x+=6f;

        trackSync();
        verts = new float[feed.length*20];
        meshverts = new float[feed.length*20];
        indices = new short[feed.length*6];

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

        }

        for(j=0;j<feed.length;j+=3){
            indices[j]=(short)(j);
            indices[j+1]=(short)(j+1);
            indices[j+2]=(short)(j+2);
            indices[j+3]=(short)(j+2);
            indices[j+4]=(short)(j+3);
            indices[j+5]=(short)(j);
        }

        //generateXY();
        initializeTextures();

        /*quad = new Mesh( true, meshverts.length, 0,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
        //quad.setVertices(meshverts);
        //quad.setIndices(indices);*/

        updatePhysics(0,normfeed.length);
        generateMESH(normfeed.length-1);
        System.out.println("Vertices "+ Arrays.toString(verts));
        System.out.println("chain info " + Arrays.toString(xyPoints));

        shapes = new ShapeRenderer();
    }

    private void trackSync(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                //game.world.cam.position.x += 25;
            }
        },0,3f);
    }

    private void updatePhysics(int startIndex, int endIndex){
        EdgeShape shape;
        p1 = new Vector2();
        p2 = new Vector2();
        shape = new EdgeShape();
        int j = 0;
        /*for(int i=startIndex;i < endIndex;i+=5){
            p1 = new Vector2(verts[i],verts[i+1]);
            p2 = new Vector2(verts[i+5],verts[i+6]);
            shape.set(p1,p2);
            game.world.fixtures.add(game.world.body.createFixture(shape,5));
        }*/

        for(int i=startIndex;i < endIndex;i+=5){
            p1.set(verts[i],verts[i+1]);
            p2.set(verts[i+5],verts[i+6]);
            shape.set(p1,p2);
            game.world.fixtures.add(game.world.body.createFixture(shape,5));
        }

            game.world.endWall(p2.x,p2.y);
    }

    private Vector2[] generateXY() {
        float yOffset = 350f;
        int index = 0;
        int j;
        int m = 0;

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
        System.out.println("LEN OF XY AND NORM " + xyPoints.length + "and " + normfeed.length);

        return xyPoints;
    }

    private void initializeTextures() {
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        surfaceTexture = new Texture(Gdx.files.internal("grass.png"));
        surfaceTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        surfaceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public Mesh generateMESH(int res){
        Random r = new Random();
        int LENGTH = 15;
        //res (resolution) is the number of height-points
        //minimum is 2, which will result in a box (under each height-point there is another vertex)
        if (res < 2)
            res = 2;

        quad = new Mesh(Mesh.VertexDataType.VertexArray, true, 2 * res, (2*res-1)*6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));


        float x = 0f;     //current position to put vertices
        float med = 0.3f; //starting y
        float y = med;

        float slopeWidth = (float) (LENGTH / ((float) (res - 1)))/25; //horizontal distance between 2 heightpoints
        slopeWidth=0.1f;

        float[] tempVer = new float[2*2*2*res]; //hold vertices before setting them to the mesh
        float[] tempVerSurface = new float[2*2*2*res]; //hold vertices before setting them to the mesh
        int offset = 0; //offset to put it in tempVer
        int offset2 = 0;
        float[] tempVer2 = new float[2*res];
        int flahUp = 0;


        for (int i = 0; i<res; i++) {

            //tempVer[offset+0] = x;      tempVer[offset+1] = 0; // below height
            //tempVer[offset+2] = x;      tempVer[offset+3] = 0; // below height

            tempVer[offset+0] = (x/80);      tempVer[offset+1] = normfeed[i]; // below height
            tempVer[offset+2] = x;      tempVer[offset+3] = 0; // below height

            //tempVer[offset+4] = x;      tempVer[offset+5] = y;  // height
            //tempVer[offset+6] = x;      tempVer[offset+7] = y;  // height

            tempVer[offset+4] = (x+=35)/80;      tempVer[offset+5] = 0;  // height
            tempVer[offset+6] = x;     tempVer[offset+7] = normfeed[i+1];  // height

            //tempVerSurface[offset+0] = x;      tempVerSurface[offset+1] = y- .02f; // below height original
            //tempVerSurface[offset+2] = x;      tempVerSurface[offset+3] = y-0.02f; // below height

            tempVerSurface[offset+0] = x;      tempVerSurface[offset+1] = y- .06f; // below height
            tempVerSurface[offset+2] = x;      tempVerSurface[offset+3] = 1f; // below height

            //tempVerSurface[offset+4] = x;      tempVerSurface[offset+5] = y+0.015f;  // height original
            //tempVerSurface[offset+6] = x;      tempVerSurface[offset+7] = y+0.015f;  // height

            tempVerSurface[offset+4] = x;      tempVerSurface[offset+5] = y+2;// height
            tempVerSurface[offset+6] = x;      tempVerSurface[offset+7] = 0;  // height

            //tempVer2[offset2+0] = x;      tempVer2[offset2+1] = 0f; // below height
            tempVer2[offset2+0] = x;      tempVer2[offset2+1] = y; // below height
            //next position:
            //x += slopeWidth;
            if(flahUp < 20) {
                //y += (r.nextFloat()*10)/200;
            }else {
               // y -= (r.nextFloat()*6)/200;
            }
            flahUp++;
            if(flahUp > 35) flahUp = 0;

            offset +=8;
            offset2 +=2;
        }

        quad.setVertices(tempVer);

        // INDICES
        short[] tempIn = new short[(2*res-1)*6];
        offset = 0;
        for (int i = 0; i<2*res-2; i+=2) {

            tempIn[offset + 0] = (short) (i);       // below height
            tempIn[offset + 1] = (short) (i + 2);   // below next height
            tempIn[offset + 2] = (short) (i + 1);   // height

            tempIn[offset + 3] = (short) (i + 1);   // height
            tempIn[offset + 4] = (short) (i + 2);   // below next height
            tempIn[offset + 5] = (short) (i + 3);   // next height

            offset+=6;
        }

        quad.setIndices(tempIn);

        return quad;
    }


    public void render () {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        float dt = Gdx.graphics.getDeltaTime();
        gdxBlends();
        multiAct(dt);
        game.r.render();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projTrans", game.world.stage.getCamera().combined);
        groundTexture.bind(0);
        shaderProgram.setUniformi("u_texture", 0);
        quad.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
        setCamPos();
        //dPadInit();
        swipeInit();
    }

    private void gdxBlends(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void multiAct(float dt){
        elide.act(dt);
        //dpad.act(dt);
        game.world.update(dt);
    }

    private void setCamPos(){
        game.world.stage.getViewport().getCamera().position.set(elide.getX(),elide.getY()+1.6f,0);
        img.setBounds(game.world.stage.getCamera().position.x-1.7f,game.world.stage.getCamera().position.y-2.5f,game.world.stage.getCamera().viewportWidth+1,game.world.stage.getCamera().viewportHeight);
    }

    private void dPadInit(){

        dpad.getUpImg().setPosition(game.world.stage.getCamera().position.x-1f,game.world.stage.getCamera().position.y-1f);
        dpad.getDownImg().setPosition(game.world.stage.getCamera().position.x-1f,game.world.stage.getCamera().position.y-2f);
        dpad.getRightImg().setPosition(game.world.stage.getCamera().position.x-.5f,game.world.stage.getCamera().position.y-1.5f);
        dpad.getLeftImg().setPosition(game.world.stage.getCamera().position.x-1.5f,game.world.stage.getCamera().position.y-1.5f);

        if(dpad.getTouchUp()){
            elide.getElideBody().applyForceToCenter(0,100,true);
            elide.getBoardBody().applyForceToCenter(0,100,true);
        }

        if(dpad.getTouchDown()){
            elide.getElideBody().applyForceToCenter(0,-100,true);
            elide.getBoardBody().applyForceToCenter(0,-100,true);
        }

        if(dpad.getTouchLeft() && elide.getElideBody().getLinearVelocity().x > -MAX_VELOCITY){
            //elide.getElideBody().applyForceToCenter(-100,0,true);
            //elide.getBoardBody().applyForceToCenter(-100,0,true);
            //elide.getElideBody().applyLinearImpulse(-.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            elide.getBoardBody().applyLinearImpulse(-.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            //elide.getElideBody().applyTorque(-500,true);
        }

        if(dpad.getTouchRight() && elide.getElideBody().getLinearVelocity().x < MAX_VELOCITY){
            //elide.getElideBody().applyForceToCenter(100,0,true);
            //elide.getBoardBody().applyForceToCenter(100,0,true);
            //elide.getElideBody().applyLinearImpulse(.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            elide.getBoardBody().applyLinearImpulse(.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            elide.getElideBody().applyTorque(-600,true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            // Do something
            game.pause();
        }
    }

    private void swipeInit(){
        tris.endcap = 20f;
        //the thickness of the line
        tris.thickness = 80f;
        //generate the triangle strip from our path
        tris.update(game.swipe.path());
        //the vertex color for tinting, i.e. for opacity
        tris.color = Color.WHITE;
        //render the triangles to the screen
        tex.bind();
        tris.draw(game.world.stage.getCamera());

        drawDebug();

    }

    //optional debug drawing..
    void drawDebug() {
        Array<Vector2> input = game.swipe.input();

        //draw the raw input
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.GRAY);
        for (int i=0; i<input.size-1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i+1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();

        //draw the smoothed and simplified path
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        Array<Vector2> out = game.swipe.path();
        for (int i=0; i<out.size-1; i++) {
            Vector2 p = out.get(i);
            Vector2 p2 = out.get(i+1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();


        //render our perpendiculars
        shapes.begin(ShapeRenderer.ShapeType.Line);
        Vector2 perp = new Vector2();

        for (int i=1; i<input.size-1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i+1);

            shapes.setColor(Color.LIGHT_GRAY);
            perp.set(p).sub(p2).nor();
            perp.set(perp.y, -perp.x);
            perp.scl(10f);
            shapes.line(p.x, p.y, p.x+perp.x, p.y+perp.y);
            perp.scl(-1f);
            shapes.setColor(Color.BLUE);
            shapes.line(p.x, p.y, p.x+perp.x, p.y+perp.y);
        }
        shapes.end();
    }

    public void dispose(){
    }

}
