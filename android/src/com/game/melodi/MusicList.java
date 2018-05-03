package com.game.melodi;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.game.melodi.Loading.AndroidInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RabitHole on 3/31/2018.
 */

public class MusicList implements AndroidInterface {

    Context context;

    public MusicList(Context context) {
        this.context = context;
        getMusicList();
        getNameList();
        getDataList();
    }

    public List<String> getMusicList() {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor curs = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        List<String> songs = new ArrayList<String>();


        while(curs.moveToNext()) {
            songs.add(curs.getString(0) + "||" + curs.getString(1) + "||" + curs.getString(2) + "||" + curs.getString(3) + "||" + curs.getString(4) + "||" + curs.getString(5));
        }

        return songs;
    }

    public List<String> getNameList(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor curs = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        List<String> names = new ArrayList<String>();

        while(curs.moveToNext()) {
            names.add(curs.getString(2));
        }

        return names;

    }

    public List<String> getDataList(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor curs = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        List<String> data = new ArrayList<String>();

        while(curs.moveToNext()) {
            data.add(curs.getString(3));
        }

        return data;

    }
}
