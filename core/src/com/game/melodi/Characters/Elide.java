package com.game.melodi.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
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

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by Paradox on 5/11/2017.
 */

public class Elide extends Image {

    //public final Body body; // bob's box2d body
    Image elide;
    Image board;
    BodyDef bd,bd2,bd3;
    PhysicsShapeCache phybod;
    Body elideModel,boardModel,leftWheel,rightWheel;
    Melodi game;
    PrismaticJointDef jointdef;
    RevoluteJointDef revdef;
    WheelJointDef wheeljoint;
    CircleShape wheelShape;
    FixtureDef wheelFix;

    public ShapeRenderer debug;
    public SpriteBatch batch;

    public static final int MAX_VELOCITY = 15;

    public Elide(GameWorld world, Melodi game){
        // char is an Image, so we load the graphics from the assetmanager
        //TODO create Asset manager
        this.game = game;
        Box2DSprite d;
        debug = new ShapeRenderer();
        batch = new SpriteBatch();

        wheelShape = new CircleShape();
        wheelFix = new FixtureDef();
        wheelShape.setRadius(.05f);
        wheelFix.shape = wheelShape;

        elide = new Image(game.manager.get("elideonboard.png",Texture.class));
        board = new Image(game.manager.get("boardmove.png",Texture.class));

        elide.setSize(.75f,.75f);
        //elide.debug();
        debug.setAutoShapeType(true);
        board.setSize(.90f,.90f);


        //TODO Make Elide's body
        bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        //needs to be dynamic
        bd.position.set(world.body.getPosition().x+7,world.body.getPosition().y+3);
        bd.allowSleep  = true;
        bd.angularDamping = 2000;

        phybod = new PhysicsShapeCache("physics/boardphy.xml");
        elideModel = phybod.createBody("elideonboard",game.world.world,bd,.0055f,.0055f);
        //elideModel.setTransform(1,1,0);

        //TODO Make board body
        bd2 = new BodyDef();
        bd2.type = BodyDef.BodyType.DynamicBody;
        //needs to be dynamic
        bd2.position.set(board.getX(),board.getY());
        bd2.allowSleep  = false;
        bd2.angularDamping = 2000;

        boardModel = phybod.createBody("boardmove",game.world.world,bd,.01f,.01f);

        boardModel.setUserData(board);
        elideModel.setUserData(elide);

        bd3 = new BodyDef();
        bd3.type = BodyDef.BodyType.DynamicBody;
        bd3.position.set(board.getWidth()/2,elide.getHeight()/2);

        wheelFix.density = 5;

        leftWheel = game.world.world.createBody(bd3);
        leftWheel.createFixture(wheelFix);

        bd3.position.set(board.getWidth(),board.getY());

        //rightWheel = game.world.world.createBody(bd3);
        //rightWheel.createFixture(wheelFix);

        //revdef = new RevoluteJointDef();
        //revdef.bodyA = boardModel;
        //revdef.bodyB = leftWheel;
        //revdef.localAnchorA.set(.20f,.4f);
        //revdef.localAnchorB.set(0,0);
        //revdef.collideConnected=false;
        //revdef.localAnchorB.set(leftWheel.getLocalCenter().x,leftWheel.getLocalCenter().y);
        //revdef.referenceAngle = leftWheel.getAngle() - boardModel.getAngle();
        //game.world.world.createJoint(revdef);

        //revdef.bodyB = rightWheel;
        //revdef.localAnchorA.set(.7f,.4f);
        //game.world.world.createJoint(revdef);




        jointdef = new PrismaticJointDef();
        jointdef.bodyA = elideModel;
        jointdef.bodyB = boardModel;
        jointdef.collideConnected = true;
        jointdef.localAnchorA.set(0,0);
        jointdef.localAnchorB.set(0,.5f);
        jointdef.enableLimit = true;


        game.world.world.createJoint(jointdef);

        game.world.stage.addActor(elide);
        game.world.stage.addActor(board);

    }

    public Body getBoardBody(){
        return boardModel;
    }

    public Image getImage(){
        return elide;
    }

    public Body getElideBody(){
        return elideModel;
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
        elide.setPosition(pos.x,pos.y);
        elide.setOrigin(elide.getMaxWidth()/2,elide.getMaxHeight()/2);
        elide.setRotation(elideModel.getAngle() * MathUtils.radiansToDegrees);
        board.setPosition(boardpos.x,boardpos.y);
        board.setOrigin(0,0);
        board.setRotation(boardModel.getAngle() * MathUtils.radiansToDegrees);

        //leftWheel.getPosition().set(boardpos.x,boardpos.y-1);
        //rightWheel.getPosition().set(board.getWidth(),boardpos.y-1);


    }
}
