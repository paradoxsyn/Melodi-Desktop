package com.game.melodi.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.game.melodi.Audiostream.File;

/**
 * Created by RabitHole on 12/1/2017.
 */

public class TmapGen {
    FileHandle output;
    File newMap;
    float length;

    public TmapGen(float length, String file){
        this.length = length;
        output = Gdx.files.local(file);
        System.out.println(output);

        length = length / 1000;

        newMap = new File(output.nameWithoutExtension() + ".tmx");
        System.out.println(newMap.getAbsolutePath());
        System.out.println(newMap.getName());
        output = Gdx.files.local(newMap.toString());
        if(output.exists()) {
            System.out.println("This exists at " + output.exists());
            output.writeString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<map version=\"1.0\" tiledversion=\"1.0.3\" orientation=\"orthogonal\" renderorder=\"right-down\" width=\"64\" height=\"64\" tilewidth=\"16\" tileheight=\"16\" nextobjectid=\"7\">\n" +
                    "<tileset firstgid=\"1\" source=\"tester.tsx\"/>\n" +
                    "<layer name=\"Tile Layer 1\" width=\"" + length + "\""+ "height=\"32\">\n" +
                    "<data encoding=\"csv\">\n" +
                    "", false);
       }

       System.out.println(output.readString());

    }

    public void terrainGen(){
        for(int i=0;i<length;i++){

        }
    }
}
