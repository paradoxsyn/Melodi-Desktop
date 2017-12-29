package com.game.melodi.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Paradox on 4/4/2017.
 */

public class Background2 extends Actor {
    private final TextureRegion textureRegion;
    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;
    private Rectangle textureRegionBounds3;
    private Texture tex;
    private int speed = 100;
    private int srcY=0;
    private int srcX=0;

    public Background2(Texture tx) {
        //textureRegion = new TextureRegion(Assets.nightskyparallax);
        tx.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tx.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        this.tex = tx;
        textureRegion = new TextureRegion(tx);
        textureRegionBounds1 = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textureRegionBounds3 = new Rectangle(0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void act(float delta) {
        //if (leftBoundsReached(delta)) {
         //   resetBounds();
        //} else {
         //   updateXBounds(-delta);
        //}
        if(topBoundsReached(delta)){
           resetYBounds();
        }else {
            updateYBounds(-delta);
        }
    }

    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    }

    private void updateYBounds(float delta) {
        textureRegionBounds1.y += delta * speed;
        textureRegionBounds3.y += delta * speed;
    }

    private void resetBounds() {
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void resetYBounds() {
        textureRegionBounds1 = textureRegionBounds3;
        textureRegionBounds3 = new Rectangle(0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= 0;
    }

    private boolean topBoundsReached(float delta){
        return (textureRegionBounds3.y - (delta * speed)) <= 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        srcY+=5;
        srcX+=3;
        //batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Gdx.graphics.getWidth(),
          //      Gdx.graphics.getHeight());
        //batch.draw(textureRegion, textureRegionBounds3.x, textureRegionBounds3.y, Gdx.graphics.getWidth(),
            //    Gdx.graphics.getHeight());
        batch.draw(tex,0,0,srcX,srcY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
