package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.melodi.Melodi;

/**
 * Created by RabitHole on 1/15/2018.
 */

public class About extends ScreenAdapter {

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

    public About(final Melodi game){
        this.game = game;

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        bg = new Image(new Texture(Gdx.files.internal("background.png")));
        bg.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));

        buttonsAtlas = new TextureAtlas("menu.pack"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
        fontParameter.size = 54;
        font = fontGenerator.generateFont(fontParameter);
        lstyle = metalskin.get(Label.LabelStyle.class);
        lstyle.font = font;

        style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");

        style.font = font;

        button = new TextButton("Ok lets try this",style);
        button.setPosition(Gdx.graphics.getWidth()/2-200,Gdx.graphics.getHeight()-800);

        label = new Label("This requires basic knowledge of how to add files to an (for now) Android phone.\n" +
                "1.Make a folder on your phone named Music (If it's not already there).\n" +
                "2.Add MP3 formatted songs to the folder that aren't terribly long.\n" +
                "3.The app should then detect the MP3s and load the song level.\n" +
                "4.  ;)",lstyle);
        label.setPosition(Gdx.graphics.getWidth()/2-800,Gdx.graphics.getHeight()-500);

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

        game.stage.addActor(bg);
        game.stage.addActor(label);
        game.stage.addActor(button);

    }

    public void backButton(){
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            // Do something
            game.setScreen(new Menu(game));
        }
    }

    public void update(float dt) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        game.stage.act(dt);

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
        game.stage.act();
        //game.viewPort.apply();
        game.stage.draw();
        //t2.render();
        backButton();
    }

    @Override
    public void dispose(){
        buttonSkin.dispose();
        metalskin.dispose();
        game.stage.clear();

    }
}
