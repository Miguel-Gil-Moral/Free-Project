import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Juego {
    private JPanel panel_juego;
    private JPanel panel_cabecera;
    private JLabel label_titulo;
    private JPanel panel_central;
    private JPanel panel_matriz;
    private JPanel panel_ventanas;
    private JPanel panel_ventana_central;
    private JPanel panel_ventanas_derecha;
    private JPanel panel_ventanas_izquierda;
    private JPanel panel_fondo_para_la_ventana_que_no_tengo_ni_idea_porque;
    private JLabel label_guardada;
    private JPanel panel_guardada;
    private JPanel panel_interior_guardado;
    private JPanel panel_pausa;
    private JPanel panel_ventanas_pequenyas;
    private JLabel label_interior_guardada;
    private JLabel label_pausa;
    private JPanel panel_puntos;
    private JPanel panel_nivel;
    private JPanel panel_linea;
    private JLabel label_puntos;
    private JPanel panel_interior_puntos;
    private JLabel label_interior_puntos;
    private JPanel panel_interior_nivel;
    private JLabel label_interior_nivel;
    private JLabel label_nivel;
    private JPanel panel_interior_linea;
    private JLabel label_linea;
    private JLabel label_interior_linea;
    private JPanel panel_fondo_siguiente;
    private JPanel panel_siguiente;
    private JLabel label_siguiente;
    private JPanel panel_interior_siguiente;
    private JLabel label_siguiente_1;
    private JLabel label_siguiente_2;
    private JLabel label_siguiente_3;

    private int vecesRotada = 0;
    private int[] posicionPieza = new int[4], bordeIzquierdo = new int[] {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            110, 120, 130, 140, 150, 160, 170, 180, 190},
    bordeDerecho = new int[]{9, 19, 29, 39, 49, 59, 69, 79, 89, 99, 109, 119, 129, 139, 149, 159, 169, 179, 189, 199},
    randomPiezas, suelo = {190, 191, 192, 193, 194, 195, 196, 197, 198, 199}, techo = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private boolean clickedC = false;
    private Timer caidaPieza;

    public Juego() {
        panel_juego.setPreferredSize(new Dimension(720, 1280));
        panel_juego.setSize(720, 1280);

        panel_cabecera.setSize(new Dimension(panel_juego.getWidth(), panel_juego.getHeight() / 15));
        panel_central.setSize(new Dimension(panel_juego.getWidth(), panel_juego.getHeight() - 100));

        panel_juego.add(panel_cabecera);
        panel_juego.add(panel_central);

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

            System.out.println("Se ha cargado la fuente de Open Sans, Archivo Black, y Bungee a la pantalla Juego");
            label_titulo.setFont(new Font("Archivo Black", Font.PLAIN, 40));
            label_guardada.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_puntos.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_linea.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_nivel.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_siguiente.setFont(new Font("Bungee", Font.PLAIN, 20));
            label_interior_linea.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_interior_nivel.setFont(new Font("Open Sans", Font.PLAIN, 20));
            label_interior_puntos.setFont(new Font("Open Sans", Font.PLAIN, 20));
        } catch (FontFormatException | IOException e) {
            System.out.println("No se ha podido cargar las fuentes");
        }

        Icon iconoPausa = getIcon("src/imagenes/pausa.png", 48);
        label_pausa.setIcon(iconoPausa);
        label_pausa.addMouseListener(new PausarJuegoListener());
        label_interior_guardada.setName("label_interior_guardada");

        generarMatriz();
        randomPiezas = generarRandom();

        sacarPieza();

        panel_matriz.setFocusable(true);
        panel_matriz.addKeyListener(new PiezaListener());

        caidaPieza = new Timer(1000, new CaidaPiezaListener());

        caidaPieza.start();
    }

    private void generarMatriz() {
        panel_matriz.setLayout(new GridLayout(20, 10));

        Icon pixel = getIcon("src/imagenes/Pixel Matriz.png", 24);

        for (int i = 0; i < 20 * 10; i++) {
            JLabel label_pixel = new JLabel(pixel);
            label_pixel.setName("label_vacio");
            panel_matriz.add(label_pixel);
        }
    }

    private int[] generarRandom() {
        Random random = new Random();
        randomPiezas = new int[7];

        for (int i = 0; i < randomPiezas.length; i++) {
            randomPiezas[i] = random.nextInt(7);
        }

        return randomPiezas;
    }

    public void sacarPieza() {
        JLabel pieza1 = null, pieza2 = null, pieza3 = null, pieza4 = null;
        String[] rutaPiezas = {"src/imagenes/Piezas Tetris/Pieza I.png", "src/imagenes/Piezas Tetris/Pieza J.png",
                "src/imagenes/Piezas Tetris/Pieza L.png", "src/imagenes/Piezas Tetris/Pieza O.png",
                "src/imagenes/Piezas Tetris/Pieza S.png", "src/imagenes/Piezas Tetris/Pieza T.png",
                "src/imagenes/Piezas Tetris/Pieza Z.png"};

        switch (randomPiezas[0]) {
            case 0:
                posicionPieza = new int[]{6, 5, 4, 3};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelI = getIcon("src/imagenes/Pixeles Tetris/Pixel I.png", 24);
                introducirIconos(icono_pixelI, pieza1, pieza2, pieza3, pieza4);
                break;
            case 1:
                posicionPieza = new int[]{15, 14, 13, 3};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelJ = getIcon("src/imagenes/Pixeles Tetris/Pixel J.png", 24);
                introducirIconos(icono_pixelJ, pieza1, pieza2, pieza3, pieza4);
                break;
            case 2:
                posicionPieza = new int[]{15, 14, 13, 5};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelL = getIcon("src/imagenes/Pixeles Tetris/Pixel L.png", 24);
                introducirIconos(icono_pixelL, pieza1, pieza2, pieza3, pieza4);
                break;
            case 3:
                posicionPieza = new int[]{15, 14, 5, 4};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelO = getIcon("src/imagenes/Pixeles Tetris/Pixel O.png", 24);
                introducirIconos(icono_pixelO, pieza1, pieza2, pieza3, pieza4);
                break;
            case 4:
                posicionPieza = new int[]{14, 13, 5, 4};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelS = getIcon("src/imagenes/Pixeles Tetris/Pixel S.png", 24);
                introducirIconos(icono_pixelS, pieza1, pieza2, pieza3, pieza4);
                break;
            case 5:
                posicionPieza = new int[]{15, 14, 13, 4};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelT = getIcon("src/imagenes/Pixeles Tetris/Pixel T.png", 24);
                introducirIconos(icono_pixelT, pieza1, pieza2, pieza3, pieza4);
                break;
            case 6:
                posicionPieza = new int[]{15, 14, 4, 3};

                pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
                pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
                pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
                pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

                Icon icono_pixelZ = getIcon("src/imagenes/Pixeles Tetris/Pixel Z.png", 24);
                introducirIconos(icono_pixelZ, pieza1, pieza2, pieza3, pieza4);
                break;
        }

        pieza1.setName("label_pieza");
        pieza2.setName("label_pieza");
        pieza3.setName("label_pieza");
        pieza4.setName("label_pieza");

        label_siguiente_1.setIcon(getIcon(rutaPiezas[randomPiezas[1]]));
        label_siguiente_2.setIcon(getIcon(rutaPiezas[randomPiezas[2]]));
        label_siguiente_3.setIcon(getIcon(rutaPiezas[randomPiezas[3]]));
    }

    private static Icon getIcon(String rutaPieza, int tamanyo) {
        ImageIcon pixel = new ImageIcon(rutaPieza);
        return new ImageIcon(
                pixel.getImage().getScaledInstance(tamanyo, tamanyo, Image.SCALE_DEFAULT)
        );
    }

    private static Icon getIcon(String rutaPieza) {
        ImageIcon pixel = new ImageIcon(rutaPieza);
        return new ImageIcon(
                pixel.getImage().getScaledInstance(36, 24, Image.SCALE_DEFAULT)
        );
    }

    public class PiezaListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            boolean piezaMovida = false, pegadoBorde = false;
            ImageIcon imagenPixel = new ImageIcon("src/imagenes/Pixel Matriz.png");
            Icon pixel = new ImageIcon(
                    imagenPixel.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT)
            );
            JLabel pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
            JLabel pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
            JLabel pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
            JLabel pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);

            JLabel pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva;

            Icon pixelPieza = pieza1.getIcon();

            int numeroMenor = posicionPieza[3];
            int numeroMayor = posicionPieza[0];

            for (int i : bordeIzquierdo) {
                for (int j : posicionPieza) {
                    if (j == i) {
                        pegadoBorde = true;
                    }
                }
            }
            for (int i : bordeDerecho) {
                for (int j : posicionPieza) {
                    if (j == i) {
                        pegadoBorde = true;
                    }
                }
            }
            for (int i : techo) {
                for (int j : posicionPieza) {
                    if (j == i) {
                        pegadoBorde = true;
                    }
                }
            }

//            int numeroMenor = pillarNumeroMenor(posicionPieza);
//            int numeroMayor = pillarNumeroMayor(posicionPieza);

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (!pegadoBorde) {
                        do {
                            JLabel piezaIzquierdaArriba = (JLabel) panel_matriz.getComponent(numeroMenor - 1);
                            JLabel piezaIzquierdaAbajo = (JLabel) panel_matriz.getComponent(numeroMayor - 1);
                            if (piezaIzquierdaArriba.getName().equals("label_vacio") && piezaIzquierdaAbajo.getName().equals("label_vacio")) {
                                pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 1);
                                pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] - 1);
                                pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 1);
                                pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 1);

                                Juego.introducirIconos(pixel, pieza1, pieza2, pieza3, pieza4);

                                Juego.introducirIconos(pixelPieza, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva);

                                posicionPieza[0] = posicionPieza[0] - 1;
                                posicionPieza[1] = posicionPieza[1] - 1;
                                posicionPieza[2] = posicionPieza[2] - 1;
                                posicionPieza[3] = posicionPieza[3] - 1;

                                piezaMovida = true;
                            } else if (piezaIzquierdaArriba.getName().equals("label_pieza")) {
                                numeroMenor -= 1;
                                piezaIzquierdaArriba = (JLabel) panel_matriz.getComponent(numeroMenor);
                            } else if (piezaIzquierdaAbajo.getName().equals("label_pieza")) {
                                numeroMayor -= 1;
                                piezaIzquierdaAbajo = (JLabel) panel_matriz.getComponent(numeroMayor);
                                piezaMovida = false;
                            }
                        } while (!piezaMovida);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!pegadoBorde) {
                        do {
                            piezaMovida = true;
                            JLabel piezaDerechaArriba = (JLabel) panel_matriz.getComponent(numeroMenor + 1);
                            JLabel piezaDerechaAbajo = (JLabel) panel_matriz.getComponent(numeroMayor + 1);
                            if (piezaDerechaArriba.getName().equals("label_vacio") && piezaDerechaAbajo.getName().equals("label_vacio")) {
                                pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 1);
                                pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 1);
                                pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 1);
                                pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 1);

                                Juego.introducirIconos(pixel, pieza1, pieza2, pieza3, pieza4);

                                Juego.introducirIconos(pixelPieza, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva);

                                posicionPieza[0] = posicionPieza[0] + 1;
                                posicionPieza[1] = posicionPieza[1] + 1;
                                posicionPieza[2] = posicionPieza[2] + 1;
                                posicionPieza[3] = posicionPieza[3] + 1;

                            } else if (piezaDerechaArriba.getName().equals("label_pieza")) {
                                numeroMenor += 1;
                                piezaDerechaArriba = (JLabel) panel_matriz.getComponent(numeroMenor);
                                piezaMovida = false;
                            } else if (piezaDerechaAbajo.getName().equals("label_pieza")) {
                                numeroMayor += 1;
                                piezaDerechaAbajo = (JLabel) panel_matriz.getComponent(numeroMayor);
                                piezaMovida = false;
                            }
                        } while (!piezaMovida);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (vecesRotada == 4) {
                        vecesRotada = 0;
                    }
                    switch (randomPiezas[0]) {
                        case 0:
                            switch (vecesRotada) {
                                case 0:
                                    pieza1.setIcon(pixel);
                                    pieza2.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 18);
                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 9);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza2_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 18;
                                    posicionPieza[1] += 9;
                                    posicionPieza[3] -= 9;
                                    break;
                                case 1:
                                    pieza1.setIcon(pixel);
                                    pieza2.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 22);
                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] - 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 11);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza2_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 22;
                                    posicionPieza[1] -= 11;
                                    posicionPieza[3] += 11;
                                    break;
                                case 2:
                                    pieza1.setIcon(pixel);
                                    pieza2.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 18);
                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] - 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 9);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza2_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 18;
                                    posicionPieza[1] -= 9;
                                    posicionPieza[3] += 9;
                                    break;
                                case 3:
                                    // 14, 24, 34, 44
                                    pieza1.setIcon(pixel);
                                    pieza2.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 22);
                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 11);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza2_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 22;
                                    posicionPieza[1] += 11;
                                    posicionPieza[3] -= 11;
                                    break;
                            }
                            break;
                        case 1:
                            switch (vecesRotada) {
                                case 0:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 9;
                                    posicionPieza[2] -= 9;
                                    posicionPieza[3] += 2;
                                    break;
                                case 1:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 11;
                                    posicionPieza[2] += 11;
                                    posicionPieza[3] += 20;
                                    break;
                                case 2:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 9;
                                    posicionPieza[2] += 9;
                                    posicionPieza[3] -= 2;
                                    break;
                                case 3:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 11;
                                    posicionPieza[2] -= 11;
                                    posicionPieza[3] -= 20;
                                    break;
                            }
                            break;
                        case 2:
                            switch (vecesRotada) {
                                case 0:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 9;
                                    posicionPieza[2] -= 9;
                                    posicionPieza[3] += 20;
                                    break;
                                case 1:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 11;
                                    posicionPieza[2] += 11;
                                    posicionPieza[3] -= 2;
                                    break;
                                case 2:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 9;
                                    posicionPieza[2] += 9;
                                    posicionPieza[3] -= 20;
                                    break;
                                case 3:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 11;
                                    posicionPieza[2] -= 11;
                                    posicionPieza[3] += 2;
                                    break;
                            }
                            break;
                        case 3:
                            System.out.println("Como vas a rotar un cubo tontico");
                            break;
                        case 4:
                            switch (vecesRotada) {
                                case 0:
                                    pieza2.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza2.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] - 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 20);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 11);

                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[1] -= 9;
                                    posicionPieza[2] += 20;
                                    posicionPieza[3] += 11;
                                    break;
                                case 1:
                                    pieza2.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza2.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 2);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 9);

                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[1] += 11;
                                    posicionPieza[2] -= 2;
                                    posicionPieza[3] += 9;
                                    break;
                                case 2:
                                    pieza2.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza2.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 20);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 11);

                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[1] += 9;
                                    posicionPieza[2] -= 20;
                                    posicionPieza[3] -= 11;
                                    break;
                                case 3:
                                    pieza2.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza2.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] - 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 2);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 9);

                                    pieza2_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza2_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[1] -= 11;
                                    posicionPieza[2] += 2;
                                    posicionPieza[3] -= 9;
                                    break;
                            }
                            break;
                        case 5:
                            switch (vecesRotada) {
                                case 0:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 11);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 9;
                                    posicionPieza[2] -= 9;
                                    posicionPieza[3] += 11;
                                    break;
                                case 1:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 9);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 11;
                                    posicionPieza[2] += 11;
                                    posicionPieza[3] += 9;
                                    break;
                                case 2:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 11);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 9;
                                    posicionPieza[2] += 9;
                                    posicionPieza[3] -= 11;
                                    break;
                                case 3:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 9);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");
                                    posicionPieza[0] += 11;
                                    posicionPieza[2] -= 11;
                                    posicionPieza[3] -= 9;
                                    break;
                            }
                            break;
                        case 6:
                            switch (vecesRotada) {
                                case 0:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 9;
                                    posicionPieza[2] += 11;
                                    posicionPieza[3] += 2;
                                    break;
                                case 1:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 11;
                                    posicionPieza[2] += 9;
                                    posicionPieza[3] += 20;
                                    break;
                                case 2:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] - 9);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 11);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 2);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] -= 9;
                                    posicionPieza[2] -= 11;
                                    posicionPieza[3] -= 2;
                                    break;
                                case 3:
                                    pieza1.setIcon(pixel);
                                    pieza3.setIcon(pixel);
                                    pieza4.setIcon(pixel);
                                    pieza1.setName("label_vacio");
                                    pieza3.setName("label_vacio");
                                    pieza4.setName("label_vacio");

                                    pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 11);
                                    pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] - 9);
                                    pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] - 20);

                                    pieza1_nueva.setIcon(pixelPieza);
                                    pieza3_nueva.setIcon(pixelPieza);
                                    pieza4_nueva.setIcon(pixelPieza);
                                    pieza1_nueva.setName("label_pieza");
                                    pieza3_nueva.setName("label_pieza");
                                    pieza4_nueva.setName("label_pieza");

                                    posicionPieza[0] += 11;
                                    posicionPieza[2] -= 9;
                                    posicionPieza[3] -= 20;
                                    break;
                            }
                            break;
                    }
                    vecesRotada++;
                    break;
                case KeyEvent.VK_C:
                    if (!clickedC) {
                        clickedC = true;
                        Icon pieza = null;
                        switch (randomPiezas[0]) {
                            case 0:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza I.png");
                                break;
                            case 1:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza J.png");
                                break;
                            case 2:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza L.png");
                                break;
                            case 3:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza O.png");
                                break;
                            case 4:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza S.png");
                                break;
                            case 5:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza T.png");
                                break;
                            case 6:
                                pieza = getIcon("src/imagenes/Piezas Tetris/Pieza Z.png");
                                break;
                        }
                        label_interior_guardada.setIcon(pieza);
                        if (label_interior_guardada.getName().equals("label_interior_guardada")) {
                            cambiarPosicionRandom();
                            label_interior_guardada.setName(String.valueOf(randomPiezas[0]));
                        } else {
                            int piezaNueva = Integer.parseInt(label_interior_guardada.getName());
                            label_interior_guardada.setName(String.valueOf(randomPiezas[0]));
                            cambiarPosicionRandom(piezaNueva);
                        }

                        introducirIconos(pixel, pieza1, pieza2, pieza3, pieza4, "label_vacio");

                        vecesRotada = 0;
                        sacarPieza();
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    int fila = 190;
                    boolean piezaFija = false;
                    for (int filasMatriz = 0; filasMatriz < bordeDerecho.length; filasMatriz++) {
                        JLabel pieza = (JLabel) panel_matriz.getComponent(filasMatriz + fila);
                        if (pieza.getName().equals("label_pieza_fija")) {
                            piezaFija = true;
                        }
                    }
                    if (piezaFija) {

                    }
//                    boolean hayPiezas;
//
//                    String posicionNueva1 = String.valueOf(posicionPieza[0]);
//                    String posicionNueva2 = String.valueOf(posicionPieza[1]);
//                    String posicionNueva3 = String.valueOf(posicionPieza[2]);
//                    String posicionNueva4 = String.valueOf(posicionPieza[3]);
//
//                    int suelo1 = pillarPosicionSuelo(posicionNueva1);
//                    int suelo2 = pillarPosicionSuelo(posicionNueva2);
//                    int suelo3 = pillarPosicionSuelo(posicionNueva3);
//                    int suelo4 = pillarPosicionSuelo(posicionNueva4);
//
//                    do {
//                        hayPiezas = false;
//                        pieza1_nueva = (JLabel) panel_matriz.getComponent(suelo1);
//                        pieza2_nueva = (JLabel) panel_matriz.getComponent(suelo2);
//                        pieza3_nueva = (JLabel) panel_matriz.getComponent(suelo3);
//                        pieza4_nueva = (JLabel) panel_matriz.getComponent(suelo4);
//
//                        if (pieza1_nueva.getName().equals("label_pieza_fija") || pieza2_nueva.getName().equals("label_pieza_fija")
//                                || pieza3_nueva.getName().equals("label_pieza_fija") || pieza4_nueva.getName().equals("label_pieza_fija")) {
//                            suelo1 -= 10;
//                            suelo2 -= 10;
//                            suelo3 -= 10;
//                            suelo4 -= 10;
//                            hayPiezas = true;
//                        } else {
//                            introducirIconos(pixel, pieza1, pieza2, pieza3, pieza4, "label_vacio");
//                            introducirIconos(pixelPieza, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva, "label_pieza_fija");
//
//                            vecesRotada = 0;
//                            label_interior_puntos.setText(String.valueOf(Integer.parseInt(label_interior_puntos.getText()) + 36));
//                            comprobarLineaCompleta();
//                            cambiarPosicionRandom();
//                            sacarPieza();
//                        }
//                    } while (hayPiezas);
                    break;
                case KeyEvent.VK_DOWN:
                    boolean llegoAlSuelo = false;
                    for (int i = 0; i < suelo.length; i++) {
                        for (int j : posicionPieza) {
                            if (suelo[i] == j) {
                                llegoAlSuelo = true;
                            }
                        }
                    }
                    if (!llegoAlSuelo) {
                        pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 10);
                        pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 10);
                        pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 10);
                        pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 10);

                        if (pieza1_nueva.getName().equals("label_pieza_fija") || pieza2_nueva.getName().equals("label_pieza_fija")
                                || pieza3_nueva.getName().equals("label_pieza_fija") || pieza4_nueva.getName().equals("label_pieza_fija")) {
                            establecerPiezaFija(pieza1, pieza2, pieza3, pieza4);
                        } else {
                            cambiarPosicionPieza(pieza1, pieza2, pieza3, pieza4, pixelPieza, pixel, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva);

                            label_interior_puntos.setText(String.valueOf(Integer.parseInt(label_interior_puntos.getText()) + 1));
                        }
                    }
                    break;
            }
        }
    }

//    private int pillarPosicionSuelo(String posicionNueva) {
//        int posicionReturn;
//        if (posicionNueva.length() == 3) {
//            posicionReturn = 190 + Integer.parseInt(String.valueOf(posicionNueva.charAt(2)));
//        } else if (posicionNueva.length() == 2) {
//            posicionReturn = 190 + Integer.parseInt(String.valueOf(posicionNueva.charAt(1)));
//        } else {
//            posicionReturn = 180 + Integer.parseInt(posicionNueva);
//        }
//        return posicionReturn;
//    }

    private static void introducirIconos(Icon imagenPixel, JLabel pieza1, JLabel pieza2, JLabel pieza3, JLabel pieza4, String nombre) {
        pieza1.setIcon(imagenPixel);
        pieza2.setIcon(imagenPixel);
        pieza3.setIcon(imagenPixel);
        pieza4.setIcon(imagenPixel);
        pieza1.setName(nombre);
        pieza2.setName(nombre);
        pieza3.setName(nombre);
        pieza4.setName(nombre);
    }

    private static void introducirIconos(Icon pixel, JLabel pieza1, JLabel pieza2, JLabel pieza3, JLabel pieza4) {
        pieza1.setIcon(pixel);
        pieza2.setIcon(pixel);
        pieza3.setIcon(pixel);
        pieza4.setIcon(pixel);
    }

//    private static int pillarNumeroMenor(int[] posicionPieza) {
//        int numeroMenor = posicionPieza[0];
//        for (int i : posicionPieza ) {
//            if (numeroMenor > i) {
//                numeroMenor = i;
//            }
//        }
//        return numeroMenor;
//    }
//
//    private static int pillarNumeroMayor(int[] posicionPieza) {
//        int numeroMayor = posicionPieza[0];
//        for (int i : posicionPieza ) {
//            if (numeroMayor < i) {
//                numeroMayor = i;
//            }
//        }
//        return numeroMayor;
//    }

    private void cambiarPosicionRandom() {
        for (int i = 0; i < randomPiezas.length; i++) {
            if (i != 6) {
                randomPiezas[i] = randomPiezas[i + 1];
            } else {
                randomPiezas[i] = new Random().nextInt(randomPiezas.length);
            }
        }
    }

    private void cambiarPosicionRandom(int pieza) {
        randomPiezas[0] = pieza;
    }

    public class CaidaPiezaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean llegoAlSuelo = false;
            for (int i = 0; i < suelo.length; i++) {
                for (int j : posicionPieza) {
                    if (suelo[i] == j) {
                        llegoAlSuelo = true;
                    }
                }
            }

            JLabel pieza1 = (JLabel) panel_matriz.getComponent(posicionPieza[0]);
            JLabel pieza2 = (JLabel) panel_matriz.getComponent(posicionPieza[1]);
            JLabel pieza3 = (JLabel) panel_matriz.getComponent(posicionPieza[2]);
            JLabel pieza4 = (JLabel) panel_matriz.getComponent(posicionPieza[3]);
            if (!llegoAlSuelo) {
                Icon iconoPieza = pieza1.getIcon();
                Icon pixel = getIcon("src/imagenes/Pixel Matriz.png", 24);


                JLabel pieza1_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[0] + 10);
                JLabel pieza2_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[1] + 10);
                JLabel pieza3_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[2] + 10);
                JLabel pieza4_nueva = (JLabel) panel_matriz.getComponent(posicionPieza[3] + 10);

                if (pieza1_nueva.getName().equals("label_pieza_fija") || pieza2_nueva.getName().equals("label_pieza_fija")
                        || pieza3_nueva.getName().equals("label_pieza_fija") || pieza4_nueva.getName().equals("label_pieza_fija")) {
                    pieza1.setName("label_pieza_fija");
                    pieza2.setName("label_pieza_fija");
                    pieza3.setName("label_pieza_fija");
                    pieza4.setName("label_pieza_fija");
                    clickedC = false;
                    vecesRotada = 0;
                    cambiarPosicionRandom();
                    sacarPieza();
                } else {
                    cambiarPosicionPieza(pieza1, pieza2, pieza3, pieza4, iconoPieza, pixel, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva);
                }
            } else {
                establecerPiezaFija(pieza1, pieza2, pieza3, pieza4);
            }
        }
    }

    private void establecerPiezaFija(JLabel pieza1, JLabel pieza2, JLabel pieza3, JLabel pieza4) {
        pieza1.setName("label_pieza_fija");
        pieza2.setName("label_pieza_fija");
        pieza3.setName("label_pieza_fija");
        pieza4.setName("label_pieza_fija");

        clickedC = false;
        vecesRotada = 0;
        comprobarLineaCompleta();
        cambiarPosicionRandom();
        sacarPieza();
    }

    private void cambiarPosicionPieza(JLabel pieza1, JLabel pieza2, JLabel pieza3, JLabel pieza4, Icon iconoPieza, Icon pixel, JLabel pieza1_nueva, JLabel pieza2_nueva, JLabel pieza3_nueva, JLabel pieza4_nueva) {
        introducirIconos(pixel, pieza1, pieza2, pieza3, pieza4, "label_vacio");
        introducirIconos(iconoPieza, pieza1_nueva, pieza2_nueva, pieza3_nueva, pieza4_nueva, "label_pieza");

        posicionPieza[0] += 10;
        posicionPieza[1] += 10;
        posicionPieza[2] += 10;
        posicionPieza[3] += 10;
    }

    public class PausarJuegoListener extends MouseAdapter {
        private final String[] OPCIONES = {"Salir", "Seguir"};

        @Override
        public void mousePressed(MouseEvent e) {
            caidaPieza.stop();
            int respuesta = JOptionPane.showOptionDialog(
                    null,
                    "¿Quiere salir del juego?",
                    "Pausa",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    OPCIONES,
                    OPCIONES[1]
            );

            if (respuesta == 0) {
                System.exit(0);
            } else {
                caidaPieza.start();
            }
        }
    }

    public void comprobarLineaCompleta() {
        int fila = 190;
        boolean lineaCompleta;
        for (int filasMatriz = 0; filasMatriz < bordeDerecho.length; filasMatriz++) {
            ArrayList<JLabel> listaLabels = new ArrayList<>();
            lineaCompleta = true;
            for (int i = 0; i < suelo.length; i++) {
                JLabel label = (JLabel) panel_matriz.getComponent(fila + i);
                if (label.getName().equals("label_vacio")) {
                    lineaCompleta = false;
                }
                listaLabels.add(label);
            }
            if (lineaCompleta) {
                int columna = 0;
                for (JLabel label : listaLabels) {
                    if ((fila - 10) > 0) {
                        JLabel label2 = (JLabel) panel_matriz.getComponent((fila + columna) - 10);
                        label.setIcon(label2.getIcon());
                        label.setName(label2.getName());
                        columna++;
                    }
                }
            }
            fila -= 10;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Juego");
        frame.setContentPane(new Juego().panel_juego);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public JPanel getPanel_juego() {
        return panel_juego;
    }
}
