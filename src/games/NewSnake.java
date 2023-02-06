package games;

import lib.G;
import lib.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class NewSnake extends Window implements ActionListener {
    public static Color cFood = Color.green, cSnake = Color.blue, cBad = Color.red;
    public static Cell food = new Cell();
    public static Cell.List snake = new Cell.List();
    public static Cell crash = null;
    public static Timer timer;
    public NewSnake(){
        super("Snake", 1000, 7000);
        startGame();
        timer = new Timer(200, this);
        timer.start();
    }

    public void startGame(){
        snake.clear();
        snake.iHead = 0;
        snake.growList();
        food.rndLoc();
    }

    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if(vk == KeyEvent.VK_LEFT){snake.direction = G.LEFT;}
        if(vk == KeyEvent.VK_RIGHT){snake.direction = G.RIGHT;}
        if(vk == KeyEvent.VK_UP){snake.direction = G.UP;}
        if(vk == KeyEvent.VK_DOWN){snake.direction = G.DOWN;}
//        if(vk == KeyEvent.VK_A){snake.growList();}
        if(vk == KeyEvent.VK_SPACE){moveSnake();}
//        repaint();
    }

    public static void moveSnake(){
        if(crash != null){return;}
        snake.move();
        Cell head = snake.head();
        if (head.hits(food)){
            snake.growList();
            do{
                food.rndLoc();
            }while(snake.hits(food));
            return;
        }
        if(!head.inBoundary()){
            crash = head;
            snake.stop();
            return;
        }
        if(snake.hits(head)){
            crash = head;
            snake.stop();
            return;
        }
    }

    public void paintComponent(Graphics g) {
        G.clear(g);
        g.setColor(cSnake); snake.show(g);
        g.setColor(cFood); food.show(g);
        if(crash != null){g.setColor(cBad);crash.show(g);}
        Cell.drawBoundary(g);
    }

    public static void main(String[] args){
        (PANEL = new NewSnake()).launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveSnake(); repaint();
    }

    //-------------------------CELL----------------------------
    public static class Cell extends G.V{
        public static final int xM = 35, yM = 35, nX = 30, nY = 20, W = 30;
        public Cell(){
            super(G.rnd(nX), G.rnd(nY));
        }

        public Cell(Cell c){
            super(c.x, c.y);
        }

        public void rndLoc(){
            set(G.rnd(nX), G.rnd(nY));
        }
        public void show(Graphics g){
            g.fillRect(xM + x * W, yM + y * W, W, W);
        }

        public boolean hits(Cell c){return c.x == x && c.y == y;}
        public boolean inBoundary(){return x >= 0 && x < nX && y >= 0 && y < nY;}


        public static void drawBoundary(Graphics g){
            int xMax = xM + nX * W, yMax = yM + nY * W;
            g.setColor(Color.black);
            g.drawLine(xM, yM, xM, yMax);
            g.drawLine(xMax, yM, xMax, yMax);
            g.drawLine(xM, yM, xMax, yM);
            g.drawLine(xM, yMax, xMax, yMax);
        }
        //---------------------Cell List-------------------
        public static class List extends ArrayList<Cell>{
            public static G.V STOPPED = new G.V(0, 0);
            public G.V direction = STOPPED;
            public int iHead = 0;
            public void show(Graphics g){
                for (Cell c: this){
                    c.show(g);
                }
            }
            public void growList(){
                int iTail = (iHead + 1) % size();
                Cell cell = size()==0 ? new Cell() : new Cell(get(iTail));
                add(size() == 0 ? 0 : iTail, cell);
            }

            public void move(){
                if(direction == STOPPED) return;
                int iTail = (iHead + 1) % size();
                Cell tail = get(iTail); // set tail to be new head
                tail.set(get(iHead));// where the old head is
                tail.add(direction);
                iHead = iTail; // new head is the former tail
            }

            public Cell head(){return get(iHead);}
            public void stop() {direction = STOPPED;}
            public boolean hits(Cell t){ //target cell
                for(Cell c: this){
                    if(c != t && c.hits(t)) return true;
                }
                return false;
            }
        }
    }


}
