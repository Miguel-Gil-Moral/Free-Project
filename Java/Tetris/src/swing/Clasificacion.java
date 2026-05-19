package swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Clasificacion {
    private JPanel panel_clasificacion;
    private JPanel panel_central;
    private JPanel panel_info;
    private JButton boton_salir;
    private JPanel panel_cabecera;
    private JLabel label_titulo;
    private JLabel label_usuario;
    private int idUsuario;

    public Clasificacion(int idUsuario) {
        this.idUsuario = idUsuario;
        panel_clasificacion.setPreferredSize(new Dimension(720, 1280));
        panel_clasificacion.setSize(new Dimension(720, 1280));

        panel_cabecera.setSize(panel_clasificacion.getWidth(), panel_clasificacion.getHeight() / 15);
        panel_clasificacion.add(panel_cabecera);
        label_usuario.setSize(48, 48);
        ImageIcon imagenUsuario = new ImageIcon("src/imagenes/usuario.png");
        Icon usuario = new ImageIcon(
                imagenUsuario.getImage().getScaledInstance(label_usuario.getWidth() * 2, label_usuario.getHeight(), Image.SCALE_SMOOTH)
        );
        label_usuario.setIcon(usuario);
        panel_cabecera.add(label_usuario, BorderLayout.WEST);

        panel_central.setSize(panel_clasificacion.getWidth(), panel_clasificacion.getHeight() - 100);
        panel_clasificacion.add(panel_central);

        String[] encabezado = {"Nombre", "Puntos", "Nivel"};

        DefaultTableModel modelo = new DefaultTableModel(encabezado, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla_info = new JTable(modelo);

        JScrollPane scrollPane = new JScrollPane(tabla_info);

        panel_info.add(scrollPane);

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

            System.out.println("Se ha cargado la fuente de Open Sans, Archivo Black, y Bungee a la pantalla Clasificación");
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        label_titulo.setFont(new Font("Archivo Black", Font.BOLD, 40));
        tabla_info.getTableHeader().setFont(new Font("Bungee", Font.BOLD, 30));
        tabla_info.getTableHeader().setBackground(new Color(10, 25, 47));
        tabla_info.getTableHeader().setForeground(new Color(255, 255, 255));

        boton_salir.setPreferredSize(new Dimension(96, 48));

        String selectQuery = "select usuario.nombre, p.puntos, p.nivel from partida p join usuario on p.id_usuario = usuario.id where p.puntos = (select max(p2.puntos) from partida p2 where p2.id_usuario = p.id_usuario);";
        Object[] nuevaFila = new Object[3];
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tetris", "admin", "admin");
            PreparedStatement pst = con.prepareStatement(selectQuery);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                nuevaFila[0] = rs.getString("nombre");
                nuevaFila[1] = rs.getInt("puntos");
                nuevaFila[2] = rs.getInt("nivel");
                modelo.addRow(nuevaFila);
            }
        } catch (Exception e) {
            System.out.println("La conexión con la base de datos ha fallado");
        }

        eventos();
    }

    private void eventos() {
        label_usuario.addMouseListener(new AbrirPerfilListener());
        boton_salir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) panel_clasificacion.getTopLevelAncestor();
                frame.setContentPane(new Menu(idUsuario).getPanel_menu());
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public class AbrirPerfilListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JFrame frame = (JFrame) panel_clasificacion.getTopLevelAncestor();
            frame.setContentPane(new Perfil(idUsuario).getPanel_perfil());
            frame.revalidate();
            frame.repaint();
        }
    }

    public JPanel getPanel_clasificacion() {
        return panel_clasificacion;
    }
}
