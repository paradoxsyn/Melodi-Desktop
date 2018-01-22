package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Animations.AnimatedImage2;
import com.game.melodi.Audiostream.File;
import com.game.melodi.Loading.Loader;
import com.game.melodi.Loading.SongList;
import com.game.melodi.Maps.TmapGen;
import com.game.melodi.Melodi;
import com.game.melodi.Scrolling.TableScroll;

import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 5/19/2017.
 */

public class SongSelect extends ScreenAdapter {

    Melodi game;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private TextButton button;
    private TextField titlefield;
    private TextField.TextFieldStyle style;
    private String title;
    private SongList slist;
    private Table table;
    private ScrollPane scrollTable;

    private TextureAtlas caveAtlas;
    private Image furry;
    private Animation<TextureAtlas.AtlasRegion> caveanim;
    private AnimatedImage2 cave;
    FileHandle file;

    private int width, height;

    InputMultiplexer input;

    public SongSelect(Melodi game){
        this.game = game;
        //input = new InputMultiplexer();
        slist = new SongList(game);
        setTable();

        //input.addProcessor(game.stage);
        //Gdx.input.setInputProcessor(input);

    }

    private void loadSongUI(){
        furry = new Image(new Texture("elideback.png"));
        caveAtlas = new TextureAtlas("caveanim/cave.txt");
        caveanim = new Animation<TextureAtlas.AtlasRegion>(0.20f,caveAtlas.findRegions("cave"));
        cave = new AnimatedImage2(caveanim);
        cave.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        furry.setPosition(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/4);
        furry.setSize(200,200);
    }

    private void setTable(){
        /*table = new Table();
        table.add(button);
        table.setPosition(Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-200);*/
        table = slist.getSongList();
        scrollTable = slist.getScroll();


        //table.setOrigin(Gdx.graphics.getWidth()-350,95);
        //table.setPosition(Gdx.graphics.getWidth()-350,Gdx.graphics.getHeight()-500);
        //scrollTable.setOrigin(Gdx.graphics.getWidth()-450,95);
        scrollTable.setPosition(Gdx.graphics.getWidth()/1.7f,Gdx.graphics.getHeight()/3);


    }

    public void show(){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        loadSongUI();

        style = new TextField.TextFieldStyle();
        buttonsAtlas = new TextureAtlas("button.pack"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
        fontParameter.size = 54;
        font = fontGenerator.generateFont(fontParameter);

        style.font = font;
        style.fontColor = Color.WHITE;

        titlefield = new TextField("Please select a title",style);

        TextButton.TextButtonStyle stylebutton = new TextButton.TextButtonStyle(); //** Button properties **//
        stylebutton.up = buttonSkin.getDrawable("buttonOff");
        stylebutton.down = buttonSkin.getDrawable("buttonOn");

        stylebutton.font = font;

        button = new TextButton("Load elise", stylebutton);

        button.addListener(new InputListener() {
                               public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                   Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
                                   return true;

                               }

                               public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                   Gdx.app.log("my app", "Rggggggeleased");

                                   //load song selection
                                   player.startPlaying("furelise.mp3");
                                   title = player.player.getTitle();
                                   file = Gdx.files.internal(title);
                                   dispose();
                                   game.setScreen(new Loader(game,file));


                               }
                           });

        titlefield.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

        game.stage.addActor(cave);
        //game.stage.addActor(table);
        game.stage.addActor(button);
        game.stage.addActor(scrollTable);
        //game.stage.addActor(titlefield);
        game.stage.addActor(furry);

    }

    public void render(float dt){
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 1, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        titlefield.setText(title);


        game.stage.draw();
        game.stage.act(dt);

    }


    public void dispose(){
        game.stage.clear();
        buttonsAtlas.dispose();
        buttonSkin.dispose();
        caveAtlas.dispose();
        //game.stage.dispose();
    }

    public void resize(int width, int height){
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        game.stage.getViewport().update(width, height, true);
    }
}
