package games;

import lib.G;
import lib.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SokoBan extends Window {
    public static Board board = new Board();
    public static G.V LEFT = new G.V(-1, 0), RIGHT = new G.V(1, 0);
    public static G.V UP = new G.V(0, -1), DOWN = new G.V(0, 1);
    public SokoBan(){
        super("SokoBan", 1000, 700);
        board.loadStringArray(puz1);
    }
    public static void main(String[] args){
        (PANEL = new SokoBan()).launch();
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        board.show(g);
        if(board.done()){
            g.setColor(Color.BLACK);
            g.drawString("You WOn", 20,30);
        }
    }

    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if (vk == KeyEvent.VK_LEFT){board.go(LEFT);};
        if (vk == KeyEvent.VK_RIGHT){board.go(RIGHT);};
        if (vk == KeyEvent.VK_UP){board.go(UP);};
        if (vk == KeyEvent.VK_DOWN){board.go(DOWN);};
        if (vk == KeyEvent.VK_SPACE){
            board.clear();
            board.loadStringArray(puz1);
        }
        repaint();
    }

    //---------------------Board---------------------
    public static class Board{
        public static final int N = 25;
        public static final int xM = 50, yM = 50, W = 40;
        public static String boardStates = " WPCGgE"; //empty, wall, player, goal, goal achieved, error
        public static Color[] colors = {Color.white, Color.darkGray, Color.GREEN, Color.orange, Color.cyan, Color.BLUE, Color.RED};
        public char[][] b = new char[N][N]; //boards
        public G.V player = new G.V(0, 0);
        public boolean onGoal = false;
        public G.V dest = new G.V(0, 0);
        public Board(){clear();}

        private void clear() {
            for (int r = 0; r < N; r++){
                for (int c = 0; c < N; c++){
                    b[r][c] = ' '; // all white
                }
            }
            player.set(0, 0);
            onGoal = false;
        }
        public void show(Graphics g){
            // fill color
            for (int r = 0; r < N; r++){
                for (int c = 0; c < N; c++){
                    int ndx = boardStates.indexOf(b[r][c]);
                    g.setColor(colors[ndx]);
                    g.fillRect(xM + c * W, yM + r * W, W, W);
                }
            }
        }
        public char ch(G.V v){return b[v.y][v.x];}
        public void set(G.V v, char c){b[v.y][v.x] = c;}
        public void movePerson(){
            boolean res = ch(dest) == 'G'; //goal state
            set(player, onGoal ? 'G' : ' '); //set value dor space player
            set(dest, 'P'); // where the player go
            player.set(dest);
            onGoal = res;

        }

        public void go(G.V v){
            dest.set(player); //copy
            dest.add(v); //actual destination
            if (ch(dest)=='W' || ch(dest) == 'E'){return;} //blocked
            if (ch(dest)==' ' || ch(dest) == 'G'){movePerson(); return;} //can move freely
            if (ch(dest)=='C' || ch(dest) == 'g'){ //pushing a box
                dest.add(v);
                if (ch(dest) != ' ' && ch(dest) != 'G'){return;} // cannot push
                set(dest, ch(dest) == 'G' ? 'g' : 'C'); //out box at the final spot
                dest.set(player);
                dest.add(v);
                set(dest, ch(dest)== 'g' ? 'G' : ' '); //set to space where the box left;
                movePerson();
            }
        }

        public void loadStringArray(String[] a){
            for (int r = 0; r < a.length; r++){
                String s = a[r];
                for (int c = 0; c < s.length(); c++){
                    char ch = s.charAt(c);
                    b[r][c] = boardStates.indexOf(ch) > -1 ? ch : 'E';
                    if (ch == 'P'){
                        player.x = c;
                        player.y = r;
                    }
                }
            }
        }

        public boolean done(){
            for (int r = 0; r < N; r ++){
                for (int c = 0; c < N; c ++){
                    if (b[r][c] == 'C') return false;
                }
            }
            return true;
        }


    }

    public static String[] puz1 = {
            "  WWWWW",
            "WWW   W",
            "WGPC  W",
            "WWW CGW",
            "WGWWC W",
            "W W G WW",
            "WC gCCGW",
            "W   G  W",
            "WWWWWWWW"
    };

}
