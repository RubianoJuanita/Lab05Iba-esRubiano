import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.WindowAdapter;
import javax.swing.*;

public class SlowTetrisGUI extends JFrame {
    // Atributos de los botones del menú principal
    private JButton botonNuevo;
    private JButton botonAbrir;
    private JButton botonSalir;

    // Atributos para cambio de paneles
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    // Atributos de la pantalla de juego
    private JPanel panelTablero;
    private JButton botonVolver;

    // Atributos del tablero
    private static final int FILAS = 20;
    private static final int COLUMNAS = 10;
    private Color[][] tablero;

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

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        prepareElementosInicio();
        prepareElementosJuego();

        add(panelPrincipal);
    }

    private void prepareElementosInicio() {
        JPanel panelInicio = new JPanel();
        panelInicio.setLayout(new BorderLayout());
        panelInicio.setBackground(Color.BLACK);

        // Título del juego
        JLabel titulo = new JLabel("SLOW TETRIS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(Color.CYAN);
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        panelInicio.add(titulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(Color.BLACK);

        botonNuevo = new JButton("Nuevo Juego");
        botonAbrir = new JButton("Abrir Juego");
        botonSalir = new JButton("Salir");

        // Estilizar botones
        Font fuenteBotones = new Font("Arial", Font.BOLD, 18);
        Dimension tamanioBotones = new Dimension(200, 50);

        botonNuevo.setFont(fuenteBotones);
        botonNuevo.setPreferredSize(tamanioBotones);

        botonAbrir.setFont(fuenteBotones);
        botonAbrir.setPreferredSize(tamanioBotones);

        botonSalir.setFont(fuenteBotones);
        botonSalir.setPreferredSize(tamanioBotones);

        panelBotones.add(botonNuevo);
        panelBotones.add(botonAbrir);
        panelBotones.add(botonSalir);

        panelInicio.add(panelBotones, BorderLayout.CENTER);

        panelPrincipal.add(panelInicio, "inicio");
    }

    private void prepareElementosJuego() {
        JPanel panelJuego = new JPanel();
        panelJuego.setLayout(new BorderLayout());
        panelJuego.setBackground(Color.DARK_GRAY);

        prepareElementosTablero();
        panelJuego.add(panelTablero, BorderLayout.CENTER);

        botonVolver = new JButton("Volver al Menú");
        panelJuego.add(botonVolver, BorderLayout.SOUTH);

        panelPrincipal.add(panelJuego, "juego");
    }

    private void prepareElementosTablero() {
        panelTablero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarTablero(g);
            }
        };
        panelTablero.setBackground(Color.BLACK);
        panelTablero.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        tablero = new Color[FILAS][COLUMNAS];
    }

    private void dibujarTablero(Graphics g) {
        int anchoPanel = panelTablero.getWidth();
        int altoPanel = panelTablero.getHeight();

        int anchoCelda = anchoPanel / COLUMNAS;
        int altoCelda = altoPanel / FILAS;

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                int x = col * anchoCelda;
                int y = fila * altoCelda;

                if (tablero[fila][col] != null) {
                    g.setColor(tablero[fila][col]);
                    g.fillRect(x, y, anchoCelda, altoCelda);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, anchoCelda, altoCelda);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x, y, anchoCelda, altoCelda);
                }
            }
        }
    }

    private void refresh() {
        panelTablero.repaint();
    }

    private void prepareActions() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        botonSalir.addActionListener(e -> exit());
        botonAbrir.addActionListener(e -> abrirJuego());
        botonNuevo.addActionListener(e -> nuevoJuego());
        botonVolver.addActionListener(e -> volverAlMenu());

    private void nuevoJuego() {
        cardLayout.show(panelPrincipal, "juego");
    }

    private void volverAlMenu() {
        cardLayout.show(panelPrincipal, "inicio");
    }

    private void abrirJuego() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                    "Archivo seleccionado para abrir: " + archivoSeleccionado.getAbsolutePath(),
                    "Cargar Juego", JOptionPane.INFORMATION_MESSAGE);
        }
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