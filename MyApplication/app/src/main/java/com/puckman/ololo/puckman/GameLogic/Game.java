package com.puckman.ololo.puckman.GameLogic;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ololo on 10.05.2017.
 */

public class Game implements Runnable {
    private boolean run;
    private boolean gOver;
    private int lvl=1;
    private final int delay = 35;
    public GamePlace map;
    public Player user;
    public Drakes[] drake;
    private boolean nLvl;
    private Thread gameThread= new Thread(this);

    public void game(){
        map = new GamePlace(lvl);
        user = new Player();
        drake=new Drakes[3];
    }

    public void loadEditMap(){
        stop();
        lvl=0;
        map = new GamePlace();
    }

    public void start() {
        if (run) {
            return;
        }
        nLvl=false;
        gOver=false;
        run = true;
        if(lvl==0){
            lvl=1;
        } else {
            map = new GamePlace(lvl);
            user.dX = 0;
            user.dY = 0;
            for (int i = 0; i < 3; i++) {
                drake[i] = new Drakes(map);
            }
            user.posX = Cell.Size;
            user.posY = Cell.Size;
            drake[0].posX = 10 * Cell.Size;
            drake[0].posY = 10 * Cell.Size;
            drake[1].posX = 10 * Cell.Size;
            drake[1].posY = Cell.Size;
            drake[2].posX = Cell.Size;
            drake[2].posY = 10 * Cell.Size;
            //new Thread(this).start();
            //gameThread= new Thread(this);
            if(gameThread.getState()== Thread.State.TERMINATED){
                gameThread= new Thread(this);
            }
            if(gameThread.getState()== Thread.State.NEW){
                gameThread.start();
            }


        }
    }

    public void stop() {
        run = false;
    }

    public void resume() {
        run = true;
       // System.out.println("KEKOS"+gameThread.getState());
        if(gameThread.getState()== Thread.State.TERMINATED){
            gameThread= new Thread(this);
        }
        if(gameThread.getState()== Thread.State.NEW){
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while (run) {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
            update();
        }
    }

    private void update() {
        if ((user.dX != 0 || user.dY != 0) && canMove(user)) {
            // System.out.println(user.dX + user.dY);
            user.posX += user.dX * user.getSpeed();
            user.posY += user.dY * user.getSpeed();
        }
        for(int i=0;i<3;i++) {
            if (Math.abs((drake[i].posX -(user.posX)))<35 && (Math.abs(drake[i].posY -(user.posY))<35)) {
                gameOver();
                break;
            }
            if (user.dY!=0|| user.dX!=0){
                drake[i].drakeDir(Math.round((drake[i].posX)/ Cell.Size), Math.round((drake[i].posY)/ Cell.Size),
                        Math.round(user.posX / Cell.Size), Math.round(user.posY / Cell.Size));}
            if (canMove(drake[i])) {
                drake[i].posX += drake[i].dX * drake[i].getSpeed();
                drake[i].posY += drake[i].dY * drake[i].getSpeed();
            } else if(drake[i].dX!=0){
                drake[i].posY-=drake[i].getSpeed();
            } else if(drake[i].dY!=0){
                drake[i].posX-=drake[i].getSpeed();
            }
        }
        if((Math.ceil(user.posX/Cell.Size)==map.getExitX())&&(Math.ceil(user.posY/Cell.Size)==map.getExitY())){
            nextLevel();
        }
    }

    private void gameOver() {
        stop();
        nLvl=false;
        gOver= true;
        lvl=1;
     //   System.out.println("OLO1");
    }

    public boolean isOver() {
        return gOver;
    }

    public boolean isNextLvl() {
        return nLvl;
    }

    private void nextLevel() {
        stop();
        nLvl=true;
        lvl+=1;
        map = new GamePlace(lvl);
       // System.out.println("LEVEL "+lvl);
       /* java.util.Timer timer2 = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run()
            {
                start();
            }
        };
        timer2.schedule( task, 5000 );*/
    }

    private boolean canMove(int x, int y) {
        Cell cell =  Cell.CellByType( map.getCellType(x, y) );
        return (cell != null && cell.canStep);
    }

    private boolean canMove(Player player) {
        int left, right, up, down;
        boolean canStep = true;
        left = (int)((player.posX) / Cell.Size);
        right = (int) ((player.posX + player.getWidth() -1) / Cell.Size);
        up = (int)((player.posY + player.getSpeed() * player.dY) / Cell.Size);
        down = (int)((player.posY + player.getHeight()+Cell.Size/10+ player.getSpeed() * player.dY -1) / Cell.Size);
        if (player.dY == -1 && !(canMove(left, up) && canMove(right, up))) {
            canStep = false;
        } else if (player.dY == 1 && !(canMove(left, down) && canMove(right, down))) {
            canStep = false;
        }
        left = (int)((player.posX + player.getSpeed() * player.dX) / Cell.Size);
        right = (int) ((player.posX + player.getWidth() +Cell.Size/10+ player.getSpeed() * player.dX -1) / Cell.Size);//
        up = (int) ((player.posY) / Cell.Size);
        down = (int) ((player.posY + player.getHeight()-1) / Cell.Size);
        if (player.dX == -1 && !(canMove(left, up) && canMove(left, down))) {
            canStep = false;
        } else if (player.dX == 1 && !(canMove(right, up) && canMove(right, down))) {
            canStep = false;
        }
        return canStep;
    }

    public boolean isRun() {
        return run;
    }
}
