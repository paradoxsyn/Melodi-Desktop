package com.game.melodi.Loading;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.game.melodi.Melodi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 6/6/2017.
 */

public class SongList {

    private Table table,table2;
    private Array<TextButton> buttons;
    private Array<String> songnames;
    private String songname;
    private Texture[] texIDs;
    private Image[] imgIDs;
    private FileHandle[] files;
    private Array<FileHandle> handles;
    private FileHandle begin;
    Melodi game;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private TextButton button;
    private TextField titlefield;
    private TextField.TextFieldStyle style;
    private String title;
    private TextButton.TextButtonStyle stylebutton;
    private ScrollPane scroll;
    int i;
    boolean empty=false;
    FileHandle file;

    public SongList(Melodi game){
        this.game = game;
        table = new Table();
        handles = new Array<>();
        buttons = new Array<>();
        songnames = new Array<>();
        scroll = new ScrollPane(table);
        scroll.setSmoothScrolling(true);

        boolean isLocAvailable = Gdx.files.isExternalStorageAvailable();
        String locRoot = Gdx.files.getExternalStoragePath();
        String path = game.extPath.getAbsolutePath();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            begin = Gdx.files.absolute(path+"/Music");
            //begin = Gdx.files.external("/0");
        }
        else if(Gdx.app.getType() == Application.ApplicationType.iOS){
            //begin equals what
        }
        else if(Gdx.app.getType() == Application.ApplicationType.WebGL){
            //doesnt have standard file structure
        }
        else {
            begin = Gdx.files.internal("./bin/data");
        }

        files = begin.list();

        try{
            begin = Gdx.files.absolute(files[0].path());
        }catch (ArrayIndexOutOfBoundsException e){
            empty = true;
            songnames.add("This folder is empty!");
        }

        //files = begin.list();

        setSkin();
        getHandles();
        setTable();

        //System.out.println(files.length);
        //System.out.println(handles);
        //System.out.println(files[0]);

    }

    private void getHandles(){
        for(FileHandle f: files){
            if(f.isDirectory()){
                Gdx.app.log("Loop","It's a folder");
                //Dont want to display this

            }
            else{
                Gdx.app.log("Loop","File");
                System.out.println("Added");
                handles.add(f);
                songname = f.name();
                songnames.add(songname);


            }
        }
    }

    private void setTable(){

        button = new TextButton(songname,stylebutton);

        if(!empty) {
            for (i = 0; i < songnames.size; i++) {
                button = new TextButton(songnames.get(i), stylebutton);
                button.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        //return super.touchDown(event, x, y, pointer, button);
                        file = Gdx.files.external("/Music/"+songnames.get(i-1));
                        //file.copyTo(Gdx.files.local(""));
                        //file = Gdx.files.local(songnames.get(i-1));
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        //super.touchUp(event, x, y, pointer, button);
                        player.startPlaying2(songnames.get(i-1));
                        game.setScreen(new Loader(game,file));
                    }
                });
                buttons.add(button);
            }
        }
        table.setSize(button.getWidth()*2,button.getHeight());
        scroll.setSize(button.getWidth()*2,button.getHeight()*3);

    }

    public Table getSongList(){
        for(int i=0;i<buttons.size;i++){
            table.add(buttons.get(i));
            table.row();
        }
        return table;
    }

    public ScrollPane getScroll(){
        return scroll;
    }

    private void setSkin(){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        style = new TextField.TextFieldStyle();
        buttonsAtlas = new TextureAtlas("button.pack"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
        fontParameter.size = 54;
        font = fontGenerator.generateFont(fontParameter);

        style.font = font;
        style.fontColor = Color.WHITE;

        titlefield = new TextField("Please select a title",style);

        stylebutton = new TextButton.TextButtonStyle(); //** Button properties **//
        stylebutton.up = buttonSkin.getDrawable("buttonOff");
        stylebutton.down = buttonSkin.getDrawable("buttonOn");

        stylebutton.font = font;

        //button = new TextButton("Load elise", stylebutton);

    }

}
