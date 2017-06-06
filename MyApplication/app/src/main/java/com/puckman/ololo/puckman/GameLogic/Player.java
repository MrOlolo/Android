package com.puckman.ololo.puckman.GameLogic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.puckman.ololo.puckman.Engine.Render;
import com.puckman.ololo.puckman.Engine.Sprite;
import com.puckman.ololo.puckman.R;

/**
 * Created by Ololo on 10.05.2017.
 */

public class Player extends Sprite {
    private int width = 60;
    private int height = 70;
    private Bitmap tmp;
    protected int speed = 7;
    protected int angle=0;
    protected int dX = 0;
    protected int dY = 0;
    protected int posX = 0;
    protected int posY = 0;

    protected String right;
    protected String left;
    protected String up;
    protected String down;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void connectSprite(Resources res, int id) {
        x=this.posX;
        y=this.posY;
        bmp = BitmapFactory.decodeResource(res, id);
        tmp= bmp;
       // System.out.println("xpl="+bmp.getHeight()+"ypl="+bmp.getWidth());
        autoSize();
    }

    public Player(){}
    public int getSpeed(){
        return speed;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    @Override
    public void update(){
       // System.out.println("pochemu");
        x = posX + dX*speed;
        y = posY + dY*speed;
        playerPic(dX,dY);
        this.refreshAll();
      //  System.out.println("LITTLE CHECk"+x);
    }


    public void changeDirMove(int dX,int dY){
        this.dX=dX;
        this.dY=dY;
    }

    public void playerPic(int dx,int dy){
        int h,w;
        Matrix matrix = new Matrix();
        h = (height > width) ?  height:width;
        w = (height > width) ?  width:height;
        if (dx==1||(dx==0&&dy==0)){
           // sMatr.setRotate(90);
            matrix.postRotate(angle+0);
            bmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            height=h;
            width=w;
            matrix.reset();
        } else if(dx==-1) {
          matrix.setRotate(angle+180);
            bmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
           // imageSrc = new ImageIcon(getClass().getResource(left)).getImage();
            height=h;
            width=w;
            matrix.reset();
        } else if(dy==-1) {
            matrix.setRotate(angle+-90);
            bmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
           // imageSrc = new ImageIcon(getClass().getResource(up)).getImage();
            height=w;
            width=h;
            matrix.reset();
        } else if(dy==1) {
           matrix.setRotate(angle+90);
            bmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
           // imageSrc = new ImageIcon(getClass().getResource(down)).getImage();
            height=w;
            width=h;
            matrix.reset();
        }
    }


}