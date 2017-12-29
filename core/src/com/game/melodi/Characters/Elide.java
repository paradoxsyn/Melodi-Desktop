package com.game.melodi.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.melodi.Animations.AnimatedImage;
import com.game.melodi.Melodi;
import com.game.melodi.Physics.BodyEditorLoader;
import com.game.melodi.Physics.GameWorld;
import com.codeandweb.physicseditor.*;

/**
 * Created by Paradox on 5/11/2017.
 */

public class Elide extends Image {

    //public final Body body; // bob's box2d body
    Image elide;
    Image board;
    Image dpad;
    BodyDef bd;
    FixtureDef fd;
    CircleShape shape;
    PhysicsShapeCache phybod;
    Body elideModel;

    public Elide(GameWorld world, Melodi game){
        // char is an Image, so we load the graphics from the assetmanager
        //TODO create Asset manager
        //Texture tex = Assets.manager.get("characters.png", Texture.class);
        //this.setDrawable(new TextureRegionDrawable(new TextureRegion(tex, 0, 256, 128, 128)));
        elide = new Image(game.manager.get("elideonboard.png",Texture.class));
        board = new Image(game.manager.get("boardmove.png",Texture.class));

        touchMove();

        elide.setSize(.75f,.75f);
        elide.setPosition(0,game.world.body.getPosition().y+.5f);

        board.setSize(.75f,.75f);
        //board.setPosition(elide.getX(),elide.getY()-.5f);
        game.world.stage.addActor(elide);
        game.world.stage.addActor(board);

        //TODO Make Elide's body
        bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        //needs to be dynamic
        bd.position.set(world.body.getPosition().x+2f,world.body.getPosition().y+2);
        elide.setPosition(bd.position.x,bd.position.y);
        fd = new FixtureDef();
        fd.density = 1;
        fd.restitution = 0;
        fd.friction = 0f;

        //phybod = new PhysicsShapeCache("physics/elidephy.xml");
        //elideModel = phybod.createBody("elideonboard",game.world.world,bd,1,1);
        //elideModel.setTransform(1,1,0);


        shape = new CircleShape();
        //shape.setAsBox(.5f,.25f);
        shape.setRadius(.4f);
        fd.shape = shape;

        elideModel = game.world.world.createBody(bd);
        elideModel.createFixture(fd);
        elideModel.setUserData(elide);
    }

    public Body getBody(){
        //return elideModel;
        return null;
    }

    public void touchMove(){
        elide.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Testmove");
                //return super.touchDown(event, x, y, pointer, button);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //elideModel.setTransform(elideModel.getPosition().x+1,elideModel.getPosition().y+1,0);
                System.out.println("pressed up");
                //elideModel.applyForce(2,2,1,1,true);
                //super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                elideModel.applyForce(3,0,0,0,false);
            }
        });
    }


    @Override
    public void act(float delta) {
        // here we override Actor's act() method to make the actor follow the box2d body
        super.act(delta);
        //setPosition(body.getPosition().x-RADIUS, body.getPosition().y-RADIUS);
        elide.setPosition(elideModel.getPosition().x-.4f,elideModel.getPosition().y-.3f);
        board.setPosition(elide.getX(),elide.getY()-.35f);
    }

}
