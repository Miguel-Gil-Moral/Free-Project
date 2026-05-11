package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Menu {
    private JPanel panel_menu;
    private JPanel panel_cabecera;
    private JPanel panel_central;
    private JPanel panel_opciones;
    private JButton boton_jugar;
    private JButton boton_clasificacion;
    private JButton boton_salir;
    private JLabel label_titulo;
    private JLabel label_imagen;
    private JLabel label_usuario = new JLabel();
    private final int idUsuario;

    public Menu(int idUsuario) {
        this.idUsuario = idUsuario;

        panel_menu.setPreferredSize(new Dimension(720, 1280));
        panel_menu.setSize(new Dimension(720, 1280));

        panel_cabecera.setSize(panel_menu.getWidth(), panel_menu.getHeight() / 15);
        label_usuario.setSize(48, 48);
        ImageIcon imagenUsuario = new ImageIcon("src/imagenes/usuario.png");
        Icon usuario = new ImageIcon(
                imagenUsuario.getImage().getScaledInstance(label_usuario.getWidth() * 2, label_usuario.getHeight(), Image.SCALE_SMOOTH)
        );
        label_usuario.setIcon(usuario);
        panel_cabecera.add(label_usuario, BorderLayout.WEST);

        panel_menu.add(panel_cabecera);
        panel_menu.add(panel_central);

        label_usuario.addMouseListener(new AbrirPerfilListener());

        boton_jugar.setPreferredSize(new Dimension(48, 48));
        boton_clasificacion.setPreferredSize(new Dimension(48, 48));
        boton_salir.setPreferredSize(new Dimension(48, 48));

        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Open_Sans/static/OpenSans-Regular.ttf"));
            Font archivoBlack = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Archivo_Black/ArchivoBlack-Regular.ttf"));

            openSans.deriveFont(20f);
            archivoBlack.deriveFont(40f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(openSans);
            ge.registerFont(archivoBlack);

            System.out.println("Se ha cargado la fuente de Open Sans y Archivo Black a la pantalla Menu");

            label_titulo.setFont(new Font("Archivo Black", Font.PLAIN, 40));
            boton_jugar.setFont(new Font("Open Sans", Font.PLAIN, 20));
            boton_clasificacion.setFont(new Font("Open Sans", Font.PLAIN, 20));
            boton_salir.setFont(new Font("Open Sans", Font.PLAIN, 20));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        eventos();
    }

    private void eventos() {
        boton_jugar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
                frame.setContentPane(new Juego(idUsuario).getPanel_juego());
                frame.revalidate();
                frame.repaint();
            }
        });

        boton_clasificacion.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
                frame.setContentPane(new Clasificacion(Menu.this.idUsuario).getPanel_clasificacion());
                frame.revalidate();
                frame.repaint();
            }
        });

        boton_salir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        null,
                        "¿Deseas salir de la aplicación?",
                        "¿Salir?",
                        JOptionPane.YES_NO_OPTION
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    public class AbrirPerfilListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
            frame.setContentPane(new Perfil(idUsuario).getPanel_perfil());
            frame.revalidate();
            frame.repaint();
        }
    }

    public JPanel getPanel_menu() {
        return panel_menu;
    }
}
