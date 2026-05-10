package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private int idUsuario;

    public Menu(JPanel panel_cabecera, int nombreUsuario) {
        this.panel_cabecera = panel_cabecera;
        this.idUsuario = nombreUsuario;

        panel_menu.add(panel_cabecera);
        panel_menu.add(panel_central);

        label_usuario.setSize(48, 48);
        ImageIcon imagenUsuario = new ImageIcon("src/imagenes/usuario.png");
        Icon usuario = new ImageIcon(
            imagenUsuario.getImage().getScaledInstance(label_usuario.getWidth() * 2, label_usuario.getHeight(), Image.SCALE_SMOOTH)
        );
        label_usuario.setIcon(usuario);
        panel_cabecera.add(label_usuario, BorderLayout.WEST);

        label_usuario.addMouseListener(new AbrirPerfilListener());

        boton_jugar.setPreferredSize(new Dimension(48, 48));
        boton_clasificacion.setPreferredSize(new Dimension(48, 48));
        boton_salir.setPreferredSize(new Dimension(48, 48));

        boton_jugar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
                frame.setContentPane(new Juego().getPanel_juego());
                frame.revalidate();
                frame.repaint();
            }
        });

        boton_clasificacion.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
                frame.setContentPane(new Clasificacion(idUsuario).getPanel_clasificacion());
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
            frame.setContentPane(new Perfil(panel_cabecera, idUsuario).getPanel_perfil());
            frame.revalidate();
            frame.repaint();
        }
    }

    public JPanel getPanel_menu() {
        return panel_menu;
    }
}
