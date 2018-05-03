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
import com.game.melodi.Input.MultiInputListener;
import com.game.melodi.Melodi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 6/6/2017.
 */

public class SongList {

    private Table table,table2;
    private Array<TextButton> buttons;
    private HashMap<Integer,TextButton> butons;
    private Array<String> songnames,datanames;
    private String songname;
    private FileHandle[] files;
    private TextField titlefield;
    private Array<FileHandle> handles;
    private FileHandle begin;
    Melodi game;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private TextButton txtbutton;
    private TextField.TextFieldStyle style;
    private TextButton.TextButtonStyle stylebutton;
    private ScrollPane scroll;
    int i;
    boolean empty=false;
    FileHandle file;
    List<String> musicList;
    List<String> dataList;
    String mlist;
    MultiInputListener multiInp;
    boolean wasDragged = false;
    float distance1, distance2;

    public SongList(Melodi game){
        this.game = game;
        table = new Table();
        handles = new Array<>();
        buttons = new Array<>();
        songnames = new Array<>();
        datanames = new Array<>();
        scroll = new ScrollPane(table);
        scroll.setSmoothScrolling(true);
        musicList = game.getNameList();
        dataList = game.getDataList();
        butons = new HashMap<Integer,TextButton>();

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
        setSkin();
        getSongs();
        setTable2();

        /*files = begin.list();

        try{
            begin = Gdx.files.absolute(files[0].path());
        }catch (ArrayIndexOutOfBoundsException e){
            empty = true;
            songnames.add("This folder is empty!");
        }

        //files = begin.list();


        getHandles();
        setTable();*/

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

    private void getSongs(){
        for(String s: musicList){
            songnames.add(s);
        }

        for(String s: dataList){
            datanames.add(s);
            System.out.println(s);
        }
    }

    private void setTable(){

        txtbutton = new TextButton(songname,stylebutton);

        if(!empty) {
            for (i = 0; i < songnames.size; i++) {
                txtbutton = new TextButton(songnames.get(i), stylebutton);
                txtbutton.setSize(300,300);
                txtbutton.addListener(new InputListener() {
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
                        //player.startPlaying2(songnames.get(i-1));
                        //game.setScreen(new Loader(game,file));
                    }
                });
                buttons.add(txtbutton);
            }
        }
        table.setSize(txtbutton.getWidth(),txtbutton.getHeight());
        scroll.setSize(txtbutton.getWidth(),txtbutton.getHeight()*3);

    }

    private void setTable2(){

        //button = new TextButton(songname,stylebutton);
        //table.setSize(txtbutton.getWidth(),txtbutton.getHeight());
        scroll.setSize(600,900);
        scroll.setScrollingDisabled(true,false);

        if(!empty) {
            for (i = 0; i < musicList.size()-1; i++) {
                txtbutton = new TextButton(songnames.get(i), stylebutton);
                txtbutton.addListener(new MultiInputListener(i,txtbutton,musicList.size()) {
                    @Override
                    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event,x,y,pointer,button);
                        file = Gdx.files.absolute(dataList.get(this.getKey()));
                        //file.copyTo(Gdx.files.local(""));
                        //file = Gdx.files.local(songnames.get(i-1));
                        //System.out.println(this.getKey());
                        wasDragged = false;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        //super.touchUp(event, x, y, pointer, button);
                        if(!wasDragged){
                            player.startPlaying2(dataList.get(this.getKey()));
                            game.setScreen(new Loader(game,file));
                            dispose();

                        }
                    }
                });
                buttons.add(txtbutton);
            }
        }

    }

    public Table getSongList(){
        for(int i=0;i<buttons.size;i++){
            table.add(buttons.get(i)).fill();
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

        style.font.getData().setScale(.5f);

        titlefield = new TextField("Please select a title",style);

        stylebutton = new TextButton.TextButtonStyle(); //** Button properties **//
        stylebutton.up = buttonSkin.getDrawable("buttonOff");
        stylebutton.down = buttonSkin.getDrawable("buttonOn");

        stylebutton.font = font;


    }

    public void dispose(){
        buttons.clear();
        buttonsAtlas.dispose();
        buttonSkin.dispose();
        scroll.clear();
        table.clear();
        txtbutton.clear();

    }

}
