package MVC;

import clases.Dado;
import clases.Jugador;
import clases.ObserverJugador;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Model implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static Model model;

    private transient ObserverJugador[] observers; // vistas de jugadores
    private Jugador[] jugadores;
    private Integer[] puntos;                     // puntos totales de ronda
    private Integer[] rondasGanadas;              // contiene las rondas que gano cada jugador

    private int rondas;                           // rondas maximas ( sin contar desempates )
    private int rondaActual;
    private int turnoActual;                      // id de jugador al que le toque jugar
    private int rerolls;
    private int maxRerolls;

    private boolean confirmado;
    private boolean jugando;

    public Model( int numJugadores, int rondas, int rerolls) {
        observers = new ObserverJugador[numJugadores];
        jugadores = new Jugador[numJugadores];
        puntos = new Integer[numJugadores];
        rondasGanadas = new Integer[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            rondasGanadas[i] = 0;
        }
        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = 0;
        }
        this.rondas = rondas;
        this.maxRerolls = rerolls;
        this.rerolls = 0;
        rondaActual = 0;
        turnoActual = 0;
        model = this;
        jugando = false;
    }

    public void setObservers(ObserverJugador[] observersJugador){   // se debera llamar luego de la construccion del modelo
        this.observers = observersJugador;
    }

    public void setJugadores(ObserverJugador[] observersJugador){  // genera los jugadores
        if (observersJugador == null || observersJugador.length == 0) {
            throw new IllegalArgumentException("El array de observadores no puede estar vacío o ser null.");
        }

        this.jugadores = new Jugador[observersJugador.length];
        this.observers = new ObserverJugador[observersJugador.length];

        for (int i = 0; i < observersJugador.length; i++) {
            this.jugadores[i] = new Jugador();
            this.observers[i] = observersJugador[i];
        }
    }

    private int verificarPokerReal(Dado[] dados){       // cinco dados iguales
        if (dados[0].getValor() == dados[1].getValor() && dados[0].getValor() == dados[2].getValor()
                && dados[3].getValor() == dados[0].getValor() && dados[4].getValor() == dados[0].getValor()){
            if (dados[0].getValor() == 1){
                return 10;
            }
            return dados[0].getValor();
        }
        return -1;
    }

    private int verificarPokerCuadruple(Dado[] dados){ // cuatro dados iguales
        if (dados[0].getValor() == dados[1].getValor() && dados[0].getValor() == dados[2].getValor()
                && dados[3].getValor() == dados[0].getValor()){
            if (dados[0].getValor() == 1){
                return 10;
            }
            return dados[0].getValor();
        }
        if (dados[0].getValor() == dados[1].getValor() && dados[0].getValor() == dados[2].getValor()
                && dados[4].getValor() == dados[0].getValor()){
            if (dados[0].getValor() == 1){
                return 10;
            }
            return dados[0].getValor();
        }
        if (dados[0].getValor() == dados[1].getValor() && dados[0].getValor() == dados[3].getValor()
                && dados[4].getValor() == dados[0].getValor()){
            if (dados[0].getValor() == 1){
                return 10;
            }
            return dados[0].getValor();
        }
        if (dados[0].getValor() == dados[2].getValor() && dados[0].getValor() == dados[3].getValor()
                && dados[4].getValor() == dados[0].getValor()){
            if (dados[0].getValor() == 1){
                return 10;
            }
            return dados[0].getValor();
        }
        if (dados[1].getValor() == dados[2].getValor() && dados[1].getValor() == dados[3].getValor()
                && dados[4].getValor() == dados[1].getValor()){
            if (dados[1].getValor() == 1){
                return 10;
            }
            return dados[1].getValor();
        }
        return -1;
    }

    private int verificarEscaleraMenor(Dado[] dados){  // verificar si dados[] tiene 1,2,3,4,5
        boolean dado1 = false;
        boolean dado2 = false;
        boolean dado3 = false;
        boolean dado4 = false;
        boolean dado5 = false;
        for (int i = 0; i < dados.length; i++){
            int valor = dados[i].getValor();
            if (valor == 1){
                dado1 = true;
            }else if (valor == 2) {
                dado2 = true;
            }else if (valor == 3) {
                dado3 = true;
            }else if (valor == 4) {
                dado4 = true;
            }else if (valor == 5) {
                dado5 = true;
            }
        }
        if (dado1 && dado2 && dado3 && dado4 && dado5){
            return 5;
        }
        return -1;
    }

    private int verificarEscaleraMayor(Dado[] dados){ // verificar si dados[] tiene 2,3,4,5,6
        boolean dado2 = false;
        boolean dado3 = false;
        boolean dado4 = false;
        boolean dado5 = false;
        boolean dado6 = false;
        for (int i = 0; i < dados.length; i++){
            int valor = dados[i].getValor();
            if (valor == 6){
                dado6 = true;
            }else if (valor == 2) {
                dado2 = true;
            }else if (valor == 3) {
                dado3 = true;
            }else if (valor == 4) {
                dado4 = true;
            }else if (valor == 5) {
                dado5 = true;
            }
        }
        if (dado6 && dado2 && dado3 && dado4 && dado5){
            return 6;
        }
        return -1;
    }

    private int verificarTriple(Dado[] dados){ // frecuencia de dado N = 3
        int[] frecuencias = new int[13];
        for (int i = 0; i < dados.length; i++){
            int valor = dados[i].getValor();
            frecuencias[valor]++;
        }
        for (int i = 0; i < frecuencias.length; i++) {
            if (frecuencias[i] == 3 ) {
                if ( i == 1){
                    return 10;
                }
                return i;
            }
        }
        return -1;
    }

    private int verificarPar(Dado[] dados, int ignorar){  // frecuencia de dado N = 2
        int[] frecuencias = new int[13];
        for (int i = 0; i < dados.length; i++){
            int valor = dados[i].getValor();
            frecuencias[valor]++;
        }
        for (int i = 0; i < frecuencias.length; i++) {
            if (i!=ignorar && frecuencias[i] == 2 ) {
                if (i == 1){
                    return 10;
                }
                return i;
            }
        }
        return -1;
    }

    private int verificarDoblePar(Dado[] dados){  // frecuencia de dado X = 2 e Y = 2
        int cond1 = verificarPar(dados, -1);
        int cond2 = verificarPar(dados, cond1);
        if (cond1>cond2){
            return cond1;               //
        }else return cond2;             // devuelve valor del mayor par
    }

    private int verificarFull(Dado[] dados) { // tres iguales y un par igual
        int[] frecuencias = new int[13];
        int trio = -1;
        for (int i = 0; i < dados.length; i++){
            int valor = dados[i].getValor();
            frecuencias[valor]++;
        }

        boolean tieneTrio = false;
        boolean tienePar = false;

        for (int i = 0; i < frecuencias.length; i++) {
            if (frecuencias[i] == 3) {
                tieneTrio = true;
                trio = i;
            } else if (frecuencias[i] == 2) {
                tienePar = true;
            }
        }

        if (tieneTrio && tienePar){
            if (trio == 1){
                return 10;
            }
            return trio;                    //  devuelve el valor del triple igual
        }
        return -1;
    }

    private int contarPuntos(Jugador jugador){
        Dado[] dados = jugador.getDados();
        int pokerReal = verificarPokerReal(dados);
        int pokerCuadruple = verificarPokerCuadruple(dados);
        int full = verificarFull(dados);
        int escaleraMayor = verificarEscaleraMayor(dados);
        int escaleraMenor = verificarEscaleraMenor(dados);
        int triple = verificarTriple(dados);
        int doblePar = verificarDoblePar(dados);
        int par = verificarPar(dados, -1);

        if (pokerReal > -1){
            return 700 + pokerReal;
        } else if (pokerCuadruple > -1) {
            return 600 + pokerCuadruple;
        } else if (full > -1) {
            return 500 + full;
        } else if (escaleraMayor > -1) {
            return 400 + escaleraMayor;
        } else if (escaleraMenor> -1) {
            return 300 + escaleraMenor;
        } else if (triple > -1) {
            return 200 + triple;
        } else if (doblePar > -1) {
            return 100 + doblePar;
        } else if (par > -1) {
            return par;
        }
        return 0;
    }

    // logica de interaccion con usuarios


    public void tirarDados(int jugador){
        jugadores[jugador].tirarDados();
        puntos[jugador] = contarPuntos(jugadores[jugador]);
        rerolls++;
        notificarFinTurnoJugador(observers[jugador], jugador, rerolls < maxRerolls);
    }

    public void confirmarDados(){
        confirmado = true;
    }

    private void notificarTirarDados(ObserverJugador observerJugador){
        observerJugador.updateTurno();
    }

    private void notificarFinTurnoJugador(ObserverJugador jugador, int numjugador , boolean reroll){
        jugador.updateFinTurno(jugadores[numjugador].getDadosString(), puntos[numjugador], reroll);
    }

    public void empezarJuego(){
        jugando = true;
        while (rondaActual != rondas){
            boolean notificar = true;
            while (turnoActual < jugadores.length){
                if (notificar){
                    notificarTirarDados(observers[turnoActual]);
                    notificar = false;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (this.confirmado){
                    turnoActual++;
                    notificar = true;
                    confirmado = false;
                    rerolls = 0;
                }
            }

            int[] array = new int[puntos.length];
            for (int i = 0; i < puntos.length; i++) {
                array[i] = puntos[i];
            }

            String puntajes = arrayIntaString(array, true);
            for (int i = 0; i < jugadores.length; i++) {
                observers[i].updatePuntos(puntajes);
            }
            int arrayCandidatos[] = new int[jugadores.length];
            for (int i = 0; i < arrayCandidatos.length; i++) {
                arrayCandidatos[i] = -1;                // arrayCandidatos[n] = -1 -> no existe candidato
            }
            int candidatosRonda = 0;
            int mayorPuntaje = 0;
            for (int i = 0; i < jugadores.length; i++) {
                if (puntos[i]>mayorPuntaje){
                    mayorPuntaje = puntos[i];
                    arrayCandidatos[0] = i;
                    candidatosRonda = 1;
                } else if (puntos[i] == mayorPuntaje){
                    arrayCandidatos[candidatosRonda] = i;
                    candidatosRonda++;
                }
            }
            int candidatos = 0;
            for (int i = 0; i < arrayCandidatos.length; i++) {
                if (arrayCandidatos[i] != -1) candidatos++;
            }

            if (candidatos>1){
                desempate(arrayCandidatos);
            }else{
                rondasGanadas[arrayCandidatos[0]]++;
                notificarObservers("El jugador "+ arrayCandidatos[0] + " gano la ronda\n");
            }
            turnoActual = 0;
            rondaActual++;

            if (rondaActual == rondas){
                int max = 0;
                int contador = 0;
                for (int j = 0; j < puntos.length; j++) {
                    if (max < puntos[j]){
                        max = puntos[j];
                        contador = 1;
                    }
                    else if(max == puntos[j]){
                        contador++;
                    }
                }
                if (contador > 1) {
                    rondas++;
                    notificarObservers("dos o mas jugadores empataron, se agregara una ronda para desempatar \n");
                }
            }

        }
        rondaActual = 0;
        String mensajeGanador = ganador();
        /*System.out.println("La partida ha terminado, "+  mensajeGanador);*/
        for (int i = 0; i < observers.length; i++) {
            observers[i].updateStatus(mensajeGanador);
            observers[i].finalizar();
        }
        jugando = false;
    }

    private void notificarObservers(String mensaje){
        for (int i = 0; i < observers.length; i++) {
            observers[i].updateStatus(mensaje);
        }
    }

    private void desempate(int[] jugadores){  // el array jugadores que recibe es un array de id de jugadores
        notificarObservers("Los Jugadores" + arrayIntaString(jugadores, false) + " empataron, las siguientes rondas seran" +
                " de desempate. \n");
        // almaceno valores originales para poder usar los array para el desempate
        Integer[] puntosTemp = this.puntos;
        Integer[] rondasGanadasTemp = this.rondasGanadas;
        //
        int idGanador = -1;
        while (idGanador == -1){
            for (int i = 0; i < puntos.length; i++) {
                    puntos[i] = 0;
                }
            int turno = 0;

            boolean notificar = true;
            confirmado = false;
            int rerolls = 0;
            while (turno<jugadores.length){
                if (jugadores[turno]!=-1){
                    if (notificar){
                        notificarTirarDados(observers[jugadores[turno]]);
                        notificar = false;
                        rerolls++;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (confirmado || rerolls == maxRerolls){
                        turno++;
                        confirmado = false;
                        notificar = true;
                        rerolls = 0;
                    }

                }
            }

            idGanador = determinarGanadorDesempate(jugadores);

        }
        this.puntos = puntosTemp;
        this.rondasGanadas = rondasGanadasTemp;
        this.rondasGanadas[idGanador]++;
        notificarObservers("El jugador que gano el desempate es el " + idGanador + "\n");
    }

    private int determinarGanadorDesempate(int[] jugadores) {
        int mayorPuntaje = 0;
        int ganadorID = -1;
        int empateContador = 0;

        for (int jugador : jugadores) {
            if (jugador != -1) {
                int puntosJugador = this.puntos[jugador];
                if (puntosJugador > mayorPuntaje) {
                    mayorPuntaje = puntosJugador;
                    ganadorID = jugador;
                    empateContador = 1; // nuevo posible ganador, reinicia el conteo de empates
                } else if (puntosJugador == mayorPuntaje) {
                    empateContador++;
                }
            }
        }

        if (empateContador > 1){
            return -1; // retorna -1 si dos o mas jugadores empataron
        }
        return ganadorID;
    }



    private String ganador(){ // devuelve mensaje del ganador
        int mayor = 0;
        int temp = 0;
        int jug = -1;
        int contadorempate = 0;
        for (int i = 0; i < jugadores.length; i++) {
            temp = this.rondasGanadas[i];
            if (mayor<temp) {
                mayor = temp;
                jug = i;
            } else if (mayor == temp) {
                contadorempate++;
            }

        }
        if (jug == -1 || contadorempate==jugadores.length){
            return  "ningun jugador gano"; //empate
        }
        return  "El ganador es Jugador "+jug+" \n                   con "+mayor+" rondas ganadas \n";
    }


    public void abandono(int id){ // en caso de abandono, notifica observers y termina juego
        for (int i = 0; i < observers.length; i++) {
            if (i!=id){
                observers[i].updateStatus("Jugador "+id+" abandono la partida");
                observers[i].finalizar();
            }
        }
        this.jugando = false;
    }

    /* Varios */

    private String arrayIntaString(int[] array, boolean detallado){
        String texto = "";
        if (detallado){
            for (int i = 0; i < array.length; i++) {
                if (array[i] != -1){
                    texto =texto + ", Jugador " + i + ":" + " " + array[i];
                }
            }
            return texto;
        }
        else {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != -1){
                    texto = texto + " " + array[i];
                }
            }
            return texto;
        }
    }

    /* Serializacion */

    public boolean equals(Object object){
        if (this==object) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(observers), Arrays.hashCode(jugadores), Arrays.hashCode(puntos), rondas, rondaActual);
    }

    @Override
    public String toString() {
        return "MVC.Model{" +
                "observers=" + Arrays.toString(observers) +
                ", jugadores=" + Arrays.toString(jugadores) +
                ", puntos=" + Arrays.toString(puntos) +
                ", rondas=" + rondas +
                ", rondaActual=" + rondaActual +
                '}';
    }


    /* Getters */

    public static Model getInstance(){
        return model;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public boolean getJugando(){
        return jugando;
    }
}
