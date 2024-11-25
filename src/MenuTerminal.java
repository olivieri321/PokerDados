import MVC.Controller;
import MVC.Model;
import MVC.VistaJugadorConsola;
import MVC.VistaJugadorGUI;
import Serializacion.Serializador;
import java.util.InputMismatchException;

import java.util.Scanner;

public class MenuTerminal {

    private static final int LIMITE_JUGADORES = 10;
    private static final int LIMITE_RONDAS = 10;
    private static final int LIMITE_REROLLS = 2;

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
        Model model = new Model(jugadores,rondas,rerolls);
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
        Serializador serializador = new Serializador(model.hashCode() + ".dat");

        Thread ThreadJuego = new Thread(() -> {
            model.empezarJuego();  // se creara un thread que maneje el juego
        });
        ThreadJuego.start();

        System.out.println("Sala creada satisfactoriamente");
        return model;
    }


    public static void main(String[] args) {
        int input1 = -1;
        int input2 = -1;
        int input3 = -1;
        int input4 = -1;
        int temp = -1;

        boolean jugando = false;
        Model model = new Model(0,0,0);

        String tempString;
        Serializador serializador;


        Scanner sc = new Scanner(System.in);
        while (temp!=3) {
            if (jugando){       // mientras modelo este en partida, no producir salidas ni esperar entradas
                temp = 0;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!model.getJugando()){
                    jugando = false;
                }
            }else {
                boolean valorIncorrecto = true;
                while (valorIncorrecto){
                    try {
                        System.out.println("Poker con dados, indique accion:\n" +
                                "1- Empezar Partida\n" + "2- Cargar Partida\n" + "3- Salir\n");
                        temp = sc.nextInt();
                        sc.nextLine();
                        valorIncorrecto = false;
                    } catch (InputMismatchException e) {
                        System.out.println("Valor ingresado invalido, intente devuelta\n");
                        sc.nextLine();
                    }

                }
                if (temp == 1) {
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("Escriba la cantidad de jugadores que participaran");
                            input1 = sc.nextInt();
                            sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                        if ( input1 > LIMITE_JUGADORES ||  input1 < 0){
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            valorIncorrecto = true;
                        }
                    }
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("Escriba la cantidad de rondas que se jugaran");
                            input2 = sc.nextInt();
                            sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                        if ( input2 > LIMITE_RONDAS ||  input2 < 0){
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            valorIncorrecto = true;
                        }
                    }
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("\nSeleccione 1 para jugar con consola, 2 para jugar con GUI");
                            input3 = sc.nextInt();
                            sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                        if ( input3 > 2 || input3 < 1 ){
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            valorIncorrecto = true;
                        }
                    }
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("\nIngrese cantidad de rerolls");
                            input4 = sc.nextInt();
                            sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                        if ( input4 > LIMITE_REROLLS || input4 < 0){
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            valorIncorrecto = true;
                        }
                    }

                }
                model = crearSala(input1, input2, input3, input4);
                jugando = true;
                /*
                System.out.println("\nJuego ejecutandose\n\n para guardar partida ingrese 1, para salir ingrese 2\n");
                temp = 0;
                serializador = new Serializador(model.hashCode() + ".data");
                while (temp != 2) {
                    temp = sc.nextInt();
                    if (temp == 1) {
                        serializador.writeOneObject(model);
                        System.out.println("Partida guardada");
                    }
                }*/
                if (temp == 2) {
                    tempString = "";
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("Escriba el nombre del archivo\n");
                            tempString = sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                    }

                    serializador = new Serializador(tempString + ".data");
                    model = (Model) serializador.readFirstObject();
                    valorIncorrecto = true;
                    while (valorIncorrecto){
                        try {
                            System.out.println("\nSeleccione 1 para jugar con consola, 2 para jugar con GUI");
                            input3 = sc.nextInt();
                            sc.nextLine();
                            valorIncorrecto = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            sc.nextLine();
                        }
                        if ( input3 > 2 || input3 < 1 ){
                            System.out.println("Valor ingresado invalido, intente devuelta\n");
                            valorIncorrecto = true;
                        }
                    }
                    cargarSala(model, input3);

                }
            }


        }
    }
}
