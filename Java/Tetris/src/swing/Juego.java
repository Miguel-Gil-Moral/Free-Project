package swing;

import javax.swing.*;

public class Juego {
    private JPanel panel_juego;
    private JPanel panel_cabecera;
    private JLabel label_titulo;

    public Juego() {
        int[][] matriz = new int[20][10];
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Juego");
        frame.setContentPane(new Juego().panel_juego);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
