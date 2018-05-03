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

    public Grind(Melodi game){
        this.game = game;
        shape = new EdgeShape();

        for(int i=0;i < 200;i++){
            p1.set(i,0);
            p2.set(i+1,1);
            shape.set(p1,p2);
            game.world.grindFixtures.add(game.world.grindBody.createFixture(shape,5));
        }

    }

    public void init(){

    }
}
