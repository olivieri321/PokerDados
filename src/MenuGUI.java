
import MVC.Controller;
import MVC.Model;
import MVC.VistaJugadorConsola;
import MVC.VistaJugadorGUI;
import Serializacion.Serializador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuGUI{
    private static Model model;

    private static final int LIMITE_JUGADORES = 10;
    private static final int LIMITE_RONDAS = 10;
    private static final int LIMITE_REROLLS = 1;

    public static void cargarSala(Model model, int gui){

        if (gui == 2){
            VistaJugadorGUI[] vistaJugadorGUIS = new VistaJugadorGUI[model.getJugadores().length];
            for (int i = 0; i < model.getJugadores().length; i++) {
                vistaJugadorGUIS[i] = new VistaJugadorGUI(new Controller(model), i);
                model.setObservers(vistaJugadorGUIS);
            }
        }else{
            VistaJugadorConsola[] vistaJugadorConsolas = new VistaJugadorConsola[model.getJugadores().length];
            for (int i = 0; i < model.getJugadores().length; i++) {
                vistaJugadorConsolas[i] = new VistaJugadorConsola(new Controller(model), i);
                model.setObservers(vistaJugadorConsolas);
            }
        }

        Thread ThreadJuego = new Thread(() -> {
            model.empezarJuego();  // se creara un thread que maneje el juego
        });
        ThreadJuego.start();

        System.out.println("Sala creada satisfactoriamente");


    }

    public static Model crearSala(int jugadores, int rondas,int gui, int rerolls){
        Controller controller;
        Model model = new Model(jugadores,rondas, rerolls);
        controller = new Controller(model);
        if (gui == 2){
            VistaJugadorGUI[] vistasJugador = new VistaJugadorGUI[jugadores];
            for (int i = 0; i < jugadores; i++) {
                vistasJugador[i] = new VistaJugadorGUI(controller, i);
            }
            model.setJugadores(vistasJugador);
        }
        else{
            VistaJugadorConsola[] vistasJugador = new VistaJugadorConsola[jugadores];
            for (int i = 0; i < jugadores; i++) {
                vistasJugador[i] = new VistaJugadorConsola(controller, i);
            }
            model.setJugadores(vistasJugador);
        }

        Thread ThreadJuego = new Thread(() -> {
            model.empezarJuego();  // se creara un thread que maneje el juego
        });
        ThreadJuego.start();
        return model;
    }

    // deshabilita botones para evitar problemas en la partida,
    // controla si la partida termina para que en el caso de que lo haga habilite los botones
    public static void prepararBotones(JButton botonAceptar, JButton botonCargar, JButton botonGuardar){
        botonAceptar.setEnabled(false);
        botonCargar.setEnabled(false);
        botonGuardar.setEnabled(true);
        new Thread(() -> {
            while (model.getJugando()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    break;
                }
            }
            SwingUtilities.invokeLater(() -> {
                botonCargar.setEnabled(true);
                botonAceptar.setEnabled(true);
                botonGuardar.setEnabled(false);
            });
        }).start();
    }

    public static void main(String[] args) {
        JFrame frame;
        JTextArea inputRondas;
        JTextArea inputJugadores;
        JTextArea inputRerolls;


        JButton botonAceptar;
        JButton botonGuardar;
        JButton botonCargar;

        Container panelPrincipal;

        frame = new JFrame("Poker con Dados");

        inputRondas = new JTextArea(1, 1);
        inputJugadores = new JTextArea(1, 1);
        inputRerolls = new JTextArea(1, 1);
        botonAceptar = new JButton("Jugar");
        botonCargar = new JButton("Cargar");
        botonGuardar = new JButton("Guardar");
        panelPrincipal = frame.getContentPane();

        // Tipo layout
        panelPrincipal.setLayout(new BorderLayout());

        // Tamaño ventana
        frame.setBounds(100, 100, 400, 400);

        // Panel para los JTextArea
        JPanel panelInputs = new JPanel();
        panelInputs.setLayout(new GridLayout(3, 2, 10, 10));
        panelInputs.add(new JLabel("Numero de rondas:"));
        panelInputs.add(inputRondas);
        panelInputs.add(new JLabel("Numero de jugadores:"));
        panelInputs.add(inputJugadores);
        panelInputs.add(new JLabel("Numero de tiros:"));
        panelInputs.add(inputRerolls);

        // Panel para el botón
        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new FlowLayout());
        panelAcciones.add(botonAceptar);
        panelAcciones.add(botonCargar);
        panelAcciones.add(botonGuardar);

        // Agregar elementos
        panelPrincipal.add(panelInputs, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);


        // Configuración de los JTextArea
        inputRondas.setOpaque(true);
        inputRondas.setBackground(Color.WHITE);
        inputJugadores.setOpaque(true);
        inputJugadores.setBackground(Color.WHITE);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        botonAceptar.addActionListener(e -> { // crear nueva partida
            try {
                int jugadores = Integer.parseInt(inputJugadores.getText());
                int rondas = Integer.parseInt(inputRondas.getText());
                int rerolls = Integer.parseInt(inputRerolls.getText());

                if (jugadores > LIMITE_JUGADORES || jugadores < 2) {
                    throw new NumberFormatException("El número de jugadores no puede ser mayor a " + LIMITE_JUGADORES);
                }
                if (rondas > LIMITE_RONDAS || rondas < 1) {
                    throw new NumberFormatException("El número de rondas no puede ser mayor a " + LIMITE_RONDAS);
                }
                if (rerolls > LIMITE_REROLLS || rerolls <= 0) {
                    throw new NumberFormatException("El número de tiros no puede ser mayor a " + LIMITE_REROLLS);
                }


                model = crearSala(jugadores, rondas, 2, rerolls);
                prepararBotones(botonAceptar, botonCargar, botonGuardar);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingresa numeros validos, el numero de jugadores debe ser" +
                        " menor o igual que  " + LIMITE_JUGADORES + ",el numero de rondas debe ser menor o igual que "+ LIMITE_RONDAS+ " y el numero de " +
                        "rerrolls debe ser menor o igual a " + LIMITE_REROLLS);
            }
        });

        botonGuardar.addActionListener(e -> { //guardar partida
            try {
                Serializador serializador = new Serializador(model.hashCode()+".data");
                serializador.writeOneObject(model);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingresa numeros válidos.");
            }
        });

        botonCargar.addActionListener(e -> { //cargar partida
            try {
                String input = JOptionPane.showInputDialog(frame, "Ingresa nombre del archivo:");
                Serializador serializador = new Serializador(input+".data");
                model = (Model)serializador.readFirstObject();
                cargarSala(model,2);
                prepararBotones(botonAceptar, botonCargar, botonGuardar);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingresa numeros validos.");
            }
        });


        inputRondas.setPreferredSize(new Dimension(200, 30));
        inputJugadores.setPreferredSize(new Dimension(200, 30));
        // Hacer visible el menú
        botonGuardar.setEnabled(false);
        frame.setVisible(true);
    }
}
