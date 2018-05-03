package com.game.melodi.Input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * Created by RabitHole on 4/8/2018.
 */

public class MultiInputListener extends ActorGestureListener {

    int id, key;
    TextButton txtbtn;
    boolean picked;
    int numBtns;

    public MultiInputListener(int key, TextButton txtbtn, int numBtns){
        this.key = key;
        this.id = txtbtn.hashCode();
        this.txtbtn = txtbtn;
        this.numBtns = numBtns;
    }

    public int getButtonID(){
        return id;
    }

    public boolean getPicked(){
        return picked;
    }

    public int getKey(){
        return key;
    }

    public void touchDown(InputEvent event, float x, float y,int pointer, int button){

            if (id == txtbtn.hashCode()) {
                picked = true;
                System.out.println("TEST" + txtbtn.getText());
            }
        //System.out.println("ID" + id);
        //System.out.println("Key" + key);
        //System.out.println("Hash" + txtbtn.hashCode());
    }
}
