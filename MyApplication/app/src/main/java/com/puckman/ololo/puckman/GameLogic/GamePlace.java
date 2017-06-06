package com.puckman.ololo.puckman.GameLogic;

/**
 * Created by Ololo on 10.05.2017.
 */

public class GamePlace {
    private int eX;
    private int eY;
    public final static int cellCount=12;
    private Cell.CellType[][] map= new Cell.CellType[cellCount][cellCount];
    private int[][] level1={
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {1,1,1,1,2,1,1,1,1,1},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {3,2,2,2,2,2,2,2,2,2}
    };
    private int[][] level2={
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,1,2,2,2,2},
            {2,1,2,2,2,1,2,2,2,2},
            {1,1,2,2,2,1,1,2,2,2},
            {3,2,1,2,2,2,2,1,2,2},
            {2,2,2,1,2,2,2,2,1,2},
            {2,2,2,2,1,2,2,2,1,2},
            {2,2,2,2,2,1,2,2,1,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2}
    };
    private int[][] level3={
            {2,1,2,2,2,2,2,2,1,2},
            {2,1,1,2,2,1,2,1,1,2},
            {2,1,2,1,2,1,2,2,1,2},
            {2,3,2,1,2,1,1,2,2,2},
            {2,2,2,1,2,2,1,2,1,1},
            {1,1,1,1,2,2,1,2,2,2},
            {2,2,2,1,1,2,2,1,1,2},
            {2,1,2,1,2,1,2,1,2,2},
            {2,1,2,1,2,2,2,1,2,1},
            {2,1,2,2,2,2,2,1,2,2}
    };

    public int getH() {

        return map.length;
    }

    public int getW() {

        return map[0].length;
    }


    private Cell.CellType[][] loadMap(int[][] lvl) {
        for (int i = 1; i < getH()-1; i++)
        {
            for (int j = 1; j < getW()-1; j++)
            {

                if (lvl[i-1][j-1]==1){
                    map[i][j] = Cell.CellType.TABLE;
                }else if (lvl[i-1][j-1]==2){
                    map[i][j] = Cell.CellType.FLOOR;
                }else if (lvl[i-1][j-1]==3){
                    map[i][j]= Cell.CellType.TV;
                    eX=j;
                    eY=i;
                }
            }
        }
        return map;
    }


/*    private Cell.CellType[][] loadMap(String path) {
        try
        {
            int k;
            Scanner sc = new Scanner(new File(path));
            for (int i = 1; i < getH()-1; i++)
            {
                for (int j = 1; j < getW()-1; j++)
                {
                    k=sc.nextInt();
                    if (k==1){
                        map[i][j] = Cell.CellType.TABLE;
                    }else if (k==2){
                        map[i][j] = Cell.CellType.FLOOR;
                    }else if (k==3){
                        map[i][j]= Cell.CellType.TV;
                        eX=j;
                        eY=i;
                    }
                }
            }
            sc.close();
            System.out.println("Close");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            return map;
        }
        catch (InputMismatchException e)
        {
            System.out.println("InputMismatchException");
            return map;
        }

        return map;
    }*/

    public void clean() {
        for (int i=0;i<getH(); i++){
            for (int j=0;j<getW();j++){
                if (i ==0||j==0||i==(getH()-1)||j==(getW()-1)){map[i][j]= Cell.CellType.TABLE;}
                else {map[i][j]= Cell.CellType.FLOOR;}
                //   if (i==5) map[i][j]=1;
            }
        }
    }

    public GamePlace() {
        clean();
        map = loadMap(level1);
        //String level_number="/level"+1;
        //System.out.println(level_number);
        //map[fy][fx]=3;
    }

    public GamePlace(int lvl){
        clean();
        if(lvl==1){
            map=loadMap(level1);
        }
        else if(lvl==2){
            map=loadMap(level2);
        }
        else if(lvl==3){
            map=loadMap(level3);
        }
        else {
            map=loadMap(level1);
        }
    }

    public boolean changeCell(int x, int y, Cell.CellType type){
        if(x!=0&&x!=11&&y!=0&&y!=11){
            map[y][x]=type;
            return true;
        }
        return false;
    }

    //public int[][] getMap() {
    //  return map;
    // }

    public int getExitX(){
        return eX;
    }

    public int getExitY(){
        return eY;
    }
    //изменить на перечисление
    public Cell.CellType getCellType(int x, int y) {

        return map[y][x];
    }

}
