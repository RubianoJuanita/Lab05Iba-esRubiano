import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.WindowAdapter;
import javax.swing.*;

public class SlowTetrisGUI extends JFrame {

    // Atributos de controles del juego
    private int hola = 10;
    private JButton botonDisminuirFilas;
    private JButton botonDisminuirColumnas;
    private JButton botonRotar;
    private JButton botonIzquierda;
    private JButton botonDerecha;
    private JButton botonCaer;
    private JButton botonCambiarColor;
    private JButton botonConfigurarTablero;
    private JButton botonAumentarFilas;
    private JButton botonAumentarColumnas;
    private JLabel labelScore;
    private JComboBox<String> comboColores;

    private JButton botonIniciarJuego;
    private boolean juegoIniciado;// controla si el juego está en curso

    private int filas;
    private int columnas;
    private boolean tableroVisible;
    // Atributos de los botones del menú principal
    private JButton botonNuevo;
    private JButton botonAbrir;
    private JButton botonSalir;

    // Atributos para cambio de paneles
    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    // Atributos de la pantalla de juego
    private JPanel panelTablero;
    private JPanel panelControl;

    // Atributos del tablero
    private int filasInferiores;// para que la parte inferior del panel de juego sea dinamica
    private int filasDivision;// fila donde está el muro divisorio del panel de juego.
    private static final int FILAS = 20;
    private static final int COLUMNAS = 10;
    private Color[][] tablero;
    private static final int FILAS_SUPERIORES = 4;

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
        prepareElementosControl();

        panelJuego.add(panelTablero, BorderLayout.CENTER);
        panelJuego.add(panelControl, BorderLayout.EAST);

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

        // Inicializar sin tablero visible
        tableroVisible = false;
        filas = 0;
        columnas = 0;
        filasDivision = 0;
        tablero = null;
    }

    private void prepareElementosControl() {
        panelControl = new JPanel();
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.Y_AXIS));
        panelControl.setBackground(Color.GRAY);
        panelControl.setPreferredSize(new Dimension(180, 0));
        panelControl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón Configurar Tablero
        botonConfigurarTablero = new JButton("Configurar Tablero");
        botonConfigurarTablero.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonConfigurarTablero.setMaximumSize(new Dimension(160, 30));
        panelControl.add(botonConfigurarTablero);

        panelControl.add(Box.createVerticalStrut(10));

        // Botón Configurar Tablero
        botonConfigurarTablero = new JButton("Configurar Tablero");
        botonConfigurarTablero.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonConfigurarTablero.setMaximumSize(new Dimension(160, 30));
        panelControl.add(botonConfigurarTablero);

        panelControl.add(Box.createVerticalStrut(10));

        // Botón Iniciar Juego
        botonIniciarJuego = new JButton("Iniciar Juego");
        botonIniciarJuego.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonIniciarJuego.setMaximumSize(new Dimension(160, 30));
        botonIniciarJuego.setBackground(Color.GREEN);
        panelControl.add(botonIniciarJuego);

        panelControl.add(Box.createVerticalStrut(10));

        // Botón Rotar Pieza
        botonRotar = new JButton("Rotar Pieza");
        botonRotar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonRotar.setMaximumSize(new Dimension(160, 30));
        panelControl.add(botonRotar);

        panelControl.add(Box.createVerticalStrut(10));

        // Cambiar color
        JLabel labelCambiarColor = new JLabel("Cambiar color");
        labelCambiarColor.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(labelCambiarColor);

        panelControl.add(Box.createVerticalStrut(5));

        String[] colores = { "RED", "BLUE", "GREEN", "YELLOW", "CYAN", "MAGENTA", "ORANGE" };
        comboColores = new JComboBox<>(colores);
        comboColores.setMaximumSize(new Dimension(160, 25));
        comboColores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(comboColores);

        panelControl.add(Box.createVerticalStrut(10));

        botonCambiarColor = new JButton("Cambiar color");
        botonCambiarColor.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCambiarColor.setMaximumSize(new Dimension(160, 30));
        panelControl.add(botonCambiarColor);

        panelControl.add(Box.createVerticalStrut(20));

        // Score
        JPanel panelScore = new JPanel();
        panelScore.setLayout(new BorderLayout());
        panelScore.setBackground(Color.BLACK);
        panelScore.setMaximumSize(new Dimension(160, 60));
        panelScore.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel tituloScore = new JLabel("SCORE", SwingConstants.CENTER);
        tituloScore.setForeground(Color.WHITE);
        tituloScore.setFont(new Font("Arial", Font.BOLD, 14));
        panelScore.add(tituloScore, BorderLayout.NORTH);

        labelScore = new JLabel("0", SwingConstants.CENTER);
        labelScore.setForeground(Color.GREEN);
        labelScore.setFont(new Font("Arial", Font.BOLD, 24));
        panelScore.add(labelScore, BorderLayout.CENTER);

        panelControl.add(panelScore);

        panelControl.add(Box.createVerticalStrut(20));

        // Botones de movimiento
        JPanel panelMovimiento = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelMovimiento.setBackground(Color.GRAY);
        panelMovimiento.setMaximumSize(new Dimension(160, 40));

        botonIzquierda = new JButton("←");
        botonDerecha = new JButton("→");

        panelMovimiento.add(botonIzquierda);
        panelMovimiento.add(botonDerecha);
        panelControl.add(panelMovimiento);

        panelControl.add(Box.createVerticalStrut(10));

        // Botón Caer
        botonCaer = new JButton("Caer");
        botonCaer.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCaer.setMaximumSize(new Dimension(160, 30));
        panelControl.add(botonCaer);

        panelControl.add(Box.createVerticalStrut(15));
        // Controles de altura (h)
        JPanel panelAltura = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelAltura.setBackground(Color.GRAY);
        panelAltura.setMaximumSize(new Dimension(160, 35));

        JLabel labelH = new JLabel("h:");
        botonDisminuirFilas = new JButton("-");
        botonAumentarFilas = new JButton("+");

        botonDisminuirFilas.setPreferredSize(new Dimension(45, 25));
        botonAumentarFilas.setPreferredSize(new Dimension(45, 25));

        panelAltura.add(labelH);
        panelAltura.add(botonDisminuirFilas);
        panelAltura.add(botonAumentarFilas);
        panelControl.add(panelAltura);

        panelControl.add(Box.createVerticalStrut(5));

        // Controles de ancho (w)
        JPanel panelAncho = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelAncho.setBackground(Color.GRAY);
        panelAncho.setMaximumSize(new Dimension(160, 35));

        JLabel labelW = new JLabel("w:");
        botonDisminuirColumnas = new JButton("-");
        botonAumentarColumnas = new JButton("+");

        botonDisminuirColumnas.setPreferredSize(new Dimension(45, 25));
        botonAumentarColumnas.setPreferredSize(new Dimension(45, 25));

        panelAncho.add(labelW);
        panelAncho.add(botonDisminuirColumnas);
        panelAncho.add(botonAumentarColumnas);
        panelControl.add(panelAncho);

        panelControl.add(Box.createVerticalGlue());
    }

    private void dibujarTablero(Graphics g) {
        if (!tableroVisible || tablero == null) {
            return;
        }

        int anchoPanel = panelTablero.getWidth();
        int altoPanel = panelTablero.getHeight();

        int anchoCelda = anchoPanel / columnas;
        int altoCelda = altoPanel / filas;
        // dibujamos las celdas
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
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
        // Dibujamos muro divisorio
        int yMuro = filasDivision * altoCelda;
        int alturaMuro = altoCelda / 2; // Muro de medio bloque de altura

        g.setColor(new Color(139, 119, 101)); // Color marrón/café como en el taller
        g.fillRect(0, yMuro, anchoPanel, alturaMuro);

        // Borde del muro
        g.setColor(Color.BLACK);
        g.drawRect(0, yMuro, anchoPanel, alturaMuro);
    }

    private void configurarTablero() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField campoFilas = new JTextField("20");
        JTextField campoColumnas = new JTextField("10");
        JTextField campoDivision = new JTextField("4");

        panel.add(new JLabel("Filas totales (mínimo 4):"));
        panel.add(campoFilas);
        panel.add(new JLabel("Columnas (mínimo 4):"));
        panel.add(campoColumnas);
        panel.add(new JLabel("Filas zona superior (mín 2):"));
        panel.add(campoDivision);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Configurar Tamaño del Tablero",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int nuevasFilas = Integer.parseInt(campoFilas.getText());
                int nuevasColumnas = Integer.parseInt(campoColumnas.getText());
                int nuevaDivision = Integer.parseInt(campoDivision.getText());

                // Validación de dimensiones mínimas
                if (nuevasFilas < 4 || nuevasColumnas < 4) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El tamaño mínimo es 4x4",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validación de la división del panel del juego
                // Mínimo 2 filas arriba, mínimo 2 filas abajo
                if (nuevaDivision < 2) {
                    JOptionPane.showMessageDialog(
                            this,
                            "La zona superior debe tener mínimo 2 filas",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nuevaDivision >= nuevasFilas - 1) {
                    JOptionPane.showMessageDialog(
                            this,
                            "La zona inferior debe tener mínimo 2 filas.\nCon " + nuevasFilas
                                    + " filas totales, la división máxima es " + (nuevasFilas - 2),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                filas = nuevasFilas;
                columnas = nuevasColumnas;
                filasDivision = nuevaDivision;
                tablero = new Color[filas][columnas];
                tableroVisible = true;
                refresh();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese números válidos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
        botonConfigurarTablero.addActionListener(e -> configurarTablero());
        botonIniciarJuego.addActionListener(e -> iniciarJuego());
        botonAumentarFilas.addActionListener(e -> aumentarFilas());
        botonAumentarColumnas.addActionListener(e -> aumentarColumnas());
        botonDisminuirFilas.addActionListener(e -> disminuirFilas());
        botonDisminuirColumnas.addActionListener(e -> disminuirColumnas());
    }

    private void iniciarJuego() {
        if (!tableroVisible) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe configurar el tablero primero",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        juegoIniciado = true;
        botonIniciarJuego.setEnabled(false);
        botonIniciarJuego.setText("Juego en Curso");
        botonConfigurarTablero.setEnabled(false);

        JOptionPane.showMessageDialog(
                this,
                "¡Juego iniciado! Las dimensiones del tablero ya no pueden ser modificadas.",
                "Juego Iniciado",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void nuevoJuego() {

        filas = 20;
        columnas = 10;
        filasDivision = 4; // El muro está después de la fila 4 (zona superior = 4 filas)
        tablero = new Color[filas][columnas];
        tableroVisible = true;
        juegoIniciado = false;// para cuando el juego esta sin iniciar
        refresh();
        cardLayout.show(panelPrincipal, "juego");
    }

    private void aumentarFilas() {
        if (!tableroVisible) {
            return;
        }
        if (juegoIniciado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Las dimensiones del tablero no pueden ser alteradas en este punto del juego",
                    "Acción no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        filas++;
        Color[][] nuevoTablero = new Color[filas][columnas];

        // Copiar contenido anterior
        for (int i = 0; i < filas - 1; i++) {
            for (int j = 0; j < columnas; j++) {
                nuevoTablero[i][j] = tablero[i][j];
            }
        }

        tablero = nuevoTablero;
        refresh();
    }

    private void disminuirFilas() {
        if (!tableroVisible || filas <= 4) {
            return;
        }

        if (juegoIniciado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Las dimensiones del tablero no pueden ser alteradas en este punto del juego",
                    "Acción no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar que queden mínimo 4 filas Y que la división del panel de juego siga
        // siendo válida
        if (filas <= 4 || filas - 1 <= filasDivision + 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se puede reducir más. Mínimo 4 filas y debe haber espacio para la zona inferior.",
                    "Límite alcanzado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        filas--;
        Color[][] nuevoTablero = new Color[filas][columnas];

        // se pierde la última fila
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                nuevoTablero[i][j] = tablero[i][j];
            }
        }

        tablero = nuevoTablero;
        refresh();
    }

    private void disminuirColumnas() {
        if (!tableroVisible || columnas <= 4) {
            return;
        }

        if (juegoIniciado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Las dimensiones del tablero no pueden ser alteradas en este punto del juego",
                    "Acción no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        columnas--;
        Color[][] nuevoTablero = new Color[filas][columnas];

        // Copiar contenido (se pierde la última columna)
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                nuevoTablero[i][j] = tablero[i][j];
            }
        }

        tablero = nuevoTablero;
        refresh();
    }

    private void aumentarColumnas() {
        if (!tableroVisible) {
            return;
        }
        if (juegoIniciado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Las dimensiones del tablero no pueden ser alteradas en este punto del juego",
                    "Acción no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        columnas++;
        Color[][] nuevoTablero = new Color[filas][columnas];

        // Copiar contenido anterior
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas - 1; j++) {
                nuevoTablero[i][j] = tablero[i][j];
            }
        }

        tablero = nuevoTablero;
        refresh();
    }

    // private void volverAlMenu() {
    // cardLayout.show(panelPrincipal, "inicio");
    // }

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