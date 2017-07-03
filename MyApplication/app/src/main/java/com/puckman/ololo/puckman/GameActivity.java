package com.puckman.ololo.puckman;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;


import com.puckman.ololo.puckman.Engine.PaintSettings;
import com.puckman.ololo.puckman.Engine.PaintSurface;
import com.puckman.ololo.puckman.Engine.Scene;
import com.puckman.ololo.puckman.Engine.Sprite;
import com.puckman.ololo.puckman.GameLogic.Cell;
import com.puckman.ololo.puckman.GameLogic.Drakes;
import com.puckman.ololo.puckman.GameLogic.Game;
import com.puckman.ololo.puckman.GameLogic.GamePlace;
import com.puckman.ololo.puckman.GameLogic.Player;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity implements OnTouchListener{

    private Game game;
    private PaintSurface sf;
    private Scene mainScene;
    private Sprite nextLvl;
    private Sprite gameOver;
    private Sprite[] drake;
    private Sprite cell_free;
    private Sprite cell_wall;
    private Sprite cell_exit;
    private Timer check;
    private TimerTask checkTask;
    private int w;
    private int h;
    private int step;
    private int taps;
    private static final int botsCnt = 3;
    private static final int checkTaskDelay=1000;
    //
    private MediaPlayer mp;
    private int length=0;
    //
    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private Sensor sensorMagnet;
    private Timer sensorUpdate;
    private int rotation;
    private static final int sensorUpdateDelay=300;
    private static final int highBord = 20;
    private static final int lowBord = -20;
    private float[] valuesAccel = new float[3];
    private float[] valuesMagnet = new float[3];
    private float[] valuesResult = new float[3];
    //
    private SharedPreferences pref;
    private boolean onMusic;
    private boolean onGSensor;
    private static final String landscape = "-1";
    private static final String portrait = "0";
    private static final String auto_orient = "1";
    private static final String pref_orient = "orient_list";
    private static final String pref_music = "music_switch";
    private static final String pref_gsensor = "g_sensor_switch";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        //
        String orient= pref.getString(pref_orient,auto_orient);
        if(orient.equals(landscape)){
          //  System.out.println("Land?");
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (orient.equals(portrait)){
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        onMusic =pref.getBoolean(pref_music,true);
       // System.out.println("Music="+onMusic);
        onGSensor =pref.getBoolean(pref_gsensor,false);
        //System.out.println("G-Sensor="+onGSensor);
        if(onGSensor){
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        //
        //
    }

    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        PaintSettings.generateSettings(metrics);
        check = new Timer();
        drake= new Sprite[botsCnt];
        mainScene = new Scene(0, 0, 2);
        mainScene.setCurLayer(0);
        mainScene.setCurLayer(1);
        sf = new PaintSurface(this, mainScene);
        sf.setOnTouchListener(this);
        h= metrics.heightPixels;
        w = metrics.widthPixels;
        step = Math.min(w, h) / GamePlace.cellCount;
        //  System.out.println("step="+step);
        mainScene.setCurLayer(1);
        setContentView(sf);
        initGame();
        drawMap(game.map);
        drawPlayers(game.user, game.drake);
        stateCheck();
       // System.out.println("OnStart");
    }

    public void onPause(){
        super.onPause();
        check.cancel();
        checkTask.cancel();
        game.stop();
      //  System.out.println("OnPause");
        //
        if(onGSensor) {
            sensorManager.unregisterListener(listener);
            sensorUpdate.cancel();
        }
        //
        if(onMusic) {
            mp.pause();
            length = mp.getCurrentPosition();
        }
        //
    }

    public void onResume(){
        super.onResume();
        game.resume();
       // System.out.println("OnResume");
        //G-sensor
        if(onGSensor) {
            sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_GAME);

            //
            sensorUpdate = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getDeviceOrientation();
                            updateSensControl();
                        }
                    });
                }
            };
            WindowManager windowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
            Display display = windowManager.getDefaultDisplay();
            rotation = display.getRotation();
            sensorUpdate.schedule(task, 0, sensorUpdateDelay);
        }
        //music
        if(onMusic) {
            mp = MediaPlayer.create(this, R.raw.puckman_second);
            mp.setLooping(true);
            if (length == 0) {
                mp.start();
            } else {
                mp.seekTo(length);
                mp.start();
            }
        }
        //
    }

    public void onStop(){
        super.onStop();
        if(onMusic) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public Game onRetainNonConfigurationInstance() {
       // System.out.println("OnRetain");
        return game;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taps=savedInstanceState.getInt("taps");
        if(onMusic) {
            length = savedInstanceState.getInt("length");
        }
      //  System.out.println("OnRestore");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        onRetainNonConfigurationInstance();
        outState.putInt("taps",taps);
        if(onMusic) {
            outState.putInt("length", length);
        }
       // System.out.println("OnSave");
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, valuesAccel, 0, 3);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, valuesMagnet, 0, 3);
                    break;
            }
        }
    };

    private void updateSensControl() {
      //  System.out.println("Orientation: " + (valuesResult));
        if(taps>0){
            int x = (int)valuesResult[2];
            int y = (int) valuesResult[1];
            // System.out.println("Change ps3 position");
            if (x < lowBord) {
                //влево
                game.user.changeDirMove(-1, 0);
            } else if (x > highBord) {
                //pravo
                game.user.changeDirMove(1, 0);
            } else if ( y < lowBord) {
                //vverh
                game.user.changeDirMove(0, 1);
            } else if (y>highBord) {
                //vniz
                game.user.changeDirMove(0, -1);
            }
            taps=taps+1;
        }
      //  System.out.println("tsp="+taps);
    }

    private void getDeviceOrientation() {
        float[] inR = new float[9];
        float[] outR = new float[9];
        SensorManager.getRotationMatrix(inR, null, valuesAccel, valuesMagnet);
        int x_axis = SensorManager.AXIS_X;
        int y_axis = SensorManager.AXIS_Y;
        switch (rotation) {
            case (Surface.ROTATION_0): break;
            case (Surface.ROTATION_90):
                x_axis = SensorManager.AXIS_Y;
                y_axis = SensorManager.AXIS_MINUS_X;
                break;
            case (Surface.ROTATION_180):
                y_axis = SensorManager.AXIS_MINUS_Y;
                break;
            case (Surface.ROTATION_270):
                x_axis = SensorManager.AXIS_MINUS_Y;
                y_axis = SensorManager.AXIS_X;
                break;
            default: break;
        }
        SensorManager.remapCoordinateSystem(inR, x_axis, y_axis, outR);
        SensorManager.getOrientation(outR, valuesResult);
        valuesResult[0] = (float) Math.toDegrees(valuesResult[0]);
        valuesResult[1] = (float) Math.toDegrees(valuesResult[1]);
        valuesResult[2] = (float) Math.toDegrees(valuesResult[2]);
    }

    private boolean initGame(){
        if (game==null){
            if(getLastNonConfigurationInstance()!=null){
                game = (Game)getLastNonConfigurationInstance();
                return true;
            } else{
               // System.out.println("INIT CHECK");
                game = new Game();
                game.game();
                game.start();
                taps=0;
                return true;
            }
        }
        else {
            return false;
        }
    }

    private Sprite screenToCell(int x, int y) {
        int xx = x;
        int yy = y;

        int tmp;
        Sprite p;
        tmp = Math.min(w, h);
        if (mainScene.isHorizontal()) {
            xx = xx - tmp;
        } else {
            yy = yy - tmp;
        }
        xx = xx / step;
        yy = yy / step;
        if (xx > GamePlace.cellCount)
            xx=GamePlace.cellCount;
        if (yy > GamePlace.cellCount)
            yy=GamePlace.cellCount;
        p = new Sprite(xx,yy);
        return p;
    }

    private void drawMap(GamePlace map){
        mainScene.getLayerByNum(1).cleanRenders();
        mainScene.getLayerByNum(0).cleanRenders();
        mainScene.setCurLayer(0);
        int mapW = map.getW();
        int mapH = map.getH();
        for (int i = 0; i < mapH; i++) {
            for (int j = 0; j < mapW; j++) {
                if (map.getCellType(i, j)== Cell.CellType.FLOOR){
                    cell_free = new Sprite(getResources(), R.drawable.bitfloor1fhd);
                    cell_free.setX(i * step);
                    cell_free.setY(j * step);
                    mainScene.addItem(cell_free);
                }
                if (map.getCellType(i, j)== Cell.CellType.TABLE){
                    cell_wall = new Sprite(getResources(), R.drawable.bit_tablefhd);
                    cell_wall.setX(i * step);
                    cell_wall.setY(j * step);
                    mainScene.addItem(cell_wall);
                }
                if (map.getCellType(i, j)== Cell.CellType.TV){
                    cell_exit = new Sprite(getResources(), R.drawable.bit_tabletvfhd);
                    cell_exit.setX(i * step);
                    cell_exit.setY(j * step);
                    mainScene.addItem(cell_exit);
                }
            }
        }
        cell_free=null;
        cell_wall=null;
        cell_exit=null;
        //System.out.println("Draw Map");
    }




    //оставить на случай добавления редактора уровней в игру
  /*  public void reDrawCell(int i, int j){
        mainScene.setCurLayer(0);
        if (gameCheck.field[i][j] == 1) {
            x = new Sprite(getResources(), R.drawable.bit_tablefhd);
            // x.resize(90,90);
            System.out.println("xb"+x.getWidth()*PaintSettings.getScaleX()+"yb"+x.getHeight()*PaintSettings.getScaleY());
            //  x.resize(step*PaintSettings.getScaleX(),step*PaintSettings.getScaleY());
            x.setX(i * step);
            x.setY(j * step);
            System.out.println("RedrawCell 1 : curlvl="+mainScene.getCurLayerNumber());
            mainScene.addItem(x);
        }
        if (gameCheck.field[i][j] == 0) {
            o = new Sprite(getResources(), R.drawable.bitfloor1fhd);
            //   o.resize(90,90);
            //    o.resize(step*PaintSettings.getScaleX(),step*PaintSettings.getScaleY());
            o.setX(i * step);
            o.setY(j * step);
            System.out.println("RedrawCell 2 : curlvl="+mainScene.getCurLayerNumber());
            mainScene.addItem(o);
        }
        if (gameCheck.field[i][j] == 3) {
            s = new Sprite(getResources(), R.drawable.bit_tabletvfhd);
            //   s.resize(90,90);
            //    o.resize(step*PaintSettings.getScaleX(),step*PaintSettings.getScaleY());
            s.setX(i * step);
            s.setY(j * step);
            System.out.println("RedrawCell 3 : curlvl="+mainScene.getCurLayerNumber());
            mainScene.addItem(s);
        }

    }*/

    private void drawPlayers(Player player, Drakes[] drakes){
        mainScene.setCurLayer(1);
        player.connectSprite(getResources(),R.drawable.bit_ps3fhd);
        mainScene.addItem(player);
        for (int i =0;i<botsCnt;i++){
            drakes[i].connectSprite(getResources(),R.drawable.drakelfhd);
            mainScene.addItem(drakes[i]);
        }
      //  System.out.println("Draw Player");
    }

    private void stateCheck(){
        //System.out.println("StateCheck");
        checkTask = new TimerTask() {
            public void run()
            {
               // System.out.println("CheckTask");
                if(game!=null&&mainScene!=null) {
                    if (game.isNextLvl()&&taps>1) {
                        mainScene.clear();
                      //  System.out.println("Next Level");
                        if (PaintSettings.isHorizontal()) {
                            nextLvl = new Sprite(getResources(), R.drawable.nextlevelfhd_land);
                        } else {
                            nextLvl = new Sprite(getResources(), R.drawable.nextlevelfhd);
                        }
                        mainScene.addItem(nextLvl);
                        taps = 0;
                    }
                    if (game.isOver()&&taps>1) {
                        mainScene.clear();
                       // System.out.println("Game Over");
                        if (PaintSettings.isHorizontal()) {
                            gameOver = new Sprite(getResources(), R.drawable.endfhd_land);
                        } else {
                            gameOver = new Sprite(getResources(), R.drawable.endfhd);
                        }
                        mainScene.addItem(gameOver);
                        taps = 0;
                    }
                }
            }
        };

        check.schedule( checkTask, 0, checkTaskDelay );

    }

    @Override
    public boolean onTouch(View arg0, MotionEvent evt) {

       // System.out.println("TAP1="+taps);

        if (evt.getAction() == MotionEvent.ACTION_DOWN) {

            if (!game.isRun() ) {
                mainScene.clear();
                game.start();
                this.drawMap(game.map);
                this.drawPlayers(game.user, game.drake);
            }
            if(taps>0){

                int x = (int) evt.getX();
                int y = (int) evt.getY();
               // System.out.println("Change ps3 position");
                if (x < w/3) {
                    //влево
                    game.user.changeDirMove(-1, 0);
                } else if (x > 2*w/3) {
                    //pravo
                    game.user.changeDirMove(1, 0);
                } else if (x > w/3 && x < 2*w/3 && y > h/2) {
                    //vverh
                    game.user.changeDirMove(0, 1);
                } else {
                    //vniz
                    game.user.changeDirMove(0, -1);
                }
            }
            taps=taps+1;
            return true;
        }
    return false;
}
}

