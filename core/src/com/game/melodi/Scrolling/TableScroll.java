package com.game.melodi.Scrolling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Paradox on 6/20/2017.
 */

public class TableScroll {
    SimpleDirectionGestureDetector gesture;
    Vector3 touchPos;
    Rectangle tableBounds;
    ShapeRenderer render;


    public TableScroll(Table table, InputMultiplexer multi){
        tableBounds = new Rectangle(table.getX(),table.getOriginY(),table.getWidth(),table.getHeight()*table.getRows());
        //table.setDebug(true);
        gesture = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onLeft() {
                //maybe top to bottom
                if(tableBounds.contains(touchPos.x, touchPos.y)){
                    System.out.println("Left");
                }

            }

            @Override
            public void onRight() {
                if(tableBounds.contains(touchPos.x, touchPos.y)){
                    System.out.println("Right");
                }
            }

            @Override
            public void onUp() {
                if(tableBounds.contains(touchPos.x, touchPos.y)){
                    System.out.println("Up");
                }
            }

            @Override
            public void onDown() {
                if(tableBounds.contains(touchPos.x, touchPos.y)){
                    System.out.println("Down");

                }
            }
        });

        multi.addProcessor(gesture);
    }

    public void handleinput(){
        if (Gdx.input.isTouched()) {
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
            //camera.unproject(touchPos);
            System.out.println(touchPos);
            System.out.println(tableBounds);
            if(tableBounds.contains(touchPos.x, touchPos.y)){
                System.out.println("FK");
            }

        }
    }

}
