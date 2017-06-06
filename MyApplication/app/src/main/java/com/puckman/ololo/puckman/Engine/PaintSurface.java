package com.puckman.ololo.puckman.Engine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;

/**
 * Created by Ololo on 23.04.2017.
 */

public class PaintSurface extends SurfaceView implements SurfaceHolder.Callback{
    Scene scene;
    RenderTimer rendTim;
    Canvas canv;
    Timer tim= new Timer();

    public PaintSurface(Context context, Scene scene){
        super(context);
        this.scene=scene;
        rendTim= new RenderTimer(this.getHolder(), this.scene);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        tim.schedule(rendTim,0,30);
        canv = this.getHolder().lockCanvas();
        scene.setWidth(canv.getWidth());
       // System.out.println("w="+canv.getWidth()+"h="+canv.getHeight());
        scene.setHeight(canv.getHeight());
        this.getHolder().unlockCanvasAndPost(canv);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        scene.setHeight(h);
        scene.setWidth(w);
        PaintSettings.generateSettings(w,h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        tim.cancel();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }
}
