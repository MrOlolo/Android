package com.puckman.ololo.puckman.GameLogic;

/**
 * Created by Ololo on 10.05.2017.
 */

public class Cell {
    /*   enum CellType:
    * TABLE - wall, you cant step here
    * FLOOR - free, you can step
    * TV - exit, end of level
     */
    public enum CellType {TABLE,FLOOR,TV}

    public static int Size=  90;

    public int posX;

    public int posY;

    public boolean canStep;

    public Cell( int posX, int posY, boolean canStep) {
   //     this.image = image;
        this.posX = posX;
        this.posY = posY;
        this.canStep = canStep;
    }

    public static  Cell CellByType(CellType cellType) {
        if(cellType == CellType.TABLE) {
            return new Cell( 0, 0, false);
        }
        if(cellType == CellType.FLOOR) {
            return new Cell( 0, 0, true);
        }
        if(cellType == CellType.TV) {
            return new Cell( 0, 0, true);
        }
        return null;

    }

}