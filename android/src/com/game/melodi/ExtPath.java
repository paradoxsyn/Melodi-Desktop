package com.game.melodi;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
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

        //String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        //int permsRequestCode = 200;
        //ActivityCompat.requestPermissions(,perms,permsRequestCode);
        //This is how you call permissions except we need a call to the current activity



        //System.out.println("List stuff " + Environment.DIRECTORY_DOWNLOADS. + "\n " + Environment.DIRECTORY_MUSIC + "\n" + Environment.MEDIA_MOUNTED_READ_ONLY );
        return path;
    }
}
