package MVC;

import clases.ObserverJugador;

import java.util.InputMismatchException;
import java.util.Scanner;

public class VistaJugadorConsola implements ObserverJugador { // el observer es la vista

    private int id;
    private Controller controller;
    private Scanner scaner;

    public VistaJugadorConsola(Controller controller, int id) {
        super();
        this.controller = controller;
        this.id = id;
        scaner = new Scanner(System.in);
    }

    @Override
    public void updateTurno() {
        System.out.println("Es tu turno jugador "+ id);
        System.out.println("Apreta enter para tirar los dados\n");
        // apreta enter el jugador y el modelo tira los dados
        scaner.nextLine();
        tirarDados();
    }

    @Override
    public void updateFinTurno(String dados, int puntos , boolean reroll){
        int opcion = 0;
        System.out.println("Los numeros obtenidos son: " + dados+ " " +
                "lo que corresponde a un total de " + puntos + " puntos.\n");
        if (reroll){
            System.out.println("1- Rerollear  \n2- Seguir");
            boolean entradaIncorrecta = true;
            while (entradaIncorrecta){
                try {
                    entradaIncorrecta = false;
                    opcion = scaner.nextInt();
                }catch (InputMismatchException e){
                    System.out.println("Entrada no valida\n");
                    entradaIncorrecta = true;
                }
                if (opcion>2 || opcion< 1){
                    System.out.println("Entrada no valida\n");
                    entradaIncorrecta = true;
                }
                scaner.nextLine();
            }

            boolean[] dadosATirar = new boolean[5];
            for (int i = 0; i < dadosATirar.length; i++) {
                dadosATirar[i] = false;
            }
            if (opcion == 1){
                Integer temp = -1;
                entradaIncorrecta = true;
                while (entradaIncorrecta){
                    System.out.println("Ingresa Nro de dado que cambiar (del 1 al 6) para confirmar ingresa 0:  \n");
                    try {
                        entradaIncorrecta = false;
                        temp = scaner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Entrada no valida\n");
                        entradaIncorrecta = true;
                        temp = -1;
                    }
                    if (temp <= 6 && temp>=1){
                        if (temp == 1){
                            dadosATirar[0] = !dadosATirar[0];
                        } else if (temp == 2) {
                            dadosATirar[1] = !dadosATirar[1];
                        }else if (temp == 3) {
                            dadosATirar[2] = !dadosATirar[2];
                        }else if (temp == 4) {
                            dadosATirar[3] = !dadosATirar[3];
                        }else if (temp == 5) {
                            dadosATirar[4] = !dadosATirar[4];
                        }
                    }else {
                        System.out.println("Entrada no valida\n");
                        entradaIncorrecta = true;
                    }
                    scaner.nextLine();
                }
                tirarDados();
            }
            else{
                controller.confirmarDados();
            }
        }
        else{
            controller.confirmarDados();
        }

    }

    private void tirarDados(){
        controller.tirarDados(this.id);
    }

    public void updateStatus(String mensaje){
        System.out.println(mensaje);
    }


    public void updatePuntos(String string){
        System.out.println(string);
    }

    public void finalizar(){
        System.exit(0);
    }

}