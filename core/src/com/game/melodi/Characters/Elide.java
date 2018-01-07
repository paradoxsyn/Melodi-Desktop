package com.game.melodi.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;
import com.game.melodi.Animations.AnimatedImage;
import com.game.melodi.Melodi;
import com.game.melodi.Physics.BodyEditorLoader;
import com.game.melodi.Physics.GameWorld;
import com.codeandweb.physicseditor.*;
import com.game.melodi.Scrolling.SimpleDirectionGestureDetector;

/**
 * Created by Paradox on 5/11/2017.
 */

public class Elide extends Image {

    //public final Body body; // bob's box2d body
    Image elide;
    Image board;
    BodyDef bd,bd2;
    PhysicsShapeCache phybod;
    Body elideModel,boardModel;
    Melodi game;
    PrismaticJointDef jointdef;
    public ShapeRenderer debug;

    public Elide(GameWorld world, Melodi game){
        // char is an Image, so we load the graphics from the assetmanager
        //TODO create Asset manager
        //Texture tex = Assets.manager.get("characters.png", Texture.class);
        //this.setDrawable(new TextureRegionDrawable(new TextureRegion(tex, 0, 256, 128, 128)));
        this.game = game;
        debug = new ShapeRenderer();

        elide = new Image(game.manager.get("elideonboard.png",Texture.class));
        board = new Image(game.manager.get("boardmove.png",Texture.class));

        //elide.setSize(.75f,.75f);
        elide.debug();
        debug.setAutoShapeType(true);
        //elide.setPosition(0,game.world.body.getPosition().y);
        elide.setWidth(.75f);
        elide.setHeight(.75f);
        //elide.setScaling(Scaling.fill);
        board.setSize(.75f,.75f);
        board.setPosition(elide.getX(),elide.getY()-.5f);


        //TODO Make Elide's body
        bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        //needs to be dynamic
        bd.position.set(world.body.getPosition().x+7,world.body.getPosition().y+3);
        elide.setPosition(bd.position.x,bd.position.y);
        bd.allowSleep  = true;
        bd.angularDamping = 800;

        phybod = new PhysicsShapeCache("physics/elidephy.xml");
        elideModel = phybod.createBody("elideonboard",game.world.world,bd,.0055f,.0055f);
        //elideModel.setTransform(1,1,0);

        //TODO Make board body
        bd2 = new BodyDef();
        bd2.type = BodyDef.BodyType.DynamicBody;
        //needs to be dynamic
        bd2.position.set(board.getX(),board.getY());
        bd2.allowSleep  = false;
        bd2.angularDamping = 800;

        boardModel = phybod.createBody("boardmove",game.world.world,bd,.01f,.01f);


        jointdef = new PrismaticJointDef();
        jointdef.bodyA = elideModel;
        jointdef.bodyB = boardModel;
        jointdef.collideConnected = false;
        jointdef.localAnchorA.set(0,0);
        jointdef.localAnchorB.set(0,.4f);
        jointdef.enableLimit = true;

        game.world.world.createJoint(jointdef);

        boardModel.setUserData(board);
        elideModel.setUserData(elide);

        game.world.stage.addActor(elide);
        game.world.stage.addActor(board);

    }

    public Image getImage(){
        return elide;
    }

    public Body getElideBody(){
        return elideModel;
    }

    public Body getBoardBody(){
        return boardModel;
    }

    public float getX(){
        return elide.getX();
    }

    public float getY(){
        return elide.getY();
    }



    @Override
    public void act(float delta) {
        // here we override Actor's act() method to make the actor follow the box2d body
        super.act(delta);
        Vector2 pos = elideModel.getPosition().sub(0,0);
        Vector2 boardpos = boardModel.getPosition().sub(0,0);
        //elide.setPosition(elideModel.getPosition().x+.08f,elideModel.getPosition().y-.05f);
        elide.setPosition(pos.x,pos.y);
        //elide.setOrigin(0,0);
        elide.setOrigin(0,0);
        elide.setRotation(elideModel.getAngle() * MathUtils.radiansToDegrees);
        board.setPosition(boardpos.x,boardpos.y);
        board.setOrigin(0,0);
        board.setRotation(boardModel.getAngle() * MathUtils.radiansToDegrees);




    }
}
