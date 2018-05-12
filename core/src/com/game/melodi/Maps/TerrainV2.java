package com.game.melodi.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Animations.AnimatedImage2;
import com.game.melodi.Animations.Background3;
import com.game.melodi.Audiostream.AudioDevicePlayer2;
import com.game.melodi.Characters.Dpad;
import com.game.melodi.Characters.Elide;
import com.game.melodi.Input.SimpleDirectionGestureDetector;
import com.game.melodi.Input.SwipeHandler;
import com.game.melodi.Input.SwipeTriStrip;
import com.game.melodi.Melodi;
import com.game.melodi.Physics.CollisionDetect;
import com.game.melodi.Points.PointSystem;
import com.game.melodi.Screens.GameOver;
import com.game.melodi.Screens.Menu;
import com.game.melodi.Shaders.BlurShader;
import com.uwsoft.editor.renderer.Overlap2D;

import java.util.Arrays;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.game.melodi.Characters.Elide.MAX_VELOCITY;
import static com.game.melodi.Melodi.player;
import static com.game.melodi.Physics.GameWorld.UNIT_HEIGHT;
import static com.game.melodi.Physics.GameWorld.UNIT_WIDTH;
/**
 * Created by Paradox on 5/21/2017.
 */

public class TerrainV2  {

    FrameBuffer fbo;
    TextureRegion buftxt;
    SpriteBatch batch;

    Background3 img;
    float[] verts,meshverts;
    ShaderProgram shaderProgram;
    Mesh quad;
    private String vshade,fshade;
    private int[] feed;
    int maxPhysics = 20000;
    private float[] normfeed;
    public static boolean start,stop;
    private boolean done = false;
    private boolean dblTap = false;

    Texture groundTexture,surfaceTexture;

    Vector2[] xyPoints;
    Vector2 p1,p2;
    Vector2 pt1,pt2;
    float ymid;
    float ampl;
    float totalSegs;
    float da;
    float dx;
    float hSegments;
    PointSystem points;

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

    public SwipeHandler swipe;
    SimpleDirectionGestureDetector list;
    InputMultiplexer multi;
    boolean fling;

    AudioDevicePlayer2 dplayer;

    private int counter=3;
    private boolean turnOff = false;

    public TerrainV2(AudioDevicePlayer2 dplayer){
        this.dplayer = dplayer;
    }

    /*private void renderFBO(){
        fbo.begin();
        batch.begin();
        //Gdx.gl.glClearColor(0f, 0f, 0f, 0f); //transparent black
        //Gdx.gl.glClear(GL_COLOR_BUFFER_BIT); //clear the color buffer
        batch.setColor(Color.CYAN);
        batch.getColor().a = 0.5f;
        game.r.render();
        game.world.stage.getViewport().getCamera().position.set(elide.getX()+1,elide.getY()+1.6f,0);
        game.world.stage.getViewport().getCamera().update();

        batch.flush();
        fbo.end();
        batch.end();
    }*/

    public void init(Melodi game) {
        this.game = game;

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),false);
        buftxt = new TextureRegion(fbo.getColorBufferTexture());

        points = new PointSystem();
        //img = new Image(game.manager.get("darkcave.png",Texture.class));
        img = new Background3(new TextureRegion(game.manager.get("darkcave.png",Texture.class)));
        img.setBounds(0,0,UNIT_WIDTH,UNIT_HEIGHT);
        game.world.backgroundstage.addActor(img);
        batch = new SpriteBatch();
        elide = new Elide(game.world,game);
        dpad = new Dpad(game,elide.getElideBody(),elide.getBoardBody());
        maxPhysics = Gdx.graphics.getWidth()/20;
        //stageListen();

        this.multi = game.getMulti();

        swipe = new SwipeHandler(25);
        swipe.minDistance = 10;

        tris = new SwipeTriStrip();
        tex = new Texture("glosser.png");

        swipeCheck();

        multi.addProcessor(list);
        multi.addProcessor(swipe);

        chain1 = new ChainShape();
        fixdef = new FixtureDef();

        vshade = Gdx.files.internal("shaders/defaultvertex.glsl").readString();
        fshade = Gdx.files.internal("shaders/defaultfrag.glsl").readString();

        //System.out.println("world stage height + width" + game.world.stage.getHeight() + " " + game.world.stage.getWidth());
        //System.out.println("stage height + width" + game.stage.getHeight() + " " + game.stage.getWidth());

        feed = player.player.getInfo();
        shaderProgram = new ShaderProgram(vshade,fshade);
        normfeed = new float[feed.length];
        for(int i=0;i<normfeed.length;i++){
            normfeed[i]=(float)feed[i];
        }
        for(int i=0;i<normfeed.length-1;i++){
            //this is for the white noise from MP3's, maybe different if not MP3
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

            verts[id++] = (x+=70)/80;
            //verts[id++] = (x+=35)/80; // spreads out the hillys
            verts[id++] = normfeed[i+1]; //adds more hillys
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
        dPadInit();

        //updatePhysics(0,normfeed.length);
        smoothLines(0,normfeed.length);
        generateMESH(normfeed.length-1);
        //System.out.println("Vertices "+ Arrays.toString(verts));
        //System.out.println("chain info " + Arrays.toString(xyPoints));

        //elide.getCharImage().setPosition(getStartingPointA().x+5,getStartingPointA().y+5);
        //elide.getElideBody().setTransform(getStartingPointB().x,getStartingPointB().y,0);
        elide.getElideBody().setTransform(elide.getBoardBody().getPosition().x+3,elide.getBoardBody().getPosition().y+5,0);
    }

    private void trackSync(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                //game.world.cam.position.x += 25;
            }
        },0,3f);
    }

    private void smoothLines(int startIndex, int endIndex){
        EdgeShape shape;
        totalSegs = 40;
        shape = new EdgeShape();
        pt1 = new Vector2();
        pt2 = new Vector2();

        for(int j=startIndex;j<endIndex;j+=5){
            hSegments = (int)Math.floor(pt2.x-pt1.x)/(10);
            //dx = (p2.x - p1.x) / hSegments;
            da = MathUtils.PI / hSegments;

            pt1.set(verts[j], verts[j + 1]);
            pt2.set(verts[j + 5], verts[j + 6]);

            dx = (pt2.x - pt1.x) / totalSegs;
            da = MathUtils.PI / totalSegs;
            ymid = (pt1.y + pt2.y) / 2;
            ampl = (pt1.y - pt2.y) / 2;

            //for(int j=0;j< hSegments+1; ++j) {
            //pt2.x = pt1.x + (j) * (dx);
            pt2.y = ymid + ampl * MathUtils.cos(da * (j));
            shape.set(pt1, pt2);
            game.world.smoothFixtures.add(game.world.body.createFixture(shape, 5));
            //}
        }



    }

    public Vector2 getStartingPointA(){
        return p1.set(verts[0],verts[1]);
    }

    public Vector2 getStartingPointB(){
        return pt1.set(verts[0],verts[1]);
    }

    public float[] getNormfeed(){
        return normfeed;
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
        //System.out.println("LEN OF XY AND NORM " + xyPoints.length + "and " + normfeed.length);

        return xyPoints;
    }

    private void initializeTextures() {
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        surfaceTexture = new Texture(Gdx.files.internal("grass.png"));
        surfaceTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        surfaceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void swipeCheck(){
        list = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onLeft() {
                System.out.println("LEFT");
            }

            @Override
            public void onRight() {
                System.out.println("RIGHT");
                //you may actually swipe right
                //elide.getBoardBody().applyAngularImpulse(1.3f,true);
                elide.getElideBody().setTransform(elide.getX()+1,elide.getY()+1,0);
            }

            @Override
            public void onUp() {
                System.out.println("UP");
                if(elide.getCharImage().isVisible() && elide.getJumpHeight() > 1.5f){
                    elide.showFrontFlip();
                    elide.showFrontBoardFlip();
                    dpad.addScoreFrontFlip();
                }
                System.out.println(elide.getElideBody().getPosition().y);
            }

            @Override
            public void onDown() {
                System.out.println("DOWN");
            }

            @Override
            public void onTap(){
                if(!dblTap) {
                    dblTap = true;
                    //TODO flip elides img
                }else{
                    dblTap = false;
                }
                System.out.println("TAPPED");
            }

            @Override
            public void onPinch(){
                System.out.println("Pinched");
            }

            @Override
            public void onTouchDown(){
                System.out.println("Touchedown");
            }
        });
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
            tempVer[offset+2] = x;      tempVer[offset+3] = 1; // below height // I think UV was 0

            //tempVer[offset+4] = x;      tempVer[offset+5] = y;  // height
            //tempVer[offset+6] = x;      tempVer[offset+7] = y;  // height

            tempVer[offset+4] = (x+=70)/80;      tempVer[offset+5] = -1;  // height //this set the stage look farther down
            //tempVer[offset+4] = (x+=35)/80;      tempVer[offset+5] = -1;  // height //this set the stage look farther down //test diff lengths of stage
            tempVer[offset+6] = x;               tempVer[offset+7] = normfeed[i+1];  // height

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

    private void checkifMoving(){
        if(elide.getBoardBody().isAwake()){
            img.move = true;
        }else{
            img.move = false;
        }
    }

    private void checkIfTrick(){
        if(elide.getTrick() && game.world.getCollision().getConnected()) {
            elide.getBoardBody().setLinearVelocity(0, 0);
            System.out.println("CRASH");
            counter--;
            elide.setTrick(false);
            //constantly goes to 0
            dpad.setLifeNum(counter);
        }
        if(counter==0){
            //GameOver
            gameOver();

        }
    }



    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        float dt = Gdx.graphics.getDeltaTime();
        //gdxBlends();
        multiAct(dt);
        if(!turnOff) {
            //game.r.backrender();
            game.r.render();
        }

        if(!turnOff) {
            shaderProgram.begin();
            shaderProgram.setUniformMatrix("u_projTrans", game.world.stage.getCamera().combined);
            groundTexture.bind(0);
            shaderProgram.setUniformi("u_texture", 0);
            //quad.render(shaderProgram, GL20.GL_TRIANGLES);
            shaderProgram.end();
            game.r.uirender();
        }
        setCamPos();
        swipeInit();
        if(swipe.isDrawing && !dblTap){
            startRunForward();
        }
        if(swipe.isDrawing && dblTap){
            startRunBackward();
        }
        scaleOut();
        checkifMoving(); //parallax checker
        checkIfTrick(); //trick checker
        checkMusic(); //check if music playing

        /*if(counter<=3){
            renderFBO();
            batch.setProjectionMatrix(game.world.stage.getCamera().combined);
            batch.begin();
            batch.draw(buftxt.getTexture(),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            batch.end();
            batch.setProjectionMatrix(game.world.stage.getCamera().combined);

        }*/

    }

    private void gdxBlends(){
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void multiAct(float dt){
        elide.act(dt);
        dpad.act(dt);
        game.world.update(dt);
    }

    private void setCamPos(){
        game.world.stage.getViewport().getCamera().position.set(elide.getX()+1,elide.getY()+1.6f,0);
        game.world.stage.getViewport().getCamera().update();
    }

    private void dPadInit(){

        dpad.getScore().setPosition(Gdx.graphics.getPpcX()-100 ,Gdx.graphics.getHeight()-100);
        dpad.getTime().setPosition(Gdx.graphics.getPpcX()-100 ,Gdx.graphics.getHeight()-200);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dpad.addScore();
            }
        },0,3);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dpad.addTime();
            }
        },0,1.3f);

        /*dpad.getUpImg().setPosition(game.world.stage.getCamera().position.x-1f,game.world.stage.getCamera().position.y-1f);
        dpad.getDownImg().setPosition(game.world.stage.getCamera().position.x-1f,game.world.stage.getCamera().position.y-2f);
        dpad.getRightImg().setPosition(game.world.stage.getCamera().position.x-.5f,game.world.stage.getCamera().position.y-1.5f);
        dpad.getLeftImg().setPosition(game.world.stage.getCamera().position.x-1.5f,game.world.stage.getCamera().position.y-1.5f);

        if(dpad.getTouchUp()){
            elide.getElideBody().applyForceToCenter(0,100,true);
            elide.getBoardBody().applyForceToCenter(0,100,true);
        }

        if(dpad.getTouchDown()){
            //elide.getElideBody().applyForceToCenter(0,-100,true);
            //elide.getBoardBody().applyForceToCenter(0,-100,true);
            elide.getBoardBody().applyForce(5,-40,0,0,true);
            elide.getElideBody().applyForce(5,-40,0,0,true);
            System.out.println("DDD");
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
        }*/

    }

    private void swipeInit(){

        tris.endcap = 35f;
        //the thickness of the line
        tris.thickness = 80f;
        //generate the triangle strip from our path
        tris.update(swipe.path());
        //the vertex color for tinting, i.e. for opacity
        tris.color = Color.WHITE;
        //render the triangles to the screen
        tex.bind();

        if(!stop)
        tris.draw(game.stage.getCamera());
        //System.out.println(tris.tristrip);
    }

    private void startRunForward(){
        if(elide.getElideBody().getLinearVelocity().x <= MAX_VELOCITY && !fling){
            elide.getBoardBody().applyLinearImpulse(.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            //elide.getElideBody().applyTorque(-650,true);
            elide.getElideBody().setFixedRotation(true);
        }
    }

    private void startRunBackward(){
        if(elide.getElideBody().getLinearVelocity().x <= MAX_VELOCITY && !fling){
            elide.getBoardBody().applyLinearImpulse(-.90f,0,elide.getElideBody().getPosition().x,elide.getElideBody().getPosition().y,true);
            //elide.getElideBody().applyTorque(-650,true);
            elide.getElideBody().setFixedRotation(true);
        }
    }

    private void scaleOut(){

        if(elide.getElideBody().getPosition().y > (game.world.stage.getCamera().viewportHeight * 3/4) && elide.getJumpHeight() >= 4){
            ((OrthographicCamera)game.world.stage.getCamera()).zoom += 0.005f;
        }
        else if(((OrthographicCamera)game.world.stage.getCamera()).zoom > 1){
            ((OrthographicCamera)game.world.stage.getCamera()).zoom -= 0.015f;
        }
        //((OrthographicCamera)game.world.stage.getCamera()).zoom += 0.001f;
    }

    private void checkMusic(){
        if(game.player.getMusicStopped()){
            gameOver();
        }
    }

    private void gameOver(){
        elide.getCharImage().addAction(Actions.moveBy(0,-5,3,Interpolation.exp5));
        elide.getBoardImage().addAction(Actions.moveBy(0,-5,3, Interpolation.exp5));
        turnOff = true;

        if(!done) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dplayer.dispose();
                    dispose();
                    game.setScreen(new GameOver(game, dpad.getTotalScore()));
                    done=true;
                }
            }, 2, 0, 1);
        }
    }

    public void dispose(){

    }

}
