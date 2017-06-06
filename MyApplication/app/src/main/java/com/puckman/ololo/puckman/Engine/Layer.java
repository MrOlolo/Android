package com.puckman.ololo.puckman.Engine;

import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Ololo on 23.04.2017.
 */

public class Layer {
    public ArrayList<Render> renders= new ArrayList<>();
    int level;
    public Paint p;
    private boolean processing=false;
    public Layer(int level){
        processing=true;
        this.level=level;
        p=new Paint();
        processing=false;
    }

    public void addRender(Render render){
        processing=true;
        renders.add(render);
        processing=false;
    }

    public void removeRender(Render delRender){
        processing=true;
        renders.remove(delRender);
        processing=false;
    }

    public void cleanRenders(){renders.clear();}

    public void update(){
        processing = true;
        for(Render render:renders){
            if(render!=null){
                ((Sprite)render).update();
            }
        }
        processing=false;
    }

    public void resize(int newX, int newY){
        processing=true;
        for(Render render:renders){
            if(render!=null){
                ((Sprite)render).resize(newX,newY);
            }
        }
        processing=false;

    }

    public boolean isProcessing(){
        return processing;
    }
}
