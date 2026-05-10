package swing;

import javax.swing.*;
import java.util.Random;

public class Juego {
    private JPanel panel_juego;
    private JPanel panel_cabecera;
    private JLabel label_titulo;
    private JPanel panel_central;
    private JLabel matriz;

    public Juego() {
        panel_juego.add(panel_cabecera);
        panel_juego.add(panel_central);

        int[][] matriz = new int[20][10];
    }

    public void sacarPieza() {
        String[] rutaPiezas = {"src/imagenes/Piezas Tetris/Pieza I.png", "src/imagenes/Pieza Tetris/ Pieza J.png",
                "src/imagenes/Piezas Tetris/ Pieza L.png", "src/imagenes/Piezas Tetris/ Pieza O.png", "src/imagenes/Piezas Tetris/ Pieza S.png",
                "src/imagenes/Piezas Tetris/ Pieza T.png", "src/imagenes/Piezas Tetris/ Pieza Z.png"};

        Random rand = new Random();
        int posicionArray = rand.nextInt(rutaPiezas.length);

    }

    public JPanel getPanel_juego() {
        return panel_juego;
    }
}
