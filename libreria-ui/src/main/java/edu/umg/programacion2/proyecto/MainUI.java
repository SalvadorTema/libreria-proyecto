package edu.umg.programacion2.proyecto;

import edu.umg.programacion2.proyecto.ui.VentanaPrincipal;

import javax.swing.*;

public class MainUI {
    public static void main(String[] args) {
        // Ejecución segura de la interfaz Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Aplica el Look & Feel del sistema operativo para mejor apariencia
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}