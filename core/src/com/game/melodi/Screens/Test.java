package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Animations.Background;
import com.game.melodi.Audiofullread.MusicFilePlayer;
import com.game.melodi.Audiofullread.MusicPlayer;
import com.game.melodi.Audiostream.File;
import com.game.melodi.Characters.Elide;
import com.game.melodi.Maps.LevelMap;
import com.game.melodi.Maps.Terrain;
import com.game.melodi.Maps.TerrainV2;
import com.game.melodi.Maps.TerrainV3;
import com.game.melodi.Maps.TimingPoint;
import com.game.melodi.Maps.TmapParse;
import com.game.melodi.Melodi;
import com.game.melodi.Physics.GameWorld;
import com.game.melodi.Shaders.FragShader;
import com.game.melodi.Shaders.VertShader;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javazoom.jl.decoder.JavaLayerException;

import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 4/28/2017.
 */

public class Test implements Screen {

    final Melodi game;
    float elapsedTime;
    //Image img;
    Background bg;
    int[] highfreq = null;
    float speed = 5;
    float xPos=0;
    private final int songLen = player.player.getInfo().length;
    private final int groundStep = 20;

    private int screenWidth,screenHeight;
    private float camWidth,camHeight,aspectRatio;

    Terrain t;
    float[] terrain;
    int x,y=500;
    int width,height=300;
    public com.badlogic.gdx.graphics.Color color = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.WHITE);
    public com.badlogic.gdx.graphics.Color color2 = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.GREEN);
    public com.badlogic.gdx.graphics.Color color3 = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.GOLD);

    ImmediateModeRenderer20 r;
    TerrainV2 t2;
    TerrainV3 t3;
    Vector2[] xyPoints;

    int hillVerts;
    int numIndices;
    int maxPhysics;

    public Mesh mesh;
    public ShaderProgram shader;
    private String vshade,fshade;

    private final int maxHillVerts = 40000;

    Vector2[] groundVerts;
    Vector2[] groundTexCoords;
    Vector2[] grassVerts;
    Vector2[] grassTexCoords;

    Sprite grassSprite;
    Sprite groundSprite;

    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    GameWorld world;

    TmapParse tmaparse;

    public Test(final Melodi game){
        this.game = game;
        r = new ImmediateModeRenderer20(false,true,0);
        batch = new SpriteBatch();

        System.out.println("TEST IS NOW ON");
        Gdx.input.setInputProcessor(game.world.stage);

        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        //t = new Terrain(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),Gdx.graphics.getHeight()/4,0.5f);
        //terrain = t.getTerrain();
        t2 = new TerrainV2();
        //t3 = new TerrainV3();
        t2.init(game);
        groundSprite = new Sprite(new Texture(Gdx.files.internal("ground.png")));
        grassSprite = new Sprite(new Texture(Gdx.files.internal("grass.png")));
        //t3.init();
        xyPoints = new Vector2[20000];
        groundVerts = new Vector2[maxHillVerts];
        groundTexCoords = new Vector2[maxHillVerts];
        grassVerts = new Vector2[maxHillVerts];
        grassTexCoords = new Vector2[maxHillVerts];
        //generateXY();
        //generateMESH();
        //maybeGL();
        /*for(int i=0;i<t3.xyPoints.length;i++){
            v = new Vector2[i];
            v[i] = t3.xyPoints[i];
        }*/
        //tmaparse = new TmapParse(game);

    }

    public Vector2[] generateXY(){
        float yOffset = 350f;
        int index = 0;
        for(int i = 0; i < songLen; i++){
            //xyPoints[i].set(index,yOffset - MathUtils.sin(i) * 60 * i / 100.f);
            xyPoints[i] = new Vector2();
            xyPoints[i].x = index;
            yOffset = player.player.getInfo()[i];
            xyPoints[i].y = yOffset - MathUtils.sin(i) * 30  * i / 6;
            index+=groundStep;
        }

        return xyPoints;
    }

    public void generateMESH(){
        hillVerts=0;
        //Vector2[] p = xyPoints.clone();
        for(int i=0;i<maxHillVerts;i++){
            grassVerts[i] = new Vector2();
            groundVerts[i] = new Vector2();
            grassTexCoords[i] = new Vector2();
            groundTexCoords[i] = new Vector2();
        }

        for(int i=0;i<songLen;i++){

            //p[i] = xyPoints[i];
            //p[i] = new Vector2();
            grassVerts[hillVerts].set(xyPoints[i].x,xyPoints[i].y-12f);
            //grassVerts[hillVerts].y = xyPoints[i].y-12f;
            grassTexCoords[hillVerts++].set(xyPoints[i].x / grassSprite.getWidth(),1f);
            //grassTexCoords[hillVerts].y = 1f;
            grassVerts[hillVerts].set(xyPoints[i].x,xyPoints[i].y);
            //grassVerts[hillVerts].y = xyPoints[i].y;
            grassTexCoords[hillVerts++].set(xyPoints[i].x/grassSprite.getWidth(),0);
            //grassTexCoords[hillVerts].y = 0f;
        }
        System.out.println("GRASS "+ Arrays.toString(grassVerts) + "\n" + "T " + Arrays.toString(grassTexCoords));
        hillVerts=0;


        for(int i = 0;i<songLen;i++) {
            //p[i] = xyPoints[i];
            groundVerts[hillVerts].set(xyPoints[i].x,0);
            //groundVerts[hillVerts].y = 0;
            groundTexCoords[hillVerts++].set(xyPoints[i].x / groundSprite.getWidth(),0);
            //groundTexCoords[hillVerts++].y = 0;
            groundVerts[hillVerts].set(xyPoints[i].x,xyPoints[i].y-5f);
            //groundVerts[hillVerts].y = xyPoints[i].y - 5f;
            groundTexCoords[hillVerts++].set(xyPoints[i].x / groundSprite.getWidth(),xyPoints[i].y / groundSprite.getHeight());
            //groundTexCoords[hillVerts++].y = xyPoints[i].y / groundSprite.getHeight();
        }
    }

    public void maybeGL(){
        //bind
        float[] vertices = new float[maxHillVerts];
        vshade = Gdx.files.internal("shaders/defaultvertex.glsl").readString();
        fshade = Gdx.files.internal("shaders/defaultfrag.glsl").readString();
        shader = new ShaderProgram(vshade,fshade);
        mesh = new Mesh( true, maxHillVerts, 6,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ));
        for(int i =0;i<maxHillVerts;i++){
            vertices[i] = grassVerts[i].x;
            vertices[i++] = grassVerts[i].y;
            //vertices[i] = grassTexCoords[i].x;
            //vertices[i++] = grassTexCoords[i].y;
        }
        //mesh.setVertices(vertices);
        //mesh.setIndices(new short[]{0,2,3,1,2,2});
        //System.out.println(Arrays.toString(mesh.getVertices(vertices)));
    }

    public void play(){
        LevelMap map = getTheme();
        game.playerstream.play(map,false,false);
        System.out.println(map.timingPointsToString());
    }

        /*highfreq = numbers;
        for(int i=0;i<highfreq.length;i++){
            if(highfreq[i]>20000){
                System.out.println("High somethin");
                System.out.println(highfreq[i]);*/


    public static LevelMap getTheme(){
        String test = "furelise.mp3,Rainbows,Kevin MacLeod,219350";
        String[] tokens = test.split(",");
        if(tokens.length  != 4){
            return null;
        }

        LevelMap map = new LevelMap(null);

        /** The theme song timing point string (for computing beats to pulse the logo) . */
        String themeTimingPoint = "1120,545.454545454545,4,1,0,100,0,0";

        map.audioFilename = new File(tokens[0]);
        map.title = tokens[1];
        map.artist = tokens[2];
        try{
            map.endTime = Integer.parseInt(tokens[3]);
        } catch (NumberFormatException e) {
            return null;
        }

        try {
            map.timingPoints = new ArrayList<>(1);
            map.timingPoints.add(new TimingPoint(themeTimingPoint));
        } catch (Exception e) {
            return null;
        }

        return map;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        //Gdx.gl20.glEnable(GL20.GL_BLEND);
        //Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //float pos = game.player.getPosition();
        //System.out.println(game.player.getPosition());

        //System.out.println("Audio file info for full read: " + fileplay.getFrameNumber() + " " + fileplay.getLength());
        //System.out.println(Arrays.toString(fileplay.getInfo()));

        /*game.test.setAutoShapeType(true);
        game.test.begin(ShapeRenderer.ShapeType.Line);
        game.test.begin();
        if(!game.player.getEnd()){
            game.camera.position.x = game.camera.position.x + speed;
            elapsedTime += Gdx.graphics.getDeltaTime();
            float endTime = songlen - game.camera.position.x;
            System.out.println("The track ends in: " + game.player.getEnd());
            wavyLine();
        }
        game.test.line(Gdx.graphics.getWidth()/2,0,0,linep1,Color.BLUE,Color.CORAL);
        game.test.end();*/
        //game.test.setAutoShapeType(true);
        /*game.test.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0;i<songLen;i++) {
            game.test.point(xyPoints[i].x,xyPoints[i].y,0);
        }
        game.test.end();*/
        t2.render(); //this is the main current form
        //batch.begin();
        //batch.setShader(shaderProgram);
        //batch.draw(sprite,sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
        //batch.end();
        //img.bind();
        //shader.begin();
        //shader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
        //shader.setUniformi("u_texture", 0);
        //mesh.render(shader, GL20.GL_TRIANGLES);
        //shader.end();
        //game.stage.draw();
        //game.stage.act(delta);

        //tilemap render



    }

    @Override
    public void dispose(){

    }

    @Override
    public void hide(){

    }

    @Override
    public void show(){

    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void resize(int width, int height){
        /*//game.viewPort.update(x,y);
        screenWidth = width;
        screenHeight = height;
        aspectRatio = (float) screenWidth / (float) screenHeight;
        camHeight = 1.0f;
        camWidth = 1.0f * aspectRatio;

        game.stage.getCamera().position.set(camWidth/2f,camHeight/2f,0);*/
    }
}
