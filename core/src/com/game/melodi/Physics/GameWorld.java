package com.game.melodi.Physics;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.List;

import static com.game.melodi.Melodi.HEIGHT;
import static com.game.melodi.Melodi.WIDTH;

/**
 * Created by Paradox on 5/11/2017.
 */

public class GameWorld {



    public static final Vector2 GRAVITY = new Vector2(0, -10.8f);
    //-10.8f

    public final Stage stage,uistage,backgroundstage,pointstage; // stage containing game actors (not GUI, but actual game elements)
    public World world; // box2d world
    public List<Fixture> fixtures,smoothFixtures,grindFixtures,frontRampFixture,backRampFixture;
    public Body body,wallbody,endwallbody,grindBody;
    public BodyDef bd;
    public FixtureDef wallfixdef,endwallfixdef;
    public StretchViewport viewport;
    public PolygonShape wall;
    private CollisionDetect collision;

    public PolygonShape axleShape;
    public FixtureDef axleFixture;
    public BodyDef axleDef;
    public Body rearAxel, frontAxel;

    // here we set up the actual viewport size of the game in meters.
    public final static float UNIT_WIDTH = WIDTH/160; // 6.4 meters width
    public final static float UNIT_HEIGHT = HEIGHT/160; // 3.75 meters height

    //public final static float UNIT_WIDTH = WIDTH/ // 6.4 meters width
    //public final static float UNIT_HEIGHT = HEIGHT // 3.75 meters height

    public GameWorld(){
        Box2D.init();
        collision = new CollisionDetect();
        world = new World(GRAVITY, true);
        //viewport = new FitViewport(UNIT_WIDTH,UNIT_HEIGHT);// set the game stage viewport to the meters size
        viewport = new StretchViewport(UNIT_WIDTH,UNIT_HEIGHT);
        stage = new Stage(viewport); // create the game stage
        uistage = new Stage();
        backgroundstage = new Stage();
        pointstage = new Stage();
        createWorld();
    }

    private void createWorld() {


        fixtures = new ArrayList<>();
        grindFixtures = new ArrayList<>();
        smoothFixtures = new ArrayList<>();
        frontRampFixture = new ArrayList<>();
        backRampFixture = new ArrayList<>();
        bd = new BodyDef();
        wall = new PolygonShape();
        wallfixdef = new FixtureDef();
        endwallfixdef = new FixtureDef();

        //axle
        axleShape = new PolygonShape();
        axleFixture = new FixtureDef();
        axleDef = new BodyDef();
        axleDef.type = BodyDef.BodyType.DynamicBody;

        //WALL
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(0,0);

        wall.setAsBox(1,100);
        wallfixdef.shape = wall;
        wallfixdef.restitution = .25f;
        wallfixdef.density = 0;
        wallfixdef.friction = 1;

        wallbody = world.createBody(bd);
        //wallbody.createFixture(wallfixdef);

        //ground init
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(0,0);
        body = world.createBody(bd);
        grindBody = world.createBody(bd);
        body.setUserData("ground");

        world.setContactListener(collision);


    }

    public List<Fixture> getFixtures(){
        return fixtures;
    }

    public void endWall(float x, float y){
        //END WALL
        bd.position.set(x,y);
        endwallfixdef.shape = wall;
        endwallbody = world.createBody(bd);
        endwallbody.createFixture(endwallfixdef);
    }

    public CollisionDetect getCollision(){
        return collision;
    }

    public void update(float delta) {

        // perform game logic here
        world.step(delta, 3, 3); // update box2d world
        stage.act(delta); // update game stage
        uistage.act(delta);
        backgroundstage.act(delta);
        pointstage.act(delta);
    }


}
