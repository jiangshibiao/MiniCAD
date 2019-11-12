import java.awt.*;
import java.io.Serializable;

public class Line extends Shape implements Serializable {
    private int dx, dy;
    @Override
    public void move(int deltax, int deltay){
        super.move(deltax, deltay);
        dx += deltax; dy += deltay;
    }
    public Line(String name, int cx, int cy, int dx, int dy, Color color){
        super(name, cx, cy, color);
        this.dx = dx;
        this.dy = dy;
    }
    public boolean in(int x, int y){
        int px = x - cx, py = y - cy;
        int qx = dx - cx, qy = dy - cy;
        int S = Math.abs(px * qy - qx * py);
        double d = S / Math.sqrt(qx * qx + qy * qy);
        return d <= 3.0;
    }
    public void draw(Graphics g){
        g.setColor(color);
        ((Graphics2D)g).setStroke(new BasicStroke(size));
        g.drawLine(cx, cy, dx, dy);
        ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
    }
}
