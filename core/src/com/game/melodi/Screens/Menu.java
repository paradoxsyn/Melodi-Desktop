package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.game.melodi.Animations.AnimatedImage2;
import com.game.melodi.Animations.Background;
import com.game.melodi.Loading.Loader;
import com.game.melodi.Maps.TerrainV2;
import com.game.melodi.Melodi;
import com.game.melodi.Networking.ServerStart;
import com.game.melodi.Particles.ParticleMixer;
import com.game.melodi.Transitions.AlphaFadingTransition;
import com.game.melodi.Transitions.SlicingTransition;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Menu extends ScreenAdapter {

	Melodi game;
	private BitmapFont font;
	private TextureAtlas buttonsAtlas; //** image of buttons **//
	private Skin buttonSkin,metalskin; //** images are used as skins of the button **//
	private TextButton button;
	private TextButton button2;
	private TextButton button3;
	private TextButton button4;
	float w;
	float h;
	float elapsedTime = 0f;
	float stateTime;
	TerrainV2 t2;

	private Image board;
	private Texture melotitle;
	private Background bg;
	private TextureAtlas elideatlas;
	private Animation<TextureAtlas.AtlasRegion> elideanim;
	private AnimatedImage2 elidecool;

	private TextureAtlas titleatlas;
	private Animation<TextureAtlas.AtlasRegion> titleanim;
	private AnimatedImage2 title;
	private Label.LabelStyle lstyle;

	ParticleMixer pmixer;
	private TextureAtlas noteatlas;

	Table buttonTable;


	boolean existingUser=false;
	TextButton.TextButtonStyle style;

	Preferences prefs = Gdx.app.getPreferences("Login");
	String id;
	//TODO If they uninstall the app their login is gone


	public Menu(final Melodi game){
		this.game = game;
		init();

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		elideatlas = new TextureAtlas("elidecoolanim/elidecool.txt");
		elideanim = new Animation<TextureAtlas.AtlasRegion>(.1f,elideatlas.findRegions("elidecool"));
		elidecool = new AnimatedImage2(elideanim);
		board = new Image(new Texture("boardfront.png"));
		bg = new Background(new TextureRegion(new Texture("titlemelo.png")));
		bg.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		buttonTable = new Table();
		metalskin = new Skin(Gdx.files.internal("metal-ui.json"));

		titleatlas = new TextureAtlas("titleanim/titleanim.txt");
		titleanim = new Animation<TextureAtlas.AtlasRegion>(.2f,titleatlas.findRegions("melodi"));
		title = new AnimatedImage2(titleanim);

		elidecool.setPosition(0,0);
		board.setPosition(elidecool.getX()+100,-100);
		board.setSize(500,500);
		elidecool.setSize(300,300);

		title.setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()-200);
		title.setSize(250,200);

		noteatlas = new TextureAtlas();
		noteatlas.addRegion("musicnote",new TextureRegion(new Texture("particles/musicnote.png")));
		pmixer = new ParticleMixer(noteatlas,"particles/note");

		pmixer.setX(title.getX());
		pmixer.setY(title.getY());
		pmixer.setSize(100,100);

		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		buttonsAtlas = new TextureAtlas("menu.pack"); //**button atlas image **//
		buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
		fontParameter.size = 54;
		font = fontGenerator.generateFont(fontParameter);

		lstyle = metalskin.get(Label.LabelStyle.class);
		lstyle.font = font;

	    game.stage.clear();

		style = new TextButton.TextButtonStyle(); //** Button properties **//
		style.up = buttonSkin.getDrawable("buttonOff");
		style.down = buttonSkin.getDrawable("buttonOn");

		style.font = font;


		if(existingUser) {
			existingUser();
		}else{
			newUser();
		}


		game.stage.addActor(bg);
		game.stage.addActor(title);
		game.stage.addActor(pmixer);
		game.stage.addActor(elidecool);
		game.stage.addActor(board);
		game.stage.addActor(buttonTable);


	}

	public void init(){
		existingUser = prefs.getBoolean("existing");
		System.out.println(existingUser);

		game.aHand.showAds(false);

		if(!existingUser){
			//prefs.putString("ID","null");
			//prefs.putBoolean("existing",false);
		}
		else{
			//prefs.putBoolean("existing",true);
			id = prefs.getString("ID");
			//existingUser = prefs.getBoolean("existing");
		}

	}

	public void existingUser(){
		Label name = new Label("Welcome "+id,lstyle);


		button = new TextButton("Quick Start (Not implemented)", style);
		button2 = new TextButton("Songs", style);
		button4 = new TextButton("How To Play", style);

		game.stage.addActor(name);

		buttonTable.add(name);
		buttonTable.row();
		buttonTable.add(button);
		buttonTable.row();
		buttonTable.add(button2);
		buttonTable.row();
		buttonTable.add(button4);

		buttonTable.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

		button.setPosition(Gdx.graphics.getWidth()/3+button.getWidth()/2, Gdx.graphics.getHeight()/6+ Gdx.graphics.getHeight()/18);
		button2.setPosition(Gdx.graphics.getWidth()/4+button.getWidth(), Gdx.graphics.getHeight()/36+ Gdx.graphics.getHeight()/108);
		button4.setPosition(Gdx.graphics.getWidth()/4-button.getWidth()/2, Gdx.graphics.getHeight()/36+ h/108);

		button2.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
				return true;

			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Rggggggeleased");

				//load song selection
				game.setScreen(new SongSelect(game));

			}
		});

		button4.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
				return true;

			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Rggggggeleased");

				//load song selection
				dispose();
				game.setScreen(new About(game));

			}
		});
	}

	public void newUser(){
		button = new TextButton("Register", style);
		button2 = new TextButton("Start offline", style);
		button4 = new TextButton("How To Play", style);

		buttonTable.add(button);
		buttonTable.row();
		buttonTable.add(button2);
		buttonTable.row();
		buttonTable.add(button4);

		buttonTable.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

		button.setPosition(Gdx.graphics.getWidth()/3+button.getWidth()/2, Gdx.graphics.getHeight()/6+ Gdx.graphics.getHeight()/18);
		button2.setPosition(Gdx.graphics.getWidth()/4+button.getWidth(), Gdx.graphics.getHeight()/36+ Gdx.graphics.getHeight()/108);

		button.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
				return true;

			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Rggggggeleased");

				//load song selection
				dispose();
				game.setScreen(new NewUser(game));

			}
		});

		button2.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
				return true;

			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Rggggggeleased");

				//load song selection
				dispose();
				game.setScreen(new SongSelect(game));

			}
		});

		button4.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
				return true;

			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("my app", "Rggggggeleased");

				//load song selection
				dispose();
				game.setScreen(new About(game));

			}
		});
	}

	public void update(float dt) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		game.stage.act(dt);

	}


	@Override
	public void resize(int width, int height) {
		game.viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
	}

	@Override
	public void dispose(){
		elideatlas.dispose();
		buttonsAtlas.dispose();
		buttonTable.reset();
		game.stage.clear();

	}



}

