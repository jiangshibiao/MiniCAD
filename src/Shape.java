import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    protected int cx, cy;
    protected Color color;
    protected float size;
    protected String name;
    public String getName(){return name;}
    public Shape(String name, int cx, int cy, Color color){
        this.name = name;
        this.cx = cx;
        this.cy = cy;
        this.color = color;
        size = 3.0f;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public void move(int deltax, int deltay){
        cx += deltax;
        cy += deltay;
    }
    public float getSize(){return size;}
    public void setSize(float size){
        this.size = Math.min(10.0f, size);
        this.size = Math.max(1.0f, this.size);
    }
    public abstract void draw(Graphics g);
    public abstract boolean in(int curX, int curY);
}
