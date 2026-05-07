package swing;

import javax.swing.*;
import java.awt.*;
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
    private JLabel label_usuario = new JLabel();

    public Menu(JPanel panel_cabecera) {
        this.panel_cabecera = panel_cabecera;
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
    }

    public class AbrirPerfilListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JFrame frame = (JFrame) panel_menu.getTopLevelAncestor();
            frame.setContentPane(new Perfil(panel_cabecera).getPanel_perfil());
            frame.revalidate();
            frame.repaint();
        }
    }

    public JPanel getPanel_menu() {
        return panel_menu;
    }
}
