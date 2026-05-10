package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Perfil {
    private JPanel panel_perfil;
    private JPanel panel_cabecera;
    private JPanel panel_central;
    private JPanel panel_info;
    private JLabel label_nombre;
    private JLabel label_puntuacion;
    private JLabel label_horas;
    private JButton boton_salir;
    private JButton boton_cerrar;
    private JPanel panel_botones;
    private int idUsuario;

    public Perfil(JPanel panel_cabecera, int idUsuario) {
        String selectQuery = "select * from partida join usuario on partida.id_usuario = usuario.id where partida.id_usuario = ? ";

        this.panel_cabecera = panel_cabecera;
        this.idUsuario = idUsuario;

        panel_perfil.add(panel_cabecera);
        panel_perfil.add(panel_central);

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tetris", "admin", "admin");
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setInt(1, idUsuario);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                label_nombre.setText(rs.getString("usuario.nombre"));
                label_puntuacion.setText(rs.getString("partida.puntos") + " puntos");
                label_horas.setText(rs.getString("partida.horas"));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.out.println("La conexión con la base de datos ha fallado");
        }


        boton_salir.setPreferredSize(new Dimension(48, 48));
        boton_cerrar.setPreferredSize(new Dimension(48, 48));

        boton_salir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_perfil.getTopLevelAncestor();
                frame.setContentPane(new Menu(panel_cabecera, idUsuario).getPanel_menu());
                frame.revalidate();
                frame.repaint();
            }
        });

        boton_cerrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        null,
                        "¿Quieres cerrar sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    JFrame frame = (JFrame) panel_perfil.getTopLevelAncestor();
                    frame.setContentPane(new Inicio().getPanel_inicio());
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
    }

    public JPanel getPanel_perfil() {
        return panel_perfil;
    }
}
