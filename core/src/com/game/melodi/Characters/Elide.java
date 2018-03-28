package com.game.melodi.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.game.melodi.Animations.AnimatedImage;
import com.game.melodi.Animations.AnimatedImage2;
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
    private boolean shouldCheck,isTouched,trick;
    private float initialHeight,jumpHeight;

    public ShapeRenderer debug;
    public SpriteBatch batch;

    int nextVel;
    Vector2[] prevVels;


    private TextureAtlas elideatlas;
    private Animation<TextureAtlas.AtlasRegion> elideanim;
    private AnimatedImage2 elidefrontflip;

    private TextureAtlas boardfrontflipatlas;
    private Animation<TextureAtlas.AtlasRegion> boardfrontanim;
    private AnimatedImage2 boardfrontflip;


    private TextureAtlas elidefallatlas;
    private Animation<TextureAtlas.AtlasRegion> elidefallanim;
    private AnimatedImage2 elidefall;

    public static final int MAX_VELOCITY = 10;
    public final static int NUM_PREV_VELS = 5;

    public Elide(GameWorld world, Melodi game){
        // char is an Image, so we load the graphics from the assetmanager
        //TODO create Asset manager
        this.game = game;
        debug = new ShapeRenderer();
        batch = new SpriteBatch();

        elideatlas = game.manager.get("backflipanim/flip.txt",TextureAtlas.class);
        elideanim = new Animation<TextureAtlas.AtlasRegion>(.2f,elideatlas.findRegions("flip"));
        elidefrontflip = new AnimatedImage2(elideanim);
        elidefrontflip.setSize(.60f,.60f);
        elidefrontflip.setVisible(false);
        elidefrontflip.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);

        boardfrontflipatlas = game.manager.get("boardfrontflipanim/boardflip.txt",TextureAtlas.class);
        boardfrontanim = new Animation<TextureAtlas.AtlasRegion>(.3f,boardfrontflipatlas.findRegions("boardfront"));
        boardfrontflip = new AnimatedImage2(boardfrontanim);
        boardfrontflip.setSize(.60f,.60f);
        boardfrontflip.setVisible(false);
        boardfrontflip.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);

        elidefallatlas = game.manager.get("elidefallanim/fall.txt",TextureAtlas.class);
        elidefallanim = new Animation<TextureAtlas.AtlasRegion>(.3f,elidefallatlas.findRegions("elidefall"));
        elidefall = new AnimatedImage2(elidefallanim);
        elidefall.setSize(.60f,.60f);
        elidefall.setVisible(false);
        elidefall.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);


        prevVels = new Vector2[NUM_PREV_VELS];
        for(int i=0;i<prevVels.length;i++){
            prevVels[i] = new Vector2(0,0);
        }

        wheelShape = new CircleShape();
        wheelFix = new FixtureDef();
        wheelShape.setRadius(.05f);
        //fixpackedimages on board anim
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
        bd.linearDamping = 0.5f;

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

        boardModel.setUserData("board");
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

        addTouchListen();

        game.world.stage.addActor(elide);
        game.world.stage.addActor(board);
        game.world.stage.addActor(elidefrontflip);
        game.world.stage.addActor(boardfrontflip);

    }

    private boolean addTouchListen(){
        elide.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //return super.touchDown(event, x, y, pointer, button);
                isTouched=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //super.touchUp(event, x, y, pointer, button);
                isTouched=false;
            }
        });
        return isTouched;
    }

    public Body getBoardBody(){
        return boardModel;
    }

    public Image getCharImage(){
        return elide;
    }

    public Image getBoardImage() {return board;}

    public Body getElideBody(){
        return elideModel;
    }

    public boolean getTrick(){
        return trick;
    }

    public void setTrick(boolean trick){
        this.trick = trick;
    }

    public boolean isShouldCheck(){
        return shouldCheck;
    }

    public float getX(){
        return elide.getX();
    }

    public float getY(){
        return elide.getY();
    }

    public AnimatedImage2 getElideFrontFlip(){
        return elidefrontflip;
    }

    public void showFrontFlip(){
        elidefrontflip.setVisible(true);
        elide.setVisible(false);
        elidefrontflip.setKeyFrame(0);
        trick=true;
    }

    public void failTrick(){
        elidefall.setVisible(true);
        elidefall.setKeyFrame(0);

        elide.setVisible(false);
        elidefrontflip.setVisible(false);
        boardfrontflip.setVisible(false);

    }

    public void showFrontBoardFlip(){
        boardfrontflip.setVisible(true);
        board.setVisible(false);
        boardfrontflip.setKeyFrame(0);
        trick=true;
    }

    private void checkAnimation(){
        if(elidefrontflip.getAnimation().isAnimationFinished(elidefrontflip.getStateTime()) && elidefrontflip.isVisible()){
            elidefrontflip.setVisible(false);
            elide.setVisible(true);
            shouldCheck=false;
            trick=false;
        }
    }

    private void checkBoardAnimation(){
        if(boardfrontflip.getAnimation().isAnimationFinished(boardfrontflip.getStateTime()) && boardfrontflip.isVisible()){
            boardfrontflip.setVisible(false);
            board.setVisible(true);
            //shouldCheck=false;
            trick=false;
        }
    }

    public void charBlink(){
        //TODO Make it an animation

    }

    public int getJumpHeight(){

        if(isTouched && game.world.getCollision().getConnected()){
            initialHeight=elideModel.getPosition().y;
            jumpHeight = 0;
        }else if(isTouched && !game.world.getCollision().getConnected()){
            jumpHeight=elideModel.getPosition().y;
            initialHeight=elideModel.getPosition().y;
        }else{
            initialHeight=elideModel.getPosition().y;
            jumpHeight=0;
        }

        float distance = initialHeight + jumpHeight;

        return (int)distance;
    }

    @Override
    public void act(float delta) {
        // here we override Actor's act() method to make the actor follow the box2d body
        super.act(delta);

        Vector2 pos = elideModel.getPosition().sub(0,0);
        Vector2 boardpos = boardModel.getPosition().sub(0,0);


        /*Vector2 vel = boardModel.getLinearVelocity();
        Vector2 weightedVel = vel;

        for(int i=0;i<NUM_PREV_VELS;++i){
            weightedVel.x += prevVels[i].x;
            weightedVel.y += prevVels[i].y;
        }
        weightedVel.x = weightedVel.x/NUM_PREV_VELS;
        weightedVel.y = weightedVel.y/NUM_PREV_VELS;

        prevVels[nextVel++] = vel;
        if(nextVel >= NUM_PREV_VELS) nextVel = 0;

        float angle = weightedVel.angle();*/
        //For smoothing out velocities

        elide.setPosition(pos.x,pos.y);
        elide.setOrigin(elide.getMaxWidth()/2,elide.getMaxHeight()/2);
        elide.setRotation(elideModel.getAngle() * MathUtils.radiansToDegrees);
        board.setPosition(boardpos.x,boardpos.y);
        board.setOrigin(0,0);
        board.setRotation(boardModel.getAngle() * MathUtils.radiansToDegrees);


        elidefrontflip.setPosition(elide.getX(),elide.getY());
        boardfrontflip.setPosition(board.getX(),board.getY());
        //board.setRotation(angle * MathUtils.radiansToDegrees);
        checkAnimation();
        checkBoardAnimation();
        getJumpHeight();

        //leftWheel.getPosition().set(boardpos.x,boardpos.y-1);
        //rightWheel.getPosition().set(board.getWidth(),boardpos.y-1);


    }
}
