package com.game.melodi.Points;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by RabitHole on 2/4/2018.
 */

public class PointSystem extends InputAdapter{

    public Array<Vector2> points = new Array<Vector2>();
    public int errorMargin;
    ShapeRenderer render;


    public PointSystem(){

        render = new ShapeRenderer();
        render.setAutoShapeType(true);

        //points.add(new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2));
        //points.add(new Vector2(Gdx.graphics.getWidth()/2+50,Gdx.graphics.getHeight()/2+50));
    }

    public Array<Vector2> getPoints(){
        return points;
    }

    public ShapeRenderer getRender(){
        return render;
    }



    private double minDistance(float x1, float y1, float x2, float y2, float x3, float y3){
        float px = x2 - x1;
        float py = y2 - y1;
        float temp = (px*px)+(py*py);
        float u=((x3-x1) * px + (y3-y1) * py) / (temp);
        if(u>1){
            u = 1;
        }
        else if(u<0){
            u=0;
        }
        float x = x1 + u * px;
        float y = y1 + u * py;

        float dx = x - x3;
        float dy = y - y3;

        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
    }

}
