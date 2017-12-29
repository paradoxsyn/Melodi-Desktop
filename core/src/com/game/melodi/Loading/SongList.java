package com.game.melodi.Loading;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
        String test = game.extPath.getAbsolutePath();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            begin = Gdx.files.absolute("/storage");
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
        //SongList(Gdx.file.external("/");
        setSkin();
        getHandles();
        setTable();
        begin = Gdx.files.absolute(files[0].path());
        files = begin.list();
        System.out.println(files.length);
        System.out.println(handles);
        //System.out.println(files[0]);

    }

    private void getHandles(){
        for(FileHandle f: files){
            if(f.isDirectory()){
                Gdx.app.log("Loop","It's a folder");
                /*for(int i=0;i<files.length;i++){

                }*/
                songname = f.name();
                songnames.add(songname);
                songnames.add(songname);
                songnames.add(songname);
                songnames.add(songname);

            }
            else{
                Gdx.app.log("Loop","File");
                System.out.println("Added");
                handles.add(f);
            }
        }
    }

    private void setTable(){
        button = new TextButton(songname,stylebutton);
        for(int i=0;i<songnames.size;i++){
            button = new TextButton(songnames.get(i),stylebutton);
            buttons.add(button);
        }
        table.setSize(button.getWidth(),button.getHeight());
        scroll.setSize(button.getWidth(),button.getHeight()*3);

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
