package swing;

import javax.swing.*;
import java.awt.*;

public class Inicio {
    private JPanel panel_inicio;
    private JPanel panel_registro;
    private JPanel panel_cabecera;
    private JLabel label_titulo;

    public Inicio() {
        panel_inicio.setPreferredSize(new Dimension(1000, 600));
        panel_inicio.setSize(new Dimension(1000, 600));
        panel_cabecera.setSize(new Dimension(panel_inicio.getWidth(), panel_inicio.getHeight() / 10));
        panel_registro.setSize(new Dimension(panel_inicio.getWidth() / 4, panel_inicio.getHeight() - 100));

        panel_inicio.add(panel_cabecera);
        panel_inicio.add(panel_registro);

        label_titulo.setFont(new Font("Bungee", Font.BOLD, 40));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inicio");
        frame.setContentPane(new Inicio().panel_inicio);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(500, 250);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/imagenes/icono.jpg");
        frame.setIconImage(icono);
    }
}
