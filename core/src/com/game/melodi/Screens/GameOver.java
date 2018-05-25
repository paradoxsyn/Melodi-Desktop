package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.melodi.Melodi;

/**
 * Created by RabitHole on 3/2/2018.
 */

public class GameOver extends ScreenAdapter {

    float elapsedTime;
    Melodi game;
    Image bg;
    Label label;
    private Label.LabelStyle lstyle;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin,metalskin; //** images are used as skins of the button **//
    TextButton.TextButtonStyle style;
    private TextButton button;
    private int score;


    public GameOver(final Melodi game, final int score){
        this.game = game;
        this.score = score;
        Gdx.input.setCatchBackKey(false);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bg = new Image(new Texture(Gdx.files.internal("background.png")));
        //bg.setBounds(Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/6,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2); middle of scrn
        bg.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));

        game.aHand.showAds(true);

        buttonsAtlas = new TextureAtlas("menu.pack"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
        fontParameter.size = 40;
        font = fontGenerator.generateFont(fontParameter);
        lstyle = metalskin.get(Label.LabelStyle.class);
        lstyle.font = font;

        style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");

        style.font = font;

        button = new TextButton("Done",style);
        button.setPosition(Gdx.graphics.getWidth()/2-200,Gdx.graphics.getHeight()-800);

        label = new Label("You score:"+score,lstyle);
        label.setPosition(Gdx.graphics.getWidth()/2-600,Gdx.graphics.getHeight()-500);

        button.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
                return true;

            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Rggggggeleased");

                //load song selection
                dispose();
                game.setScreen(new Menu(game));

            }
        });

        game.gameOverStage.addActor(bg);
        game.gameOverStage.addActor(label);
        game.gameOverStage.addActor(button);

    }

    public void update(float dt) {
        elapsedTime += Gdx.graphics.getDeltaTime();

    }


    @Override
    public void resize(int width, int height) {
        game.viewPort.update(width, height, true);
        game.stage.getCamera().update();
    }

    @Override
    public void render(float delta){
        elapsedTime += Gdx.graphics.getDeltaTime();
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //game.stage.act();
        //game.viewPort.apply();
        //game.stage.draw();
        game.gameOverStage.act();
        game.gameOverStage.draw();
        //t2.render();
    }

    @Override
    public void dispose(){
        buttonSkin.dispose();
        metalskin.dispose();
        game.gameOverStage.clear();

    }
}
