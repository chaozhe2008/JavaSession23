package lib;

import java.awt.*;
import java.util.Random;

public class G {
    public static Random RANDOM = new Random();
    public static G.V LEFT = new G.V(-1, 0), RIGHT = new G.V(1, 0);
    public static G.V UP = new G.V(0, -1), DOWN = new G.V(0, 1);
    public static int rnd(int max){
        return RANDOM.nextInt(max);
    }
    public static Color rndColor() {
        return new Color(rnd(256), rnd(256), rnd(256));
    }

    public static void clear(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 5000, 5000);
    }

    public static VS backRect = new VS(0, 0, 5000, 5000);

    //----------------------------V(vector)----------------------//
    public static class V{
        public int x, y;
        public V(){};
        public V(int x, int y){set(x, y);}
        public void set(int x, int y){this.x = x; this.y = y;}
        public void set(V v){this.x = v.x; this.y = v.y;}
        public V(V v){set(v.x, v.y);}
        public void add(V v){x += v.x; y += v.y;};
    }

    //----------------------------VS----------------------//
    public static class VS{
        public V loc = new V(), size = new V();
        public VS(int x, int y, int w, int h){
            loc.set(x, y);
            size.set(w, h);
        }
        public void fill(Graphics g, Color c){
            g.setColor(c);
            g.fillRect(loc.x, loc.y, size.x, size.y);
        }

    }

}


