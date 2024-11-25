package MVC;

import GUI.Ventana;
import clases.ObserverJugador;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class VistaJugadorGUI extends Ventana implements ObserverJugador {

    private int id;
    private Controller controller;
    private boolean presiono = false;
    private boolean fin = false;

    public VistaJugadorGUI(Controller controller, int id) {
        super();
        this.controller = controller;
        this.id = id;
        frame.setTitle("Jugador " + id);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.notificarAbandono(id);
            }
        });
        this.botonTirar.setEnabled(false);
    }

    public void updateTurno() {
        this.botonTirar.setEnabled(true);

        synchronized (this) { // se bloquea el hilo para el objeto
            try {
                while (!presiono) {
                    wait(); // Esperar hasta que jugador presione el botón
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.tirarDados();
        presiono = false; // Resetear la bandera para el siguiente turno
    }

    @Override
    protected void pulsarBotonTirar() {
        this.tirarDados();
        presiono = true;

        synchronized (this) {
            notify(); // libera el lock del wait llamado en update turno
        }
    }

    public void updateFinTurno(String dados, int puntos, boolean reroll) {

        // Crear un JDialog no modal (no bloquea las ventanas) que contiene el JOptionPane
        JDialog dialogo = new JDialog(this.frame, "");
        dialogo.setModal(false); // No bloquea otras ventanas
        dialogo.setUndecorated(true);

        JOptionPane opcion = new JOptionPane("Obtuviste los dados "+ dados+ ", en caso\n de querer" +
                " volver a tirar seleccione los Dados a tirar.",
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        //dados a cambiar
        JCheckBox checkBox1 = new JCheckBox("D1");
        JCheckBox checkBox2 = new JCheckBox("D2");
        JCheckBox checkBox3 = new JCheckBox("D3");
        JCheckBox checkBox4 = new JCheckBox("D4");
        JCheckBox checkBox5 = new JCheckBox("D5");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(checkBox1);
        panel.add(checkBox2);
        panel.add(checkBox3);
        panel.add(checkBox4);
        panel.add(checkBox5);

        opcion.add(panel, BorderLayout.NORTH);

        // centrar ventana emergente
        int frameX = this.frame.getX();
        int frameY = this.frame.getY();
        int frameWidth = this.frame.getWidth();
        int frameHeight = this.frame.getHeight();
        int dialogX = frameX + ((frameWidth - dialogo.getWidth()) / 2) -50;
        int dialogY = frameY + ((frameHeight - dialogo.getHeight()) / 2) - 50;

        dialogo.setLocation(dialogX, dialogY);

        dialogo.setContentPane(opcion);

        dialogo.pack();
        dialogo.setVisible(true);

        opcion.addPropertyChangeListener(evento -> {
            if (evento.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                // Obtener la respuesta del usuario
                int respuesta = (int) opcion.getValue();
                dialogo.dispose();

                if (reroll) {
                    if (respuesta == JOptionPane.YES_OPTION) {
                        boolean[] dadosElejidos = new boolean[5];
                        dadosElejidos[0] = checkBox1.isSelected();
                        dadosElejidos[1] = checkBox2.isSelected();
                        dadosElejidos[2] = checkBox3.isSelected();
                        dadosElejidos[3] = checkBox4.isSelected();
                        dadosElejidos[4] = checkBox5.isSelected();

                        controller.tirarDados(this.id, dadosElejidos);
                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        controller.confirmarDados();
                        this.mostrarMensaje("Los números obtenidos son " + dados + "\n" +
                                "Lo que corresponde a un total de " + puntos + " puntos.\n", true);
                    }
                } else {
                    controller.confirmarDados();
                    this.mostrarMensaje("Los números obtenidos son " + dados + "\n" +
                            "Lo que corresponde a un total de " + puntos + " puntos.\n", true);
                }
            }
        });
    }

    private void tirarDados() {
        if (presiono && fin){
            this.cerrarVentana();
        }
        else if(presiono){
            this.botonTirar.setEnabled(false);
            presiono = false;
            controller.tirarDados(this.id);
        }

    }

    public void updatePuntos(String string){
        this.actualizarPuntaje(string);
    }

    public void updateStatus(String mensaje) {
        this.mostrarMensaje("               " + mensaje , false);
    }

    public void finalizar(){
        this.mostrarMensaje("               \n\n            Fin de partida, pressione tirar dados para salir\n", false);
        fin = true;
        this.botonTirar.setEnabled(true);

    }
}