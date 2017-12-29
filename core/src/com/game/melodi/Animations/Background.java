package com.game.melodi.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Paradox on 4/4/2017.
 */

public class Background extends Actor {
    private final TextureRegion textureRegion;
    private final TextureRegion texflip;
    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;
    private int speed = 100;

    public Background(TextureRegion tx) {
        //textureRegion = new TextureRegion(Assets.nightskyparallax);
        textureRegion = new TextureRegion(tx);
        texflip = new TextureRegion(tx);
        texflip.flip(true,false);
        textureRegionBounds1 = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void act(float delta) {
        if (leftBoundsReached(delta)) {
            resetBounds();
        } else {
            updateXBounds(-delta);
        }
    }

    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    }



    private void resetBounds() {
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= 0;
        //change this based on screen res;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        batch.draw(texflip, textureRegionBounds2.x, textureRegionBounds2.y, Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight());
    }



}
