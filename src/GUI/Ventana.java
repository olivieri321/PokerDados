package GUI;

import javax.swing.*;
import java.awt.*;

public abstract class Ventana {
    protected JFrame frame;
    protected JTextArea estado;
    protected JButton botonTirar;
    protected Container panelPrincipal;
    protected Label puntaje;

    public Ventana(){
        construirventana("Jugador");
    }

    private void construirventana(String jugador) {
        // Ventana principal
        frame = new JFrame(jugador);

        // Elementos principales
        estado = new JTextArea(4, 1);
        botonTirar = new JButton("Tirar Dados");
        puntaje = new Label("Puntaje: 0"); // Texto inicial más claro
        panelPrincipal = frame.getContentPane();

        // Configuración del layout
        panelPrincipal.setLayout(new BorderLayout());

        // Tamaño de la ventana
        frame.setBounds(100, 100, 400, 300);

        // Panel para botones
        JPanel panelAcciones = new JPanel();
        panelAcciones.add(botonTirar);
        panelAcciones.setLayout(new FlowLayout());
        panelAcciones.setBackground(new Color(40, 40, 160));

        // Configuración de puntaje
        JPanel panelPuntaje = new JPanel();
        panelPuntaje.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelPuntaje.add(puntaje);

        // Configuración de estado
        estado.setOpaque(true);
        estado.setBackground(new Color(40, 200, 80));
        estado.setPreferredSize(new Dimension(300, 200));

        // Agregar elementos al contenedor principal
        panelPrincipal.add(panelPuntaje, BorderLayout.NORTH);  // Puntaje en la parte superior
        panelPrincipal.add(estado, BorderLayout.CENTER);       // Estado en el centro
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH); // Botones en la parte inferior


        // Listeners

        botonTirar.addActionListener(e -> pulsarBotonTirar()); // apretar tirar accionara funcion pulsarBotonTirar

        // Mostrar ventana
        frame.setVisible(true);
    }


    protected void mostrarMensaje(String mensaje, boolean reemplazar){
        if (reemplazar){
            estado.setText(mensaje);
        }
        else{
            estado.append(mensaje);
        }
    }

    protected void actualizarPuntaje(String puntaje){
        this.puntaje.setText(puntaje);
    }

    public void cerrarVentana(){
        frame.dispose();
    }

    protected abstract void pulsarBotonTirar();
}
