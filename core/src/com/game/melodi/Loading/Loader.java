package com.game.melodi.Loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.melodi.Audiostream.AudioDevicePlayer2;
import com.game.melodi.Audiostream.File;
import com.game.melodi.Audiostream.FileHandleInputStreamFactory;
import com.game.melodi.Audiostream.MP3InputStreamFactory;
import com.game.melodi.Input.MyTextInputListener;
import com.game.melodi.Maps.TmapGen;
import com.game.melodi.Maps.TmapParse;
import com.game.melodi.Melodi;
import com.game.melodi.Screens.Test;

import java.io.IOException;

import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 3/5/2017.
 */

public class Loader extends ScreenAdapter {
    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;

    private Melodi game;
    MyTextInputListener popup;
    String title;

    private TmapParse tmappar;
    private TmapGen tmap;
    FileHandle file;
    AudioDevicePlayer2 dplayer;


    public Loader(Melodi game, FileHandle file){
        //super(game);
        this.game = game;
        this.file = file;
        game.manager = new AssetManager();
        try {
            dplayer = new com.game.melodi.Audiostream.AudioDevicePlayer2(
                    new com.game.melodi.Audiostream.MP3InputStreamFactory(
                            new FileHandleInputStreamFactory(file)
                    ), file.path());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void loadSongMenu(){
        /*TextureAtlas songSelect;
        Animation<TextureAtlas.AtlasRegion> songSelectAnim;
        Image elideBack;
        AnimatedImage2 cave;

        songSelect = new TextureAtlas("caveanim/cave.txt");
        songSelectAnim = new Animation<TextureAtlas.AtlasRegion>(1.5f,songSelect.findRegions("cave"));
        elideBack = new Image(new Texture("elideback.png"));
        cave = new AnimatedImage2(songSelectAnim);*/

        //game.manager.load("caveanim/cave.txt", TextureAtlas.class);
        //game.manager.load("elideback.png",Texture.class);
    }

    public void loadGameAssets(){
        //Image elide,board;
        game.manager.load("elideonboard.png", Texture.class);
        game.manager.load("boardmove.png", Texture.class);
        game.manager.load("background.png", Texture.class);
        game.manager.load("dpadup.png", Texture.class);
        game.manager.load("dpaddown.png", Texture.class);
        game.manager.load("dpadleft.png", Texture.class);
        game.manager.load("dpadright.png", Texture.class);
        game.manager.load("darkcave.png", Texture.class);
        game.manager.load("lifeimg.png", Texture.class);
        game.manager.load("backflipanim/flip.txt",TextureAtlas.class);
        game.manager.load("boardfrontflipanim/boardflip.png", Texture.class);
        game.manager.load("boardfrontflipanim/boardflip.txt",TextureAtlas.class);
        game.manager.load("elidefallanim/fall.txt",TextureAtlas.class);
        game.manager.load("boardmovekickflipanim/boardmovekickflip.txt",TextureAtlas.class);
        game.manager.load("boardmovekickflipanim/boardmovekickflip.png",TextureAtlas.class);
        game.manager.load("boardmoveshuffleanim/boardmoveshuffle.txt",TextureAtlas.class);
        game.manager.load("boardmoveshuffleanim/boardmoveshuffle.png",TextureAtlas.class);
        game.manager.load("elideboardmovementanim/elideboardmovement.txt",TextureAtlas.class);
        game.manager.load("elideboardmovementanim/elideboardmovement.png",TextureAtlas.class);
        game.manager.load("elidecorkscrewanim/elidecorkscrew.txt",TextureAtlas.class);
        game.manager.load("elidecorkscrewanim/elidecorkscrew.png",TextureAtlas.class);


    }

    public void popup(){

        popup = new MyTextInputListener();
        Gdx.input.getTextInput(popup,"Song map", "","Enter a song map name");
    }

    @Override
    public void show() {
        // Tell the manager to load assets for the loading screen
        game.manager.load("loading.pack", TextureAtlas.class);
        // Wait until they are finished loading
        game.manager.finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage();
        //loadSongMenu();
        loadGameAssets();
        // Get our textureatlas from the manager
        TextureAtlas atlas = game.manager.get("loading.pack", TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        Animation anim = new Animation<TextureRegion>(0.05f, atlas.findRegions("loading-bar-anim") );
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        // loadingBar = new Image(atlas.findRegion("loading-bar1"));
        stage.getRoot().setTouchable(Touchable.disabled);
        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        // Add everything to be loaded, for instance:
        // game.manager.load("data/assets1.pack", TextureAtlas.class);
        // game.manager.load("data/assets2.pack", TextureAtlas.class);
        // game.manager.load("data/assets3.pack", TextureAtlas.class);


        game.manager.finishLoading();

    }

    @Override
    public void resize(int width, int height) {
        // Set our screen to always be XXX x 480 in size
        //width = 480 * width / height;
        //height = 480;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        Viewport v = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),stage.getCamera());
        stage.setViewport(v);
        v.update(width,height);
        v.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (game.manager.update()) { // Load some, will return true if done loading
            //if (Gdx.input.isTouched()) { // If the screen is touched after the game is done loading, go to the main menu screen
           //game.setScreen(new LosuScrn(game));
        }

        // Interpolate the percentage to make it more smooth
        //percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);
        if(!player.player.isLoaded) {
            percent = Interpolation.linear.apply(percent, player.player.getFeedPosition(), 0.1f);
        }else{
            System.out.println("finished loading, load lvl");
            game.setScreen(new Test(game,dplayer));
            title = player.player.getTitle();
            System.out.println(file.toString());
            System.out.println(file.path());
            //this is playing a seperate thread from the actual loaded song data
            //game.playerstream.loadTrack(new File("furelise.mp3"),0,false);
            //game.playerstream.loadTrack(new File("jetsetrun.mp3"),0,false);
            //game.playerstream.loadTrack(new File(file.toString()),0,false);
            dplayer.play();
            //System.out.println("LLLLLLL? " + game.playerstream.getTotalLength());
        }
        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();
    }

    public void genMap(){
        title = player.player.getTitle();
        //tmap = new TmapGen(game.playerstream.getTotalLength(),title);
        tmappar = new TmapParse(game);
    }


    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
        game.manager.unload("loading.pack");
        dispose();
    }

    public void dispose(){

        game.stage.clear();

        //game.stage.dispose();

    }





}
