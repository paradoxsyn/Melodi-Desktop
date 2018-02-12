package com.game.melodi.Animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Paradox on 3/24/2017.
 */

public class AnimatedImage2 extends Image {

    protected Animation<TextureAtlas.AtlasRegion> animation;
    private float stateTime = 0;
    private boolean pauseTime=false;

    public AnimatedImage2(com.badlogic.gdx.graphics.g2d.Animation<TextureAtlas.AtlasRegion> animation){
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    @Override
    public void act(float delta)
    {
        if(!pauseTime) {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta,true));
            super.act(delta);
        }
    }


    public Animation<TextureAtlas.AtlasRegion> getAnimation(){
        return this.animation;
    }

    public float getStateTime(){
        return stateTime;
    }

    public void pause(){
        pauseTime = true;
    }

    public void resume(){
        pauseTime = false;
    }

    public void setKeyFrame(int keyFrame){

        stateTime=0;
    }
}
