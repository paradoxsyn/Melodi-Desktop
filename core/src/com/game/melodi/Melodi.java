package com.game.melodi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.game.melodi.Loading.PathInterface;
import com.game.melodi.Physics.GameWorld;
import com.game.melodi.Screens.Menu;

public class Melodi extends Game {
	public SpriteBatch batch;
	public FitViewport viewPort;
	public OrthographicCamera camera;
	public Texture img;
	public TextureRegion bg;
	public Stage stage;
	public MusicController playerstream;
	public ShapeRenderer test;
	public GameWorld world; // contains the game world's bodies and actors.
	public MixRender r;
	public AssetManager manager;
	public static MusicPlayer player;
	public PathInterface extPath;
	//GUI Aspect
	public static final int WIDTH=480; //1024
	public static final int HEIGHT=800; //600

	public Melodi(PathInterface path){
		this.extPath = path;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		//camera = new OrthographicCamera(WIDTH,HEIGHT); //prob dont need
		//viewPort = new FitViewport(this.WIDTH,this.HEIGHT);
		viewPort = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		stage = new Stage(viewPort);
		test = new ShapeRenderer();
		world = new GameWorld();
		r = new MixRender(world);
		player = new MusicPlayer();
		//this.setScreen(new Test(this));
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
