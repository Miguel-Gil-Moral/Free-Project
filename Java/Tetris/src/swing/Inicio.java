package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Inicio {
    private JPanel panel_inicio;
    private JPanel panel_cabecera;
    private JPanel panel_central;
    private JLabel label_titulo;
    private JPanel panel_registro;
    private JTextField campo_nombre;
    private JPasswordField campo_contrasenya;
    private JButton boton_crear;
    private JLabel label_pregunta;

    public Inicio() {
        panel_inicio.setPreferredSize(new Dimension(720, 1280));
        panel_inicio.setSize(new Dimension(720, 1280));

        panel_inicio.add(panel_cabecera);
        panel_inicio.add(panel_central);

        panel_cabecera.setSize(new Dimension(panel_inicio.getWidth(), panel_inicio.getHeight() / 15));

        campo_nombre.setPreferredSize(new Dimension(48, 48));
        campo_contrasenya.setPreferredSize(new Dimension(48, 48));
        boton_crear.setPreferredSize(new Dimension(48, 48));

        panel_registro.setFocusable(true);
        eventos();

        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Open_Sans/static/OpenSans-Regular.ttf"));
            Font archivoBlack = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Archivo_Black/ArchivoBlack-Regular.ttf"));

            openSans.deriveFont(20f);
            archivoBlack.deriveFont(40f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(openSans);
            ge.registerFont(archivoBlack);

            System.out.println("Se ha cargado la fuente de Open Sans y Archivo Black a la pantalla Inicio");

            label_titulo.setFont(new Font("Archivo Black", Font.PLAIN, 40));
            campo_nombre.setFont(new Font("Open Sans", Font.PLAIN, 20));
            campo_contrasenya.setFont(new Font("Open Sans", Font.PLAIN, 20));
            boton_crear.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_pregunta.setFont(new Font("Open Sans", Font.PLAIN, 20));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        label_pregunta.addMouseListener(new CambiarSesionListener());
    }

    private void eventos() {

        //Nombre
        campo_nombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo_nombre.getText().equals("Introduce el nombre")) {
                    campo_nombre.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo_nombre.getText().isEmpty()) {
                    campo_nombre.setText("Introduce el nombre");
                }
            }
        });
        campo_nombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    campo_nombre.transferFocus();
                }
            }
        });

        // Contraseña
        campo_contrasenya.setEchoChar((char)0);
        campo_contrasenya.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo_contrasenya.getText().equals("Introduce la contraseña")) {
                    campo_contrasenya.setText("");
                    campo_contrasenya.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo_contrasenya.getText().isEmpty()) {
                    campo_contrasenya.setText("Introduce la contraseña");
                    campo_contrasenya.setEchoChar((char)0);
                }
            }
        });
        campo_contrasenya.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    campo_contrasenya.transferFocus();
                }
            }
        });

        //Botón
        boton_crear.addActionListener(new BotonCrearListener());
        boton_crear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    crearCuenta();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inicio");
        frame.setContentPane(new Inicio().panel_inicio);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/imagenes/icono.png");
        frame.setIconImage(icono);
    }

    public class BotonCrearListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            crearCuenta();
        }
    }

    private void crearCuenta() {
        String contrasenya = "", nombreUsuario = "";
        int idUsuario = 0;
        if (!campo_nombre.getText().equals("Introduce el nombre") || !campo_contrasenya.getText().equals("Introduce la contraseña")) {
            try {
                String selectQuery = "select * from usuario where nombre = ? and contrasenya = ?";
                String insertQuery = "insert into usuario (nombre, contrasenya) values (?, ?)";

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tetris","admin","admin");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, campo_nombre.getText());
                ps.setString(2, campo_contrasenya.getText());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    idUsuario = rs.getInt("id");
                    nombreUsuario = rs.getString("nombre");
                    contrasenya = rs.getString("contrasenya");
                }
                if (nombreUsuario.equals(campo_nombre.getText()) && contrasenya.equals(campo_contrasenya.getText()) && boton_crear.getText().equals("Crear Cuenta")) {
                    JOptionPane.showMessageDialog(null, "Ya hay un usuario igual con la misma contraseña");
                } else if (nombreUsuario.isEmpty() && contrasenya.isEmpty() && boton_crear.getText().equals("Iniciar Sesión")) {
                    JOptionPane.showMessageDialog(null, "El nombre o la contraseña no son correctos");
                } else if (!nombreUsuario.equals(campo_nombre.getText()) && !contrasenya.equals(campo_contrasenya.getText()) && boton_crear.getText().equals("Crear Cuenta")){
                    PreparedStatement ps2 = con.prepareStatement(insertQuery);
                    ps2.setString(1, campo_nombre.getText());
                    ps2.setString(2, campo_contrasenya.getText());
                    int filas = ps2.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(null, "Se han introducido correctamente a la base de datos");
                        ResultSet rs2 = ps.executeQuery();
                        while (rs2.next()) {
                            idUsuario = rs2.getInt("id");
                        }
                        irMenu(idUsuario);
                    }
                    ps2.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha iniciado sesión correctamente");
                    irMenu(idUsuario);
                }
                campo_nombre.setText("Introduce el nombre");
                campo_contrasenya.setText("Introduce la contraseña");
                campo_contrasenya.setEchoChar((char)0);
                rs.close();
                ps.close();
                con.close();
            } catch (Exception ex) {
                System.out.println("La conexión con la base de datos ha fallado");
            }
        }
    }

    private void irMenu(int idUsuario) {
        JFrame frame = (JFrame) panel_inicio.getTopLevelAncestor();
        frame.setContentPane(new Menu(idUsuario).getPanel_menu());
        frame.revalidate();
        frame.repaint();
    }

    public class CambiarSesionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (label_pregunta.getText().equals("¿Ya tienes cuenta?")) {
                boton_crear.setText("Iniciar Sesión");
                label_pregunta.setText("¿No tienes cuenta?");
            } else {
                boton_crear.setText("Crear Cuenta");
                label_pregunta.setText("¿Ya tienes cuenta?");
            }
        }
    }

    public JPanel getPanel_inicio() {
        return panel_inicio;
    }
}
