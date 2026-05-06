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
        int anchoPantalla = 720, altoPantalla = 1280;
        panel_inicio.setPreferredSize(new Dimension(anchoPantalla, altoPantalla));
        panel_inicio.setSize(new Dimension(anchoPantalla, altoPantalla));

        panel_inicio.add(panel_cabecera);
        panel_inicio.add(panel_central);

        panel_cabecera.setSize(new Dimension(panel_inicio.getWidth(), panel_inicio.getHeight() / 15));

        campo_nombre.setPreferredSize(new Dimension(48, 48));
        campo_contrasenya.setPreferredSize(new Dimension(48, 48));
        boton_crear.setPreferredSize(new Dimension(48, 48));

        panel_registro.setFocusable(true);
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

        boton_crear.addMouseListener(new BotonCrearListener());
        boton_crear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    crearCuenta();
                }
            }
        });

        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuentes/Open_Sans/static/OpenSans-Regular.ttf"));

            openSans.deriveFont(12f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(openSans);

            System.out.println("Se ha cargado la fuente de Open Sans a la aplicación");

            boton_crear.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_pregunta.setFont(new Font("Open Sans", Font.PLAIN, 12));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }



        label_pregunta.addMouseListener(new CambiarSesionListener());

        label_titulo.setFont(new Font("Arial Black", Font.BOLD, 40));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inicio");
        frame.setContentPane(new Inicio().panel_inicio);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(600, 0);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/imagenes/icono.png");
        frame.setIconImage(icono);
    }

    public class BotonCrearListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            crearCuenta();
        }
    }

    private void crearCuenta() {
        String nombre = "", contrasenya = "";
        if (!campo_nombre.getText().equals("Introduce el nombre") || !campo_contrasenya.getText().equals("Introduce la contraseña")) {
            try {
                String selectQuery = "select nombre, contrasenya from usuario where nombre = ? and contrasenya = ?";
                String insertQuery = "insert into usuario (nombre, contrasenya) values (?, ?)";

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tetris","admin","admin");
                PreparedStatement ps = con.prepareStatement(selectQuery);
                ps.setString(1, campo_nombre.getText());
                ps.setString(2, campo_contrasenya.getText());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    nombre = rs.getString("nombre");
                    contrasenya = rs.getString("contrasenya");
                }
                if (nombre.equals(campo_nombre.getText()) && contrasenya.equals(campo_contrasenya.getText()) && boton_crear.getText().equals("Crear Cuenta")) {
                    JOptionPane.showMessageDialog(null, "Ya hay un usuario igual con la misma contraseña");
                } else if (nombre.isEmpty() && contrasenya.isEmpty() && boton_crear.getText().equals("Iniciar Sesión")) {
                    JOptionPane.showMessageDialog(null, "El nombre o la contraseña no son correctos");
                } else if (!nombre.equals(campo_nombre.getText()) && !contrasenya.equals(campo_contrasenya.getText()) && boton_crear.getText().equals("Crear Cuenta")){
                    PreparedStatement ps2 = con.prepareStatement(insertQuery);
                    ps2.setString(1, campo_nombre.getText());
                    ps2.setString(2, campo_contrasenya.getText());
                    int filas = ps2.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(null, "Se han introducido correctamente a la base de datos");
                    }
                    ps2.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Se ha iniciado sesión correctamente");
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
}
