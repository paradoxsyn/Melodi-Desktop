package com.game.melodi.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.game.melodi.Melodi;

import static com.game.melodi.Physics.GameWorld.UNIT_HEIGHT;
import static com.game.melodi.Physics.GameWorld.UNIT_WIDTH;

/**
 * Created by RabitHole on 1/5/2018.
 */

public class Dpad extends Image {

    Image up,down,left,right;
    Melodi game;
    Body elide,board;
    Label score,time,lifeNumber;
    int totalScore, totalTime=0;
    public ShapeRenderer debug;
    private boolean holdup,holddown,holdleft,holdright;
    private Label.LabelStyle lstyle;
    private BitmapFont font;
    private Skin metalskin; //** images are used as skins of the button **//
    Image life;
    private int lifeNum=3;


    public Dpad(Melodi game,Body elide, Body board){
        this.game = game;
        this.elide = elide;
        this.board = board;

        debug = new ShapeRenderer();
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pacifico.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 50;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        font = fontGenerator.generateFont(fontParameter);
        //font.getData().setScale(1);
        lstyle = metalskin.get(Label.LabelStyle.class);
        lstyle.font = font;
        lstyle.fontColor.set(Color.WHITE);

        life = new Image(game.manager.get("lifeimg.png",Texture.class));
        life.setSize(150,150);
        life.setPosition(Gdx.graphics.getWidth()/2-500,Gdx.graphics.getHeight()-200);

        score = new Label("Score: " + totalScore,lstyle);
        time = new Label("Time: " + totalTime,lstyle);
        lifeNumber = new Label(" x"+lifeNum,lstyle);
        lifeNumber.setPosition(life.getX()+100,life.getY());

        //score.setSize(1,1);


        /*up = new Image(game.manager.get("dpadup.png",Texture.class));
        down = new Image(game.manager.get("dpaddown.png",Texture.class));
        left = new Image(game.manager.get("dpadleft.png",Texture.class));
        right = new Image(game.manager.get("dpadright.png",Texture.class));

        up.setSize(.35f,.35f);
        down.setSize(.35f,.35f);
        right.setSize(.35f,.35f);
        left.setSize(.35f,.35f);

        //up.debug();
        //debug.setAutoShapeType(true);

        touchMove(elide,board);


        game.world.uistage.addActor(up);
        game.world.uistage.addActor(down);
        game.world.uistage.addActor(left);
        game.world.uistage.addActor(right);
        */




        game.world.uistage.addActor(score);
        game.world.uistage.addActor(time);
        game.world.uistage.addActor(life);
        game.world.uistage.addActor(lifeNumber);
    }

    private void addScore(){
        totalScore+=.5f;
        score.setText("Score: " + totalScore);
    }

    public void addScoreFrontFlip(){
        totalScore+=100;
    }

    public Label getScore(){
        return score;
    }

    public Label getTime(){
        return time;
    }

    public int getLifeNum(){
        return lifeNum;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public int getTotalTime(){
        return totalTime;
    }


    public void act(float dt){
        super.act(dt);
        addScore();
        //up.setPosition(game.world.stage.getCamera().position.x+.7f,game.world.stage.getCamera().position.y+1);
    }



}
