package MVC;

import GUI.Ventana;
import clases.ObserverJugador;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        synchronized (this) { // el hilo que ejecuta update turno tiene control exclusivo de este objeto mientras espera
            try {
                while (!presiono) {
                    wait(); // Esperar hasta que jugador presione el botón
                }
            } catch (InterruptedException e) { // se requiere que el wait este dentro del codigo try catch
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

        // Notificar al modelo que el jugador ha presionado el botón
        synchronized (this) {
            notify();
        }
    }

    public void updateFinTurno(String dados, int puntos, boolean reroll) {

        JOptionPane opcion = new JOptionPane("Obtuviste los dados "+ dados+ " queres volver a tirar?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);

        // Crear un JDialog no modal (no bloquea las ventanas) que contiene el JOptionPane
        JDialog dialogo = opcion.createDialog(this.frame, "Confirmación");
        dialogo.setModal(false); // No bloquea otras ventanas
        dialogo.setVisible(true);

        opcion.addPropertyChangeListener(evento -> {
            if (reroll){
                if (evento.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                    dialogo.dispose();

                    // Obtener la respuesta del usuario
                    int respuesta = (int) opcion.getValue();
                    if (respuesta == JOptionPane.YES_OPTION) {
                        controller.tirarDados(this.id);
                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        controller.confirmarDados();
                        this.mostrarMensaje("               Los numeros obtenidos son " + dados + "\n" +
                                "               lo que corresponde a un total de " + puntos + " puntos.\n", true);
                    }
                }
            } else {
                controller.confirmarDados();
                this.mostrarMensaje("               Los numeros obtenidos son " + dados + "\n" +
                        "               lo que corresponde a un total de " + puntos + " puntos.\n", true);
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
        this.mostrarMensaje("               " + mensaje , true);
    }

    public void finalizar(){
        this.mostrarMensaje("               \n\n            Fin de partida, pressione tirar dados para salir\n", false);
        fin = true;
        this.botonTirar.setEnabled(true);

    }
}