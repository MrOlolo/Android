package com.puckman.ololo.puckman.GameLogic;

import java.util.ArrayList;

/**
 * Created by Ololo on 10.05.2017.
 */

public class FoundWay {
    private ArrayList<Dot > open;
    private ArrayList <Dot> close;
    protected ArrayList <Dot> way;
    private boolean isEnd;
    //private GamePlace chMap = new GamePlace();
    private GamePlace chMap;

    public FoundWay(GamePlace chMap){
        this.chMap=chMap;
    }
    class Dot {
        public int x,y,ex,ey,value;
        public boolean wasHere;
        public Dot(int x,int y,int ex,int ey,int value, boolean wasHere) {
            this.x = x;
            this.y = y;
            this.ex = ex;
            this.ey = ey;
            this.value = value;
            this.wasHere = wasHere;
        }
    }

    private boolean checkCell(int x, int y) {
        // maps=new GamePlace();
        Cell cell =  Cell.CellByType( chMap.getCellType(x, y) );
        // System.out.println(maps.getCellType(x, y));
        return (cell != null && cell.canStep);
    }

    private  void willNext (Dot next, int x, int y, int x2, int y2){
       // System.out.println("Willnext?");
        if (checkCell(x,y)){
            //  System.out.println("Willnext");
            // System.out.println("next "+x+" "+y);
            int value = Math.abs(x-x2)+Math.abs(y-y2);
            Dot ex = new Dot(x,y,next.x,next.y,value,false);
            Dot old = null;
            for(Dot p : close) {
                if(p.x==ex.x&&p.y==ex.y){
                    old=p;
                    break;
                }
            }

            if (old==null||old.value>value) {
                close.add(ex);
                int i = 0;
                for (;i<open.size();i++){
                    if (value<open.get(i).value){
                        open.add(i,ex);
                        //System.out.println("<");
                        break;
                    }
                }
                if(i>=open.size()){
                    //System.out.println(">");
                    open.add(ex);
                }
            }
        }
    }

    private void buildWay(Dot next) {
        //way.clear();
        // System.out.println("WAY");
        while (next.ex!=-1){
            //System.out.println("WAY11");
            for (Dot f:close){
                //System.out.println("WAY22");
                // System.out.println("p "+f.x+" "+f.y);
                way.add(next);
                if(f.x==next.ex&&f.y==next.ey){
                    next = f;
                    //System.out.println("WAY33");
                    break;
                }
            }
        }
    }
    public void findingWay(int x1, int y1, int x2, int y2) {
        isEnd = false;
        open = new <Dot>ArrayList();
        close = new <Dot>ArrayList();
        way = new <Dot>ArrayList();
        //System.out.println("START");
        Dot start = new Dot(x1, y1, -1, -1, Math.abs(x1 - x2) + Math.abs(y1 - y2), true);
        open.add(start);
        close.add(start);
        Dot next;
        while (open.size() > 0) {
            next = open.get(0);
            open.remove(0);
            //System.out.println("kek");
            if (next.x == x2 && next.y == y2) {
                // System.out.println("paw");
                way.add(start);
                buildWay(next);
                isEnd = true;
                // System.out.println("paw-paw");
                break;
            } else {
                next.wasHere = true;
                // System.out.println("lol");
                willNext(next, next.x, next.y + 1, x2, y2);
                willNext(next, next.x + 1, next.y, x2, y2);
                willNext(next, next.x, next.y - 1, x2, y2);
                willNext(next, next.x - 1, next.y, x2, y2);
                //   willNext(next, next.x+1, next.y+1, x2, y2);
                // willNext(next, next.x+1, next.y-1, x2, y2);
                //willNext(next, next.x-1, next.y-1, x2, y2);
                //willNext(next, next.x-1, next.y+1, x2, y2);
            }
        }

        open = null;
        close = null;
    }
}