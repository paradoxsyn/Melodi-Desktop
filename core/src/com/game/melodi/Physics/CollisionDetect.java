package com.game.melodi.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by RabitHole on 3/6/2018.
 */

public class CollisionDetect implements ContactListener {

    private boolean isConnected,isTrackEnd;

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureB().getBody().getUserData() == "board" && contact.getFixtureA().getBody().getUserData() == "ground"){
            isConnected = true;
        }
        else{
            isConnected = false;
        }

    }

    public boolean getConnected(){
        return isConnected;
    }

    @Override
    public void endContact(Contact contact) {
        isConnected = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
