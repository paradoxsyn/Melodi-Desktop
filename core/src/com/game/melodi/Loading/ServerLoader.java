package com.game.melodi.Loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Melodi;
import com.game.melodi.Networking.ServerStart;
import com.game.melodi.Screens.Menu;

/**
 * Created by RabitHole on 1/11/2018.
 */

public class ServerLoader extends ScreenAdapter{

    Melodi game;
    ServerStart server;
    Window window;
    String connect = "Connecting...";
    Label loading;
    private Window.WindowStyle wstyle;
    private Skin metalskin; //** images are used as skins of the button **//

    public ServerLoader(Melodi game){
        this.game = game;
        init();
    }

    public void init(){
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));
        //wstyle = new Window.WindowStyle(new BitmapFont(), Color.CORAL, });
        //window = new Window(connect,metalskin);
        //window.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        //window.setSize(400,200);
        //game.stage.addActor(window);
        //server = new ServerStart();


    }

    public void finishLoad(){
        //dispose();
        game.setScreen(new Menu(game));
    }

    @Override
    public void resize(int width, int height) {
        game.viewPort.update(width, height, true);
        game.stage.getCamera().update();
    }

    @Override
    public void render(float delta){
        //elapsedTime += Gdx.graphics.getDeltaTime();
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.stage.act();
        game.stage.draw();

        if(server.isLoaded()) {
            finishLoad();
        }
        else if(server.isOffline()){
            finishLoad();
        }



    }

    public void dispose(){

    }
}
