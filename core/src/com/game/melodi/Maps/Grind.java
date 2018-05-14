package com.game.melodi.Maps;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.game.melodi.Melodi;

/**
 * Created by RabitHole on 4/18/2018.
 */

public class Grind {

    public Melodi game;
    private EdgeShape shape;
    private Vector2 p1 = new Vector2();
    private Vector2 p2 = new Vector2();
    private float groundHeight;
    private int x,y;

    public Grind(Melodi game, int start, int end){
        this.game = game;
        shape = new EdgeShape();
        this.x = start;
        this.y = end;

    }

    public void init(){
        groundHeight = game.world.fixtures.get(10).getBody().getPosition().y;
        for(int i=x;i < y;i++){
            p1.set(i,groundHeight+4);
            p2.set(i+1,groundHeight+4);
            shape.set(p1,p2);
            game.world.grindFixtures.add(game.world.grindBody.createFixture(shape,5));
        }
        p1.set(y,groundHeight+4);
        p2.set(y+1,groundHeight+6);
        shape.set(p1,p2);
        game.world.grindFixtures.add(game.world.grindBody.createFixture(shape,5));
    }

    public void addFrontRamp(int startX, int startY){
        p1.set(-2,5);
        p2.set(0,0);
        shape.set(p1,p2);
        game.world.grindFixtures.add(game.world.wallbody.createFixture(shape,5));
        p1.set(-2,8);
        p2.set(-2,5);
        shape.set(p1,p2);
        game.world.grindFixtures.add(game.world.wallbody.createFixture(shape,5));
    }

    public void addBackRamp(float startX, float startY){
        p1.set(startX+2,5);
        p2.set(startX,startY);
        shape.set(p1,p2);
        game.world.grindFixtures.add(game.world.endwallbody.createFixture(shape,5));
        p1.set(startX+2,startY+8);
        p2.set(startX+2,startY+5);
        shape.set(p1,p2);
        game.world.grindFixtures.add(game.world.endwallbody.createFixture(shape,5));
    }
}
