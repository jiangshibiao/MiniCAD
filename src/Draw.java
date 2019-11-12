import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Draw extends JFrame{
    private final int HEIGHT = 1000;
    private final int WIDTH = 1000;
    private final int ICONWIDTH = 60;
    private final int COLORWIDTH = 30;
    private DrawListener myMouse;

    Draw() {
        super("JSB's Painting");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setLayout(new BorderLayout());

        myMouse = new DrawListener();
        this.addMouseListener(myMouse);
        this.addMouseMotionListener(myMouse);

        JPanel board = new JPanel();
        JPanel toolBar = new JPanel();
        this.add(board, BorderLayout.CENTER);
        this.add(toolBar, BorderLayout.EAST);

        toolBar.setPreferredSize(new Dimension(COLORWIDTH * 3, HEIGHT));
        toolBar.setBackground(Color.LIGHT_GRAY);

        String[] shapeName = { "Choose", "Camera", "Save", "Open", "Delete", "Line", "Rectangle", "Oval", "Text", "Undo", "Redo" };
        for (int i = 0; i < shapeName.length; i++) {
            JButton nowButton = new JButton(shapeName[i]);
            nowButton.setIcon(new ImageIcon("src/images/" + shapeName[i] + ".png"));
            nowButton.setPreferredSize(new Dimension(ICONWIDTH, ICONWIDTH));
            nowButton.addActionListener(myMouse);
            toolBar.add(nowButton);
        }

        Color[] colorArray = {Color.red, Color.pink, Color.orange, Color.yellow, Color.green, Color.blue, Color.cyan,
                Color.black, Color.gray, Color.white};
        for (int i = 0; i < colorArray.length; i++) {
            JButton nowButton = new JButton();
            nowButton.setBackground(colorArray[i]);
            nowButton.setPreferredSize(new Dimension(COLORWIDTH , COLORWIDTH));
            nowButton.addActionListener(myMouse);
            toolBar.add(nowButton);
        }

        String[] optName = { "Thicker", "Thinner"};
        for (int i = 0; i < optName.length; i++) {
            JButton nowButton = new JButton(optName[i]);
            nowButton.setIcon(new ImageIcon("src/images/" + optName[i] + ".png"));
            nowButton.setPreferredSize(new Dimension(COLORWIDTH, COLORWIDTH));
            nowButton.addActionListener(myMouse);
            toolBar.add(nowButton);
        }

        this.setVisible(true);

        myMouse.myGraph = (Graphics2D)this.getGraphics(); //Get the current Graphics
        myMouse.myFrame = this; //It'll be convenient for repaint()
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        this.getContentPane().setBackground(Color.white);
        myMouse.paint();
    }
    public static void main(String argv[]){
        new Draw();
    }
}
class DrawListener implements MouseListener, MouseMotionListener, ActionListener{
    private final int HEIGHT = 930;
    private final int WIDTH = 1000;
    private Stack<Shape>DoList = new Stack<>();
    private Stack<Shape>TodoList = new Stack<>();

    private enum Operation{Texting, Saving, Opening, Screening, OTHER};

    private JFrame fileFrame;
    private JTextField content;
    private JButton OKbutton;
    private BufferedImage bi;
    private Operation operation;

    JFrame myFrame;
    Graphics2D myGraph;
    Color nowColor = Color.black;
    String nowSelect = "Choose";
    Shape nowShape = null;
    Boolean Dragging = false;

    private int curX, curY, stX, stY;

    DrawListener(){
        OKbutton = new JButton("OK");
        OKbutton.addActionListener(this);
        curX = -1; curY = -1;
        operation = Operation.OTHER;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        curX = stX = e.getX();
        curY = stY = e.getY();
        if (nowSelect.equals("Choose")) {
            for (Shape cur : DoList)
                if (cur.in(curX, curY))
                    nowShape = cur;
        }
        else if (nowSelect.equals("Text")){
            buildFrame("Texting...", "Please enter a string with length smaller than 32.");
            operation = Operation.Texting;
        }
    }
    void buildFrame(String frameName, String labelName){
        JLabel label = new JLabel(labelName);
        content = new JTextField(32);
        fileFrame = new JFrame(frameName);
        Container contentPane = fileFrame.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(label);
        contentPane.add(content);
        contentPane.add(OKbutton);
        fileFrame.setSize(400, 150);
        fileFrame.setLocationRelativeTo(null);
        fileFrame.setVisible(true);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        Dragging = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        int edX = e.getX(), edY = e.getY();
        int x1 = Math.min(stX, edX), x2 = Math.max(stX, edX);
        int y1 = Math.min(stY, edY), y2 = Math.max(stY, edY);

        if (nowSelect.equals("Choose")) {
            if (nowShape != null) {
                nowShape.move(edX - curX, edY - curY);
                DoList.remove(nowShape);
                DoList.push(nowShape);
                myFrame.paint(myGraph);
            }
        }
        else if (nowSelect.equals("Line") || nowSelect.equals("Rectangle") || nowSelect.equals("Oval")){
            if (Dragging) {
                if (!DoList.isEmpty())
                    DoList.pop();
            }
            else {
                Dragging = true;
            }
            if (nowSelect.equals("Line"))
                nowShape = DoList.push(new Line("Line", stX, stY, edX, edY, nowColor));
            else if (nowSelect.equals("Rectangle"))
                nowShape = DoList.push(new Rectangle("Rectangle", x1, y1, x2-x1, y2-y1, nowColor));
            else if (nowSelect.equals("Oval")){
                nowShape = DoList.push(new Oval("Oval", x1, y1, x2-x1, y2-y1, nowColor));
            }
            myFrame.paint(myGraph);
        }
        curX = edX; curY = edY;
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if ("".equals(e.getActionCommand())) {
            JButton nowButton = (JButton) e.getSource();
            nowColor = nowButton.getBackground();
            if (nowShape != null){
                nowShape.setColor(nowColor);
                myFrame.paint(myGraph);
            }
        } else {
           nowSelect = e.getActionCommand();
           if (nowSelect.equals("Choose")){
               nowShape = null;
           }
           else if (nowSelect.equals("Delete")){
               if (nowShape != null){
                   DoList.remove(nowShape);
                   myFrame.paint(myGraph);
               }
               nowSelect = "Choose";
           }
           else if (nowSelect.equals("Save")){
               operation = Operation.Saving;
               buildFrame("Saving...", "Please enter your saving path.");
           }
           else if (nowSelect.equals("Open")){
               operation = Operation.Opening;
               buildFrame("Opening...", "Please enter your opening path.");
           }
           else if (nowSelect.equals("Camera")){
               operation = Operation.Screening;
               buildFrame("Screening...", "Please enter your saving path.");
               bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
               Graphics biGraph = bi.getGraphics();
               for (Shape sh: DoList)
                   sh.draw(biGraph);
               biGraph.dispose();
           }
           else if (nowSelect.equals("OK")){
               fileFrame.setVisible(false);
               if (!content.getText().isEmpty()) {
                   System.out.println(content.getText());
                   switch (operation){
                       case Texting:
                           nowShape = DoList.push(new Text("Text", curX, curY, content.getText(), nowColor));
                           myFrame.paint(myGraph);
                           break;
                       case Saving:
                           ObjectOutputStream oos;
                           try {
                               oos = new ObjectOutputStream(new FileOutputStream(content.getText()));
                               oos.writeObject(DoList);
                           }
                           catch (IOException ie){
                           }
                           break;
                       case Opening:
                           ObjectInputStream ios;
                           try {
                               System.out.println("Trying. " + content.getText());
                               ios = new ObjectInputStream(new FileInputStream(content.getText()));
                               System.out.println("DOING END.");
                               Object obj = ios.readObject();
                               DoList = (Stack<Shape>)obj;
                               System.out.println("DOING END.");
                               for (Shape sh: DoList)
                                   System.out.println(sh.name);
                               myFrame.paint(myGraph);
                           }
                           catch (Exception ie){
                               System.out.println(ie.getMessage());
                           }
                           break;
                       case Screening:
                           try{
                               ImageIO.write(bi, "PNG", new File(content.getText()));
                           }
                           catch (IOException ie){
                               System.out.println(ie.getMessage());
                           }
                   }
               }
               operation = Operation.OTHER;
           }
           else if (nowSelect.equals("Undo") && !DoList.isEmpty()){
               TodoList.push(DoList.pop());
               myFrame.paint(myGraph);
           }
           else if (nowSelect.equals("Redo") && !TodoList.isEmpty()) {
               System.out.println(TodoList.peek().getName());
               DoList.push(TodoList.pop());
               myFrame.paint(myGraph);
           }
           else if (nowSelect.equals("Thicker")){
               if (nowShape != null)
                   nowShape.setSize(nowShape.getSize() + 0.5f);
               myFrame.paint(myGraph);
           }
           else if (nowSelect.equals("Thinner")){
               if (nowShape != null)
                   nowShape.setSize(nowShape.getSize() - 0.5f);
               myFrame.paint(myGraph);
           }
        }
    }
    public void paint(){
        for (Shape cur: DoList)
            cur.draw(myGraph);
    }
}
