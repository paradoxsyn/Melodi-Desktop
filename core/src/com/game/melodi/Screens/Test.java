package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.game.melodi.Audiostream.AudioDevicePlayer2;
import com.game.melodi.Audiostream.AudioInputStreamFactory;
import com.game.melodi.Audiostream.File;
import com.game.melodi.Audiostream.FileHandleInputStreamFactory;
import com.game.melodi.Audiostream.MP3InputStreamFactory;
import com.game.melodi.Characters.Elide;
import com.game.melodi.Input.SimpleDirectionGestureDetector;
import com.game.melodi.Input.SwipeHandler;
import com.game.melodi.Maps.Grind;
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

    int width,height=300;
    public com.badlogic.gdx.graphics.Color color = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.WHITE);
    public com.badlogic.gdx.graphics.Color color2 = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.GREEN);
    public com.badlogic.gdx.graphics.Color color3 = new com.badlogic.gdx.graphics.Color(com.badlogic.gdx.graphics.Color.GOLD);

    ImmediateModeRenderer20 r;
    TerrainV2 t2;
    SpriteBatch batch;
    AudioDevicePlayer2 dplayer;
    Grind g;


    public Test(final Melodi game, AudioDevicePlayer2 dplayer){
        this.game = game;
        r = new ImmediateModeRenderer20(false,true,0);
        batch = new SpriteBatch();
        this.dplayer = dplayer;
        t2 = new TerrainV2(dplayer);

        t2.init(game);
        g = new Grind(game,30,50);
        //g.init();
        g.addFrontRamp(1,1);


    }

    public void play(){
        LevelMap map = getTheme();
        game.playerstream.play(map,false,false);
        System.out.println(map.timingPointsToString());
    }

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
        dplayer.dispose();
    }

    @Override
    public void hide(){

    }

    @Override
    public void show(){
    }

    @Override
    public void pause(){
        dplayer.pause();
    }

    @Override
    public void resume(){
        dplayer.resume();
    }

    public void resize(int width, int height){
        //game.viewPort.update(x,y);
        //screenWidth = width;
        //screenHeight = height;
        //aspectRatio = (float) screenWidth / (float) screenHeight;
        //camHeight = 1.0f;
        //camWidth = 1.0f * aspectRatio;

        //game.stage.getCamera().position.set(camWidth/2f,camHeight/2f,0);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        game.world.stage.getViewport().update(width, height,true);
        game.world.uistage.getViewport().update(width, height,true);
        //game.world.stage.getCamera().position.set(.5f,1f*(width/height),0);
    }
}
