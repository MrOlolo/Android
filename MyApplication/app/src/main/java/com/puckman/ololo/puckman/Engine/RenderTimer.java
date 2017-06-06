package com.puckman.ololo.puckman.Engine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.TimerTask;

/**
 * Created by Ololo on 24.04.2017.
 */

public class RenderTimer extends TimerTask {
    private SurfaceHolder holder;
    private Paint paint;
    private Scene scene;
    private Layer layer;
    private Canvas canvas;

    RenderTimer(SurfaceHolder holder, Scene scene){
        this.holder = holder;
        paint = new Paint();
        this.scene = scene;
    }

    @Override
    public void run() {
        try {
            canvas = null;
            canvas = holder.lockCanvas();
            canvas.drawRGB(0,0,0);
            for (int i =0; i< scene.getLayCount();i++){
                layer = scene.getLayerByNum(i);
                if (layer!=null){
                    paint = layer.p;
                    for(Render tmp:layer.renders){
                        tmp.render(canvas,paint);
                    }
                }
            }
            scene.update();
          //  System.out.println("DRASTE");
        } catch (Exception e){

        } finally {
            if (canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
