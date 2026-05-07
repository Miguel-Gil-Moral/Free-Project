package swing;

import javax.swing.*;

public class Perfil {
    private JPanel panel_perfil;
    private JPanel panel_cabecera;
    private JPanel panel_central;

    public Perfil(JPanel panel_cabecera) {
        this.panel_cabecera = panel_cabecera;

        panel_perfil.add(panel_cabecera);
        panel_perfil.add(panel_central);
    }

    public JPanel getPanel_perfil() {
        return panel_perfil;
    }
}
