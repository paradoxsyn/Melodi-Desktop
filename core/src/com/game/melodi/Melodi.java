package com.game.melodi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.melodi.Audiofullread.MusicPlayer;
import com.game.melodi.Audiostream.MusicController;
import com.game.melodi.Graphics.MixRender;
import com.game.melodi.Input.SwipeHandler;
import com.game.melodi.Input.SwipeTriStrip;
import com.game.melodi.Loading.PathInterface;
import com.game.melodi.Loading.ServerLoader;
import com.game.melodi.Networking.ServerStart;
import com.game.melodi.Physics.GameWorld;
import com.game.melodi.Screens.Menu;

public class Melodi extends Game {
	public SpriteBatch batch;
	public FitViewport viewPort;
	public OrthographicCamera camera;
	public Texture img;
	public TextureRegion bg;
	public Stage stage;
	static public MusicController playerstream;
	public ShapeRenderer test;
	public GameWorld world; // contains the game world's bodies and actors.
	public MixRender r;
	public AssetManager manager;
	public static MusicPlayer player;
	public PathInterface extPath;
	public InputMultiplexer multi;
	String storage;
	//GUI Aspect
	public static final int WIDTH=480; //1024
	public static final int HEIGHT=800; //600

	public static SwipeHandler swipe;

	InputProcessor backProcessor;

	public static ServerStart server;

	public Melodi(PathInterface path){
		this.extPath = path;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		multi = new InputMultiplexer();
		img = new Texture("badlogic.jpg");
		//camera = new OrthographicCamera(WIDTH,HEIGHT); //prob dont need
		//viewPort = new FitViewport(this.WIDTH,this.HEIGHT);
		viewPort = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		stage = new Stage(viewPort);
		test = new ShapeRenderer();
		world = new GameWorld();
		r = new MixRender(world);
		player = new MusicPlayer();
		server = new ServerStart();

		swipe = new SwipeHandler(30);
		swipe.minDistance = 10;

		multi.addProcessor(stage);
		multi.addProcessor(world.stage);
		multi.addProcessor(swipe);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(multi); //** stage is responsive **//
		this.setScreen(new Menu(this));
	}


	@Override
	public void render () {
		super.render();
	}

	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
