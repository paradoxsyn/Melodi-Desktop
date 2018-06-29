package com.game.melodi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import com.game.melodi.Loading.PlayServicesInterface;
import com.game.melodi.Melodi;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication implements PlayServicesInterface {

	GoogleSignInOptions signInOpt;
	GoogleSignInClient signIn;
	GoogleSignInResult result;
	GoogleSignInAccount acc;
	private LeaderboardsClient mLeaderboardsClient;
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//MobileAds.initialize(this, "ca-app-pub-8441412014443121~3883130886");
		AdHandler adHand = new AdHandler();
		//signIn = new PlayServices(this);
		adHand.adViewInit(this);
		//config.useWakelock = true;
		activityFeatures();
		View gameView = initializeForView(new Melodi(new ExtPath(),new MusicList(this),adHand,this), config);

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

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("SDJKLSDJSKLDJLSKJDJSLDJKSJDKLSJDKLSJDLKJSKLJSLDKKLSDJKLSDJKLSJDKLSDJLKSDJKLSJKLJLKJLKJJKJLJKLKLDJKLSJDKLSJKLDJKLSDJKLSDJKLSDJKLLSDJK");
	}


	//@Override
	public void onStartMethod() {

	}

	@Override
	public void signIn() {
		signInOpt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build();
		signIn = GoogleSignIn.getClient(this,signInOpt);
		startActivityForResult(signIn.getSignInIntent(),RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		Log.d(TAG, "data : " + resultCode + " | " + data  + " || " + RC_SIGN_IN + " ||| " + requestCode);
		if(requestCode == RC_SIGN_IN){
			result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if(result.isSuccess()){
				//Stored
				acc = result.getSignInAccount();
				System.out.println("Login is success");
			}else{
				String message = result.getStatus().getStatusMessage();
				if(message == null || message.isEmpty());{
					Toast.makeText(this, "Login Failed",Toast.LENGTH_LONG).show();
				}
				new AlertDialog.Builder(this).setMessage(message)
						.setNeutralButton(android.R.string.ok,null).show();
			}

		}
	}

	@Override
	public void signOut() {

	}

	@Override
	public void rateGame() {

	}

	@Override
	public void unlockAchievement(String str) {

	}

	@Override
	public void submitScore(String LeaderBoard, int highScore) {

	}

	@Override
	public void submitLevel(int highLevel) {

	}

	@Override
	public void showAchievement() {

	}

	@Override
	public void showScore(String LeaderBoard) {

	}

	@Override
	public void showLevel() {

	}

	@Override
	public boolean isSignedIn() {
		return false;
	}
}
