package com.game.melodi.Maps;

import com.game.melodi.Audiostream.File;
import com.game.melodi.Melodi;

import java.util.Arrays;

import static com.game.melodi.Melodi.player;

/**
 * Created by RabitHole on 12/15/2017.
 */

public class TmapParse {

    float length; //cut by 10000
    Melodi game;
    int[] feed;
    float[] normfeed;

    public TmapParse(Melodi game){
        this.game = game;
        length = game.playerstream.getTotalLength()/10000;
        createFeed();

    }

    private void createFeed(){
        feed = player.player.getInfo();
        normfeed = new float[feed.length];
        for(int i=0;i<normfeed.length;i++){
            normfeed[i]=(float)feed[i];
        }
        for(int i=0;i<normfeed.length-1;i++){
            normfeed[i] -= Math.min(feed[80], feed[feed.length - 1]);
        }
        for(int i=0;i<normfeed.length;i++){
            normfeed[i] /= Math.max(feed[80], feed[feed.length - 1] - Math.min(feed[80], feed[feed.length - 1]));
        }
        System.out.println("NORMALIZED FEED" + Arrays.toString(normfeed));
    }

    private void createTimingPoints(){

    }
}
