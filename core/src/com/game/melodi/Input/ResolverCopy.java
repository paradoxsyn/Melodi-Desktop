package com.game.melodi.Input;

/**
 * Created by Paradox on 4/3/2017.
 */


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ResolverCopy implements SwipeResolver {

    @Override
    public void resolve(Array<Vector2> input, Array<Vector2> output) {
        output.clear();
        output.addAll(input);
    }

}