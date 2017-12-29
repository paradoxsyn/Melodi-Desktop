package com.game.melodi.Maps;

import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;

/**
 * Created by Paradox on 5/15/2017.
 */

public class Terrain {
    /**MID-POINT PLACEMENT ALGORITHM**/

    float[] points;
    double power;
    float displace,roughness;
    int height,width;

    public Terrain(int width, int height, float displace, float roughness) {

        //Gives us power of 2 based on width
        this.width = width;
        this.height = height;
        this.displace = displace;
        this.roughness = roughness;

        power = Math.pow(2, Math.ceil(Math.log(width) / Math.log(2)));
    }
    public float[] getTerrain() {

        //set initial left point
        points = new float[(int)power+200];
        System.out.println(power);

        points[0] = height / 2 + ((float) Math.random() * displace * 2) - displace;

        //set initial right point
        points[(int) power] = height / 2 + ((float) Math.random() * displace * 2 - displace);
        System.out.println(Arrays.toString(points));
        displace *= roughness;

        //Increase number of segments
        for (int i = 1; i < (int)power; i *= 2) {
            //iterate through each segment calculating center point
            for (int j = ((int) power / i) / 2; j < (int)power; j += (int)power / i) {
                points[j] = ((points[j - ((int) power / i) / 2] + points[j + ((int) power / i) / 2])/2);
                points[j] += (Math.random() * displace * 2) - displace;
            }
            //for random range, probably not needed for music
            displace *= roughness;
        }
        return points;
    }

}
