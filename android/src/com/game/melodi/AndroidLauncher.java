package com.game.melodi;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.melodi.Melodi;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//MobileAds.initialize(this, "ca-app-pub-8441412014443121~3883130886");
		AdHandler adHand = new AdHandler();
		adHand.adViewInit(this);
		//config.useWakelock = true;
		activityFeatures();
		View gameView = initializeForView(new Melodi(new ExtPath(),new MusicList(this),adHand), config);

		layout.addView(gameView);
		layout.addView(adHand.getAdView(),adHand.getAdParams());

		setContentView(layout);

		//initialize(new Melodi(new ExtPath(),new MusicList(this)), config);
	}

	private void activityFeatures(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

	}



}
