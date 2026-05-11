package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
    private JLabel label_titulo;
    private final int idUsuario;

    public Perfil(int idUsuario) {
        String selectQuery = "select * from partida join usuario on partida.id_usuario = usuario.id where partida.id_usuario = ? ";
        this.idUsuario = idUsuario;

        panel_perfil.setPreferredSize(new Dimension(720, 1280));
        panel_perfil.setSize(new Dimension(720, 1280));

        panel_cabecera.setSize(panel_perfil.getWidth(), panel_perfil.getHeight() / 15);
        panel_perfil.add(panel_cabecera);

        panel_central.setSize(panel_perfil.getWidth(), panel_perfil.getHeight() - 100);
        panel_perfil.add(panel_central);

        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Open_Sans/static/OpenSans-Regular.ttf"));
            Font archivoBlack = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Archivo_Black/ArchivoBlack-Regular.ttf"));

            openSans.deriveFont(20f);
            archivoBlack.deriveFont(40f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(openSans);
            ge.registerFont(archivoBlack);

            System.out.println("Se ha cargado la fuente de Open Sans y Archivo Black a la pantalla Perfil");

            label_titulo.setFont(new Font("Archivo Black", Font.PLAIN, 40));
            label_nombre.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_puntuacion.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_horas.setFont(new Font("Open Sans", Font.PLAIN, 20));
            boton_salir.setFont(new Font("Open Sans", Font.PLAIN, 20));
            boton_cerrar.setFont(new Font("Open Sans", Font.PLAIN, 20));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

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

        eventos();
    }

    private void eventos() {
        boton_salir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_perfil.getTopLevelAncestor();
                frame.setContentPane(new Menu(idUsuario).getPanel_menu());
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
