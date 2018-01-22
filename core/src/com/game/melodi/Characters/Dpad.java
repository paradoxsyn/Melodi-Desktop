package com.game.melodi.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    public ShapeRenderer debug;
    private boolean holdup,holddown,holdleft,holdright;

    public Dpad(Melodi game,Body elide, Body board){
        this.game = game;
        this.elide = elide;
        this.board = board;

        debug = new ShapeRenderer();

        up = new Image(game.manager.get("dpadup.png",Texture.class));
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


        game.world.stage.addActor(up);
        game.world.stage.addActor(down);
        game.world.stage.addActor(left);
        game.world.stage.addActor(right);
    }

    public void touchMove(final Body one, final Body two){
        up.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Testmove");
                //return super.touchDown(event, x, y, pointer, button);
                //one.applyForce(100,0,0,0,true);
                //two.applyForce(100,0,0,0,true);
                holdup = true;
                //elideModel.applyForceToCenter(50,50,false);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //elideModel.setTransform(elideModel.getPosition().x,elideModel.getPosition().y+2,0);
                System.out.println("pressed up");
                holdup = false;
                //elideModel.applyLinearImpulse(1,2,1,2,true);
                //elideModel.applyForce(2,2,1,1,true);
                //super.touchUp(event, x, y, pointer, button);
            }
            //public void touchDragged(InputEvent event, float x, float y, int pointer) {

            //}
        });

        left.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Testmove");
                //return super.touchDown(event, x, y, pointer, button);
                //one.applyForce(100,0,0,0,true);
                //two.applyForce(100,0,0,0,true);
                holdleft = true;
                //elideModel.applyForceToCenter(50,50,false);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //elideModel.setTransform(elideModel.getPosition().x,elideModel.getPosition().y+2,0);
                System.out.println("pressed up");
                holdleft = false;
                //elideModel.applyLinearImpulse(1,2,1,2,true);
                //elideModel.applyForce(2,2,1,1,true);
                //super.touchUp(event, x, y, pointer, button);
            }
            //public void touchDragged(InputEvent event, float x, float y, int pointer) {

            //}
        });

        right.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Testmove");
                //return super.touchDown(event, x, y, pointer, button);
                //one.applyForce(100,0,0,0,true);
                //two.applyForce(100,0,0,0,true);
                holdright = true;
                //elideModel.applyForceToCenter(50,50,false);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //elideModel.setTransform(elideModel.getPosition().x,elideModel.getPosition().y+2,0);
                holdright = false;
                //elideModel.applyLinearImpulse(1,2,1,2,true);
                //elideModel.applyForce(2,2,1,1,true);
                //super.touchUp(event, x, y, pointer, button);
            }
            //public void touchDragged(InputEvent event, float x, float y, int pointer) {

            //}
        });

        down.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //return super.touchDown(event, x, y, pointer, button);
                //one.applyForce(100,0,0,0,true);
                //two.applyForce(100,0,0,0,true);
                holddown = true;
                //elideModel.applyForceToCenter(50,50,false);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //elideModel.setTransform(elideModel.getPosition().x,elideModel.getPosition().y+2,0);
                holddown = false;
                //elideModel.applyLinearImpulse(1,2,1,2,true);
                //elideModel.applyForce(2,2,1,1,true);
                //super.touchUp(event, x, y, pointer, button);
            }
            //public void touchDragged(InputEvent event, float x, float y, int pointer) {

            //}
        });
    }
    public Image getUpImg(){
        return up;
    }
    public Image getDownImg() {
        return down;
    }
    public Image getLeftImg() {
        return left;
    }
    public Image getRightImg() {
        return right;
    }

    public boolean getTouchDown(){
        return holddown;
    }
    public boolean getTouchRight(){
        return holdright;
    }
    public boolean getTouchLeft(){
        return holdleft;
    }
    public boolean getTouchUp(){
        return holdup;
    }

    @Override
    public void act(float dt){
        super.act(dt);
        //up.setPosition(game.world.stage.getCamera().position.x+.7f,game.world.stage.getCamera().position.y+1);
    }



}
