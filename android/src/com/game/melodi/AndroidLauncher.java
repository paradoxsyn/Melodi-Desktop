package com.game.melodi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.melodi.Melodi;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication {

	PlayServices signIn;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//MobileAds.initialize(this, "ca-app-pub-8441412014443121~3883130886");
		AdHandler adHand = new AdHandler();
		 signIn = new PlayServices(this);
		adHand.adViewInit(this);
		//config.useWakelock = true;
		activityFeatures();
		View gameView = initializeForView(new Melodi(new ExtPath(),new MusicList(this),adHand,signIn), config);

		layout.addView(gameView);
		layout.addView(adHand.getAdView(),adHand.getAdParams());
		//layout.addView();
		setContentView(layout);

		//initialize(new Melodi(new ExtPath(),new MusicList(this)), config);
	}

	private void activityFeatures(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

	}

	private void createLogin(String email, String password){
		signIn.getMAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
				// If sign in fails, display a message to the user. If sign in succeeds
				// the auth state listener will be notified and logic to handle the
				// signed in user can be handled in the listener.

				if(!task.isSuccessful()){
					Toast.makeText(AndroidLauncher.this,"Login unsucessful",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}



}
