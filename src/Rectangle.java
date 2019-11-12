import java.awt.*;
import java.io.Serializable;

public class Rectangle extends Shape implements Serializable {
    private int width, height;
    public Rectangle(String name, int cx, int cy, int width, int height, Color color){
        super(name, cx, cy, color);
        this.name = name;
        this.width = width;
        this.height = height;
    }
    public boolean in(int x, int y){
        return x >= cx && x <= cx + width && y >= cy && y <= cy + height;
    }
    public void draw(Graphics g){
        g.setColor(color);
        ((Graphics2D)g).setStroke(new BasicStroke(size));
        g.drawRect(cx, cy, width, height);
        ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
    }
}
