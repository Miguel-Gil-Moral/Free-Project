package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Juego {
    private JPanel panel_juego;
    private JPanel panel_cabecera;
    private JLabel label_titulo;
    private JPanel panel_central;
    private JLabel label_matriz;
    private JPanel panel_matriz;
    private JPanel panel_ventanas_derecha;
    private JPanel panel_pieza_guardada;
    private JLabel label_pieza_guardada;
    private JPanel panel_interior_pieza_guardada;
    private JPanel panel_ventanas_pequenyas;
    private JPanel panel_puntos;
    private JPanel panel_nivel;
    private JPanel panel_linea;
    private JLabel label_puntos;
    private JLabel label_nivel;
    private JLabel label_linea;
    private JPanel panel_interior_puntos;
    private JPanel panel_interior_linea;
    private JPanel panel_interior_nivel;
    private JLabel label_imagen_pieza_guardada;
    private JLabel label_puntos_ventana;
    private JLabel label_linea_ventana;
    private JLabel label_nivel_ventana;
    private JPanel panel_ventanas_izquierda;
    private JPanel panel_pausa;
    private JLabel label_pausa;
    private JPanel panel_ventana_siguiente;
    private JLabel label_siguiente;
    private JPanel panel_interior_ventana_siguiente;
    private JLabel label_siguiente_pieza_1;
    private JLabel label_siguiente_pieza_2;
    private JLabel label_siguiente_pieza_3;
    private int idUsuario;

    public Juego(int idUsuario) {
        this.idUsuario = idUsuario;
        panel_juego.setPreferredSize(new Dimension(720, 1280));
        panel_juego.setSize(new Dimension(720, 1280));

        panel_cabecera.setSize(panel_juego.getWidth(), panel_juego.getHeight() / 15);
        panel_juego.add(panel_cabecera);

        panel_central.setSize(panel_juego.getWidth(), panel_juego.getHeight() - 100);
        panel_juego.add(panel_central);

        try {
            Font archivoBlack = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Archivo_Black/ArchivoBlack-Regular.ttf"));
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Open_Sans/static/OpenSans-Regular.ttf"));
            Font bungee = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Bungee/Bungee-Regular.ttf"));

            openSans.deriveFont(20f);
            bungee.deriveFont(30f);
            archivoBlack.deriveFont(40f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(openSans);
            ge.registerFont(bungee);
            ge.registerFont(archivoBlack);

            System.out.println("Se ha cargado la fuente de Open Sans, Archivo Black, y Bungee a la pantalla Juego");
            label_titulo.setFont(new Font("Archivo Black", Font.PLAIN, 40));
            label_pieza_guardada.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_linea.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_nivel.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_puntos.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_linea_ventana.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_nivel_ventana.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_puntos_ventana.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_siguiente.setFont(new Font("Bungee", Font.PLAIN, 20));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        label_pausa.setSize(48, 48);
        ImageIcon imagenPausa = new ImageIcon("src/imagenes/pausa.png");
        Icon iconoPausa = new ImageIcon(
                imagenPausa.getImage().getScaledInstance(label_pausa.getWidth(), label_pausa.getHeight(), Image.SCALE_SMOOTH)
        );
        label_pausa.setIcon(iconoPausa);
        label_pausa.addMouseListener(new PausarJuegoListener());

        int[][] matriz = new int[20][10];

        int[][] pieza = sacarPieza();
    }

    public int[][] sacarPieza() {
        Random rand = new Random();

        int numRandom = rand.nextInt(7);
        int[][] pieza = new int[4][4];
        switch (numRandom) {
            case 0:
                pieza = new int[][]{
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0}
                };
                break;
            case 1:
                pieza = new int[][]{
                        {0, 0, 1, 0},
                        {0, 0, 1, 0},
                        {0, 1, 1, 0},
                        {0, 0, 0, 0}
                };
                break;
            case 2:
                pieza = new int[][]{
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 0, 0, 0}
                };
                break;
            case 3:
                pieza = new int[][]{
                        {0, 1, 1, 0},
                        {0, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };
                break;
            case 4:
                pieza = new int[][]{
                        {0, 1, 1, 0},
                        {1, 1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };
                break;
            case 5:
                pieza = new int[][]{
                        {0, 1, 0, 0},
                        {1, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };
                break;
            case 6:
                pieza = new int[][]{
                        {0, 1, 1, 0},
                        {0, 0, 1, 1},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };
        }

        if (label_siguiente_pieza_1.getText().isEmpty()) {
            label_siguiente_pieza_1.setText(String.valueOf(numRandom));
        } else if (label_siguiente_pieza_2.getText().isEmpty()) {
            label_siguiente_pieza_2.setText(String.valueOf(numRandom));
        } else {
            label_siguiente_pieza_3.setText(String.valueOf(numRandom));
        }

        return pieza;
    }

    public class PausarJuegoListener extends MouseAdapter {
        private final String[] OPCIONES = {"Salir", "Seguir"};

        @Override
        public void mousePressed(MouseEvent e) {
            int respuesta = JOptionPane.showOptionDialog(
                    null,
                    "¿Quiere salir del juego?",
                    "Pausa",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    OPCIONES,
                    OPCIONES[1]
            );

            if (respuesta == 0) {
                JFrame frame = (JFrame) panel_juego.getTopLevelAncestor();
                frame.setContentPane(new Menu(idUsuario).getPanel_menu());
                frame.revalidate();
                frame.repaint();
            }
        }
    }

    public JPanel getPanel_juego() {
        return panel_juego;
    }
}
