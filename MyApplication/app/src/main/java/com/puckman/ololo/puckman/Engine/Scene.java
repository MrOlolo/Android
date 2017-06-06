package com.puckman.ololo.puckman.Engine;

/**
 * Created by Ololo on 23.04.2017.
 */

public class Scene {
    private Layer[] layers;
    private int curLayer;


    private boolean horiz;
    private int layerCount;
    private int topLayer;
    private int botLayer=0;
    private int width;
    private int height;

    public Scene(int width, int height, int layerCount){
        this.width=width;
        this.height=height;
        if(width>height){
            horiz=true;
        }
        this.layerCount=layerCount;
        layers = new Layer[layerCount];
        curLayer=0;
        topLayer=layerCount-1;
        for(int i=botLayer; i<layerCount; i++){
            layers[i]= new Layer(i);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setCurLayer(int i) {
        if (i <= topLayer)
            curLayer = i;
        else
            curLayer = topLayer;
    }

    public void addItem(Render item) {
        layers[curLayer].addRender(item);
    }

    public int getCurLayerNumber() {
        return this.curLayer;
    }

    public Layer getCurLay() {
        return layers[this.curLayer];
    }

    public void clear() {
        this.layers[this.curLayer].cleanRenders();
    }

    public int getLayCount() {
        return layerCount;
    }

    public void resize(int newx, int newy)
    {
        for (Layer layer: layers)
        {
            layer.resize(newx, newy);
        }
    }

    public Layer getLayerByNum(int num)
    {
        if (num<this.layerCount)
        {
            return layers[num];
        }
        return null;
    }

    public boolean isHorizontal() {
        return horiz;
    }

    public void update() {
        for (Layer l : layers) {
            l.update();
        }
    }
}
