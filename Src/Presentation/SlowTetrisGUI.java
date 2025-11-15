import java.awt.*;
import javax.swing.*;

public class SlowTetrisGUI extends JFrame {
/*... */
private SlowTetrisGUI(){;
    prepareElementos();
}
public static void main(String[] args ){
    SlowTetrisGUI gui = new SlowTetrisGUI();
    gui.setVisible(true);
    gui.setLocationRelativeTo(null);
}
private void prepareElementos(){
    setTitle("Slow Tetris");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension s =Toolkit.getDefaultToolkit().getScreenSize();
    setSize(s.width/2, s.height/2);
}
}