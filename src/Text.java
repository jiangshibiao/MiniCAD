import java.awt.*;
import java.io.Serializable;

public class Text extends Shape implements Serializable {
    private String content;
    @Override
    public void move(int deltax, int deltay){
        super.move(deltax, deltay);
    }
    public Text(String name, int cx, int cy, String content, Color color){
        super(name, cx, cy, color);
        this.content = content;
    }
    public boolean in(int x, int y){
        int width = (int)(size * 20 * content.length());
        int height = (int)(size * 20);
        return x >= cx && x <= cx + width && y <= cy && y >= cy - height;
    }
    public void draw(Graphics g){
        g.setColor(color);
        ((Graphics2D)g).setStroke(new BasicStroke(size));
        g.setFont(new Font("宋体",Font.BOLD,(int)(size * 20)));
        g.drawString(content, cx, cy);
        ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
    }
}
