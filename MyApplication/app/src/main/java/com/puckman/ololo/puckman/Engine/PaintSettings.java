package com.puckman.ololo.puckman.Engine;

import android.util.DisplayMetrics;

import java.util.Timer;

/**
 * Created by Ololo on 23.04.2017.
 */

public class PaintSettings {
    private static boolean autoScale=false;
    private static int defX=1080;
    private static int defY=1776;
    private static int curX;
    private static int curY;
    private static double scaleX=1;
    private static double scaleY=1;
    private static boolean horizont = false;

    public static  void generateSettings(DisplayMetrics d){
        PaintSettings.curX= d.widthPixels;
        PaintSettings.curY= d.heightPixels;
        PaintSettings.scaleX = (double)PaintSettings.curX/(double)PaintSettings.defX;
        PaintSettings.scaleY = (double)PaintSettings.curY/(double)PaintSettings.defY;
        if (PaintSettings.curX>PaintSettings.curY)
        {
            PaintSettings.horizont=true;
        } else{
            PaintSettings.horizont=false;
        }
        if(PaintSettings.horizont&&PaintSettings.defX==PaintSettings.curY&&PaintSettings.defY==PaintSettings.curX){
            PaintSettings.autoScale=false;
            return;
        }
        if (PaintSettings.scaleX!=1||PaintSettings.scaleY!=1)
        {
            PaintSettings.autoScale=true;
        }
    }

    public static void generateSettings(int w, int h) {
        PaintSettings.curX = w;
        PaintSettings.curY = h;
        PaintSettings.scaleX = (double)PaintSettings.curX/(double)PaintSettings.defX;
       // System.out.println("scaleX="+scaleX);
        PaintSettings.scaleY = (double)PaintSettings.curY/(double)PaintSettings.defY;
       // System.out.println(" PaintSettings.curY="+PaintSettings.curY);
        //System.out.println(" PaintSettings.defY="+ PaintSettings.defY);
      //  System.out.println("scaleY="+scaleY);
        if (PaintSettings.curX>PaintSettings.curY)
        {
            PaintSettings.horizont=true;
        }
        if(PaintSettings.horizont&&PaintSettings.defX==PaintSettings.curY&&PaintSettings.defY==PaintSettings.curX){
            return;
        }
        if (PaintSettings.scaleX!=1||PaintSettings.scaleY!=1)
        {
            PaintSettings.autoScale=true;
        }
    }

    public static boolean isHorizontal() {
        return horizont;
    }

    public static boolean isAutoScale() {
        return autoScale;
    }

    public static double getScaleX() {
        return scaleX;
    }

    public static double getScaleY() {
        return scaleY;
    }

    public static int getCurX() {
        return curX;
    }

    public static int getCurY() {
        return curY;
    }

}


