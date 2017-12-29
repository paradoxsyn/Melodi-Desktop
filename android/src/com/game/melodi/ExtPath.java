package com.game.melodi;

import android.content.Context;
import android.os.Environment;

import com.game.melodi.Loading.PathInterface;

/**
 * Created by Paradox on 6/6/2017.
 */

public class ExtPath implements PathInterface {
    private String path;
    private String path2;

    public ExtPath(){
        getAbsolutePath();
    }

    public String getAbsolutePath(){
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path2 = Environment.getRootDirectory().getAbsolutePath();

        //path =

        System.out.println("Interface path " + path);
        System.out.println("Root dir " + path2);
        //System.out.println("List stuff " + Environment.DIRECTORY_DOWNLOADS. + "\n " + Environment.DIRECTORY_MUSIC + "\n" + Environment.MEDIA_MOUNTED_READ_ONLY );
        return path;
    }
}
