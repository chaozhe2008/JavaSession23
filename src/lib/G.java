package lib;

import java.awt.*;
import java.util.Random;

public class G {
    public static Random RANDOM = new Random();
    public static int rnd(int max){
        return RANDOM.nextInt(max);
    }
    public static Color rndColor() {
        return new Color(rnd(256), rnd(256), rnd(256));
    }

    public static VS backRect = new VS(0, 0, 5000, 5000);

    //----------------------------V(vector)----------------------//
    public static class V{
        public int x, y;
        public V(){};
        public V(int x, int y){set(x, y);}
        public void set(int x, int y){this.x = x; this.y = y;}
        public V(V v){set(v.x, v.y);}
        
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


