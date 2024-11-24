package MVC;

import clases.ObserverJugador;

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
        while (!controller.validarEmptyInputConsola(scaner.nextLine())){
            try {
                wait(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        tirarDados();
    }

    @Override
    public void updateFinTurno(String dados, int puntos , boolean reroll){
        int resultado = 0;
        int opcion = 2;
        do {
            if (resultado == -1){
                System.out.println("valor incorrecto, ingrese otro valor");
            }else {
                System.out.println("Los numeros obtenidos son: " + dados+ " " +
                        "lo que corresponde a un total de " + puntos + " puntos.\n");
                if (reroll){
                    System.out.println("1- Rerollear  \n2- Seguir");
                    opcion = scaner.nextInt();
                }
            }
            resultado = controller.eleccionReroll(opcion, this.id);
        }while (resultado == -1);

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
