package com.puckman.ololo.puckman.Engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Ololo on 23.04.2017.
 */

public class Sprite implements Render{
    protected Bitmap bmp;
    protected int x,y;
    protected int dX=0;
    protected int dY=0;
    protected int width=0;
    protected int height=0;
    protected int speed=5;
    protected Matrix sMatr=new Matrix();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(int x) {
        this.x = x;
        refreshAll();
    }

    public Sprite(){}

    public void setY(int y) {
        this.y = y;
        refreshAll();
    }

    public int getDx() {
        return dX;
    }

    public int getDy() {
        return dY;
    }

    public void setDx(int dx) {
        this.dX = dx;
    }

    public void setDy(int dy) {
        this.dY = dy;
    }

    public void update(){
        x = x + dX*speed;
        y = y + dY*speed;

    }

    protected void autoSize(){
        if(PaintSettings.isAutoScale()){
            System.out.println(PaintSettings.getScaleX()+"wtf"+PaintSettings.getScaleY());
            double scal;
            if(PaintSettings.getScaleX()<=PaintSettings.getScaleY()){
                scal=PaintSettings.getScaleX();
            }
            else{
                scal=PaintSettings.getScaleY();
            }
            this.resize(bmp.getWidth()*scal,bmp.getHeight()*scal);
        }
        this.refreshAll();
    }

    public Sprite(Resources res, int id) {
        this.x=0;
        this.y=0;
        this.bmp = BitmapFactory.decodeResource(res, id);
      //  System.out.println("x="+bmp.getHeight()+"y="+bmp.getWidth());
        autoSize();
    }

    public Sprite(int x, int y){
        this.x=x;
        this.y=y;
        //this.bmp=bmp;
       // autoSize();
    }

    public void resize(double newX, double newY) {
        bmp= Bitmap.createScaledBitmap(bmp,(int)newX,(int)newY,true);
        this.refreshAll();
    }

    public  void  refreshAll() {
        if(bmp!=null){
            this.width=bmp.getWidth();
            this.height=bmp.getHeight();
            sMatr.setTranslate(this.x,this.y);
        }
    }

    public  Matrix getMatrix(){
        return sMatr;
    }

    public void setMatrix(Matrix matrix){
        sMatr=matrix;
    }

    @Override
    public void render (Canvas canvas, Paint paint){
        canvas.drawBitmap(bmp,sMatr,paint);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }
}
