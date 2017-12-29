package com.game.melodi.Particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Paradox on 3/16/2017.
 */

public class ParticleMixer extends Image {

    ParticleEffect effect;
    ParticleEmitter emitter;

    public ParticleMixer(TextureAtlas atlas, String file){
        //super(new Texture(".png"));
        effect = new ParticleEffect();
        emitter = new ParticleEmitter();
        effect.load(Gdx.files.internal(file), atlas);
        effect.start();
        emitter = effect.getEmitters().first();
        effect.setPosition(this.getWidth()+this.getX(),this.getHeight()+this.getY());
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        effect.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        effect.update(delta);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        effect.setPosition(this.getWidth()+this.getX(),this.getHeight()+this.getY());

    }

    @Override
    public void setY(float y) {
        super.setY(y);
        effect.setPosition(this.getWidth()+this.getX(),this.getHeight()+this.getY());

    }

    public void flipXY(){
        emitter.getWind().setHigh(250f);
        emitter.getWind().setLow(250);
        emitter.getGravity().setHigh(-100);
        emitter.getGravity().setLow(-200);
        emitter.getScale().setHigh(30);
        //emitter.getTransparency().setHigh(.5f);
    }

    public ParticleEffect getEffect(){
        return effect;
    }

    public ParticleEmitter getEmitter(){
        return emitter;
    }



}
