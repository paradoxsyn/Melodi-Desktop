package com.game.melodi;

import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.melodi.Maps.TerrainV2;
import com.game.melodi.Melodi;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Melodi(new ExtPath()), config);
		//initialize(new TerrainV2(), config);
	}
}
