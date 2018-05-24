package com.game.melodi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.game.melodi.Loading.AdActivityHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by RabitHole on 5/23/2018.
 */

public class AdHandler implements AdActivityHandler{

    public AdView adView;
    public RelativeLayout.LayoutParams adParams;
    Handler handler;
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    public AdHandler(){
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case SHOW_ADS:{
                            adView.setVisibility(View.VISIBLE);
                            break;
                        }
                        case HIDE_ADS:{
                            adView.setVisibility(View.GONE);
                            break;
                        }
                    }
                }
            };


    }

    @Override
    public void showAds(boolean show){
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }

    public void adViewInit(Context context){
        adView = new AdView(context);
        adView.setAdSize(AdSize.LARGE_BANNER);
        // adView.setAdUnitId("ca-app-pub-8441412014443121/4710719712");
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("F3DAF8ABBCD4EEA3519E36810FC80674").build();
        adView.loadAd(adRequest);

        adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public RelativeLayout.LayoutParams getAdParams(){
        return adParams;
    }

    public AdView getAdView(){
        return adView;
    }
}
