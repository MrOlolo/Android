package com.puckman.ololo.puckman.GameLogic;

/**
 * Created by Ololo on 10.05.2017.
 */

public class LevelEditor {
//    // private int lvl=1;
//
//    private Canvas canvas;
//    private GameView editView;
//    private GamePlace map;
//    private int mapH;
//    private int mapW;
//
//    public Canvas getCanvas() {
//        return canvas;
//    }
//
//    private MouseAdapter MouseListener =  new MouseAdapter() {
//        public void mousePressed(MouseEvent me) {
//            int lx,ly;
//            Cell.CellType type;
//            boolean ek=foundEx();
//            int x = Math.round(me.getX()/Cell.Size);
//            int y = Math.round(me.getY()/Cell.Size);
//            //System.out.println(y);
//            type=map.getCellType(x,y);
//            if(type== Cell.CellType.TV){
//                type= Cell.CellType.TABLE;
//            }
//            else if(type== Cell.CellType.FLOOR&&!ek){
//                type= Cell.CellType.TV;
//            }
//            else if (type== Cell.CellType.FLOOR&&ek){
//                type= Cell.CellType.TABLE;
//            }
//            else if (type== Cell.CellType.TABLE){
//                type= Cell.CellType.FLOOR;
//            }
//            if (map.changeCell(x,y,type)) {
//                update(map);
//            }
//
//        }
//    };
//
//    private boolean foundEx(){
//        for(int i =1;i<mapW-1;i++) {
//            for (int j = 1; j < mapH-1; j++) {
//                if (map.getCellType(j,i)== Cell.CellType.TV)
//                    return true;
//            }
//        }
//        return false;
//    }
//
//
//    /*private void drawMap() {
//        Cell cell;
//        for (int i = 0; i < mapH; i++) {
//            for (int j = 0; j < mapW; j++) {
//                cell = Cell.CellByType(map.getCellType(j, i));
//                cell.posX = j * Cell.Size;
//                cell.posY = i * Cell.Size;
//                canvas.addRender(cell);
//            }
//        }
//    }*/
//    public void editor(){
//        canvas = new Canvas();
//        map = new GamePlace();
//        mapW = map.getW();
//        mapH = map.getH();
//        canvas.setFocusable(true);
//        canvas.addMouseListener(MouseListener);
//        editView = new GameView(canvas);
//        editView.drawMap(map);
//    }
//
//    public void discard(){
//        map = new GamePlace();
//        update(map);
//    }
//
//    public void cleanMap() {
//        map.clean();
//        update(map);
//    }
//
//    public void save(String level){
//        File editMap;
//        if (level.isEmpty()){
//            editMap = new File("editMap.txt");
//        }
//        else {
//            editMap = new File(level+".txt");
//        }
//        System.out.println(editMap);
//        int k=2;
//        try{
//            BufferedWriter savemap = new BufferedWriter(new FileWriter(editMap));
//            for(int i =1;i<mapH-1;i++) {
//                for (int j = 1; j < mapW-1; j++) {
//                    if (map.getCellType(j,i)== Cell.CellType.TABLE){
//                        k=1;
//                    }else if (map.getCellType(j,i)== Cell.CellType.FLOOR){
//                        k=2;
//                    }else if (map.getCellType(j,i)== Cell.CellType.TV){
//                        k=3;
//                    }
//                    savemap.write(k+" ");
//                }
//                savemap.write("\n");
//            }
//            System.out.println("soidet)");
//            savemap.flush();
//            savemap.close();
//        }catch (IOException ex){
//            ex.printStackTrace();
//            System.out.println("Chet ne poshlo");
//        }
//    }
//
//    private void update(GamePlace map){
//        editView.drawMap(map);
//        editView.updateView();
//    }
}