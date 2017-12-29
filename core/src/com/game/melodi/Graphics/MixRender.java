package com.game.melodi.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.game.melodi.Physics.GameWorld;

/**
 * Created by Paradox on 5/11/2017.
 */

public class MixRender {

    GameWorld world;
    OrthographicCamera camera;
    Box2DDebugRenderer renderer;

    public MixRender(GameWorld world){
        this.world = world;
        this.renderer = new Box2DDebugRenderer();

        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        this.camera = (OrthographicCamera) world.stage.getCamera();

        //camera.translate(0,1f);

        // center the camera on char (optional)
        //camera.position.x = world.bob.body.getPosition().x;
        //camera.position.y = world.bob.body.getPosition().y;


    }

    public void render()
    {
        // have the camera follow char
        //camera.position.x = world.bob.body.getPosition().x;
        //camera.position.y = world.bob.body.getPosition().y;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // box2d debug renderering (optional)
        renderer.render(world.world, camera.combined);
        // game stage rendering
        world.stage.draw();
    }

    public void resize(int w, int h){

    }
}
