package games;
import lib.G;
import lib.Window;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Tetris extends Window implements ActionListener {
    public static Timer timer;
    public static final int H = 20, W = 10, C = 25;
    public static final int xM = 50, yM = 50;
    public static Color[] color = {Color.RED, Color.GREEN, Color.BLUE,
            Color.ORANGE, Color.CYAN, Color.YELLOW,
            Color.MAGENTA, Color.BLACK, Color.PINK};
    public static Shape[] shapes = {Shape.Z, Shape.S, Shape.J, Shape.L, Shape.I, Shape.O, Shape.T};
    public static int[][] well = new int[W][H];
    public static int iBack = 7; //background color : black
    public static int zap = 8; // index of empty space
    public static Shape shape;

    public static int time = 1, iShape = 0;


    public Tetris(){
        super("Tetris", 1000, 700);
        shape = shapes[G.rnd(7)];
        clearWell();
        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        time++;
        if(time == 30){
            time = 0;
            shape.drop();
        }
        unZapWell();
        showWell(g);
        shape.show(g);
    }

    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if(vk == KeyEvent.VK_LEFT){shape.slide(G.LEFT);}
        if(vk == KeyEvent.VK_RIGHT){shape.slide(G.RIGHT);}
        if(vk == KeyEvent.VK_UP){shape.safeRot();}
        if(vk == KeyEvent.VK_DOWN){shape.drop();}
        repaint();
    }

    public static void clearWell(){
        for(int x = 0; x < W; x++){
            for(int y = 0; y < H; y++){
                well[x][y] = iBack;
            }
        }
    }

    public static void showWell(Graphics g){
        for(int x = 0; x < W; x++){
            for(int y = 0; y < H; y++){
                g.setColor(color[well[x][y]]);
                int xx = xM + C * x, yy = yM + C * y;
                g.fillRect(xx, yy, C, C);
                g.setColor(Color.BLACK);
                g.drawRect(xx, yy, C, C);
            }
        }
    }

    public static void zapWell(){
        for(int y = 0; y < H; y++){zapRow(y);}
    }

    public static void zapRow(int y){
        for (int x = 0; x < W; x++){
            if(well[x][y] == iBack){return;}
        }
        for (int x = 0; x < W; x++){
           well[x][y] = zap;
        }
    }

    public static void unZapWell(){
        boolean done = false;
        for(int y = 1; y < H; y++){
            for (int x = 0; x < W; x++){
                if(well[x][y-1] != zap && well[x][y] == zap){
                    done = true;
                    well[x][y] = well[x][y-1];
                    well[x][y-1] = (y==1) ? iBack : zap;
                }
            }
            if (done) return;
        }
    }



    public static void dropNewShape() {
        shape = shapes[G.rnd(7)];
        shape.loc.set(4,0); //set loc of new shape

    }

    public static void main(String[] args){
        (PANEL = new Tetris()).launch();
    }
    //-------------------Shape-----------------//

    public static class Shape{
        public static Shape Z,S,J,L,I,O,T;
        public G.V temp = new G.V(0,0);
        public static Shape cds = new Shape(new int[] {0, 0, 0, 0, 0, 0, 0, 0}, 0);
        public G.V[] a = new G.V[4]; //array that holds 4 shapes for each shape
        public int iColor; //color index
        public G.V loc = new G.V(0, 0);

        public Shape(int[] xy, int iC) {
            for (int i = 0; i < 4; i++) {
                a[i] = new G.V(0, 0);
                a[i].set(xy[i * 2], xy[i * 2 + 1]); //x coord is the even # in the AL, Y is odd
            }
            iColor = iC;
        }

        public void show(Graphics g){
            g.setColor(color[iColor]);
            for(int i = 0; i < 4; i++){
                g.fillRect(x(i), y(i), C, C); //interior color of squares
            }

            g.setColor(Color.BLACK);
            for(int i = 0; i < 4; i++){
                g.drawRect(x(i), y(i), C, C); //black border
            }

        }

        public int x(int i){return xM + C * (a[i].x + loc.x);}

        public int y(int i){return yM + C * (a[i].y + loc.y);}

        public void rot(){
            temp.set(0,0);
            for(int i = 0; i < 4; i++){
                a[i].set(-a[i].y, a[i].x); //90 degree rotation
                if (temp.x > a[i].x){temp.x = a[i].x;} //track the min of x and y
                if (temp.y > a[i].y){temp.y = a[i].y;} //make the temp min
            }
            temp.set(-temp.x, -temp.y);
            for(int i = 0; i < 4; i++){
                a[i].add(temp); //90 degree rotation
            }
        }

        public void safeRot(){
            rot();
            cdsSet();
            if(collisionDetected()){
                rot();rot();rot();
            }
        }
        public static boolean collisionDetected(){ // make sure in boundary
            for (int i = 0; i < 4; i++){
                G.V v = cds.a[i];
                if(v.x < 0 || v.x >= W || v.y < 0 || v.y >= H){return true;}
                if(well[v.x][v.y] < iBack){return true;} //testing dead block
            }
            return false;
        }

        public void cdsSet(){
            for (int i = 0; i < 4; i++){
                cds.a[i].set(a[i]);
                cds.a[i].add(loc);
            }
        }

        public void cdsGet(){
            for (int i = 0; i < 4; i++){a[i].set(cds.a[i]);}
        }

        public void cdsAdd(G.V v){
            for (int i = 0; i < 4; i++){cds.a[i].add(v);}
        }


        public void slide(G.V dX){
            cdsSet();
            cdsAdd(dX);
            if(collisionDetected()){return;}
            //cdsGet();
            loc.add(dX); //slide is updating loc rather than changeing it
        }

        public void drop(){
            cdsSet();
            cdsAdd(G.DOWN);
            if(collisionDetected()){ //copy the shape to well and draw a new shape
                copyToWell();
                zapWell();
                dropNewShape();
                return;
            }
            loc.add(G.DOWN);
        }


        public void copyToWell() {
            for (int i = 0; i<4; i++){
                well[a[i].x + loc.x][a[i].y + loc.y] = iColor; //copy to well
            }
        }

        static{
            Z = new Shape(new int[]{0, 0, 1, 0, 1, 1, 2, 1}, 0);
            S = new Shape(new int[]{0, 1, 1, 0, 1, 1, 2, 0}, 1);
            J = new Shape(new int[]{0, 0, 0, 1, 1, 1, 2, 1}, 2);
            L = new Shape(new int[]{0, 1, 1, 1, 2, 1, 2, 0}, 3);
            I = new Shape(new int[]{0, 0, 1, 0, 2, 0, 3, 0}, 4);
            O = new Shape(new int[]{0, 0, 1, 0, 0, 1, 1, 1}, 5);
            T = new Shape(new int[]{0, 1, 1, 0, 1, 1, 2, 1}, 6);

        }

    }
}

