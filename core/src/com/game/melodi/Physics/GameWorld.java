package com.game.melodi.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.ArrayList;
import java.util.List;

import static com.game.melodi.Melodi.HEIGHT;
import static com.game.melodi.Melodi.WIDTH;

/**
 * Created by Paradox on 5/11/2017.
 */

public class GameWorld {



    public static final Vector2 GRAVITY = new Vector2(0, -3.8f);

    public final Stage stage; // stage containing game actors (not GUI, but actual game elements)
    public World world; // box2d world
    public List<Fixture> fixtures;
    public Body body;
    public Body body2;
    public BodyDef bd;
    public FixtureDef fixdef;
    public CircleShape shape;
    public StretchViewport viewport;

    // here we set up the actual viewport size of the game in meters.
    public final static float UNIT_WIDTH = WIDTH/160; // 6.4 meters width
    public final static float UNIT_HEIGHT = HEIGHT/160; // 3.75 meters height

    //public final static float UNIT_WIDTH = WIDTH/ // 6.4 meters width
    //public final static float UNIT_HEIGHT = HEIGHT // 3.75 meters height

    public GameWorld(){
        Box2D.init();
        world = new World(GRAVITY, true);
        //viewport = new FitViewport(UNIT_WIDTH,UNIT_HEIGHT);// set the game stage viewport to the meters size
        viewport = new StretchViewport(UNIT_WIDTH,UNIT_HEIGHT);
        stage = new Stage(viewport); // create the game stage
        createWorld();
    }

    private void createWorld() {

        // create box2d bodies and the respective actors here.
        //char = new char(this);
        //stage.addActor(char);
        // add more game elements here

        fixtures = new ArrayList<>();
        bd = new BodyDef();

        //bd.position.set(0,0);
        //bd.type = BodyDef.BodyType.StaticBody;
        //body = world.createBody(bd);

        //BALL
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(2,2);

        //ball shape
        shape = new CircleShape();
        shape.setRadius(1.0f);

        //fixture def ball
        fixdef = new FixtureDef();
        fixdef.shape = shape;
        fixdef.density = 1.5f;
        fixdef.friction = 1.25f;
        fixdef.restitution = .25f;
        //body2 = world.createBody(bd); //.createFixture(fixdef)
        //shape.dispose();

        //ground init
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(0,0);
        body = world.createBody(bd);


    }

    public void update(float delta) {

        // perform game logic here
        world.step(delta, 3, 3); // update box2d world
        stage.act(delta); // update game stage
    }


}
