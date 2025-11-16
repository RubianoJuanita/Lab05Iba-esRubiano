
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.*;

public class SlowTetrisGUI extends JFrame {
    /* ... */
    private SlowTetrisGUI() {
        prepareElementos();
        prepareActions();
    }

    public static void main(String[] args) {
        SlowTetrisGUI gui = new SlowTetrisGUI();
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
    }

    private void prepareElementos() {
        setTitle("Slow Tetris");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(s.width / 2, s.height / 2);
    }

    private void prepareActions() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Esta sguro de que desea salir del juego?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}