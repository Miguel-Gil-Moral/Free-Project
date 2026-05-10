package swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Clasificacion {
    private JPanel panel_clasificacion;
    private JPanel panel_central;
    private JPanel panel_info;
    private JButton boton_salir;
    private JPanel panel_tabla;
    private JPanel panel_boton;
    private JTable tabla_info;
    private JPanel panel_cabecera;
    private JLabel label_titulo;
    private JLabel label_imagen;
    private int idUsuario;

    public Clasificacion(int idUsuario) {
        panel_clasificacion.add(panel_cabecera);
        panel_clasificacion.add(panel_central);
        panel_info.setLayout(new GridLayout(1, 2));
        panel_info.add(panel_boton);
        panel_boton.add(boton_salir);
        panel_info.add(panel_tabla);

        panel_clasificacion.setSize(new Dimension(720, 1280));
        panel_central.setSize(new Dimension(panel_clasificacion.getWidth(), panel_clasificacion.getHeight() - 100));
        panel_info.setSize(new Dimension(panel_central.getWidth(), panel_central.getHeight()));

        String[] encabezado = {"Nombre Usuario", "Puntuación", "Nivel"};

        DefaultTableModel modelo = new DefaultTableModel(encabezado, 0);

        tabla_info = new JTable(modelo);

        JScrollPane scrollPane = new JScrollPane(tabla_info);

        panel_boton.setPreferredSize(new Dimension(800, 300));
        panel_tabla.add(scrollPane);

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

            System.out.println("Se ha cargado la fuente de Open Sans y Bungee a la aplicación");
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        label_titulo.setFont(new Font("Archivo Black", Font.BOLD, 40));
        tabla_info.getTableHeader().setFont(new Font("Bungee", Font.BOLD, 30));
        tabla_info.getTableHeader().setBackground(new Color(10, 25, 47));
        tabla_info.getTableHeader().setForeground(new Color(255, 255, 255));

        boton_salir.setPreferredSize(new Dimension(96, 48));
        boton_salir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_clasificacion.getTopLevelAncestor();
                frame.setContentPane(new Menu(panel_cabecera, idUsuario).getPanel_menu());
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public JPanel getPanel_clasificacion() {
        return panel_clasificacion;
    }
}
