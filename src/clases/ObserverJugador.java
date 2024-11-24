package clases;

public interface ObserverJugador {
    void updatePuntos(String puntaje);   // visualizacion de puntos totales de jugador (fin ronda)
    void updateTurno();                  // pedir tirar dados
    void updateFinTurno(String dados, int puntos, boolean reroll); // mostrar puntos obtenidos y dar posibilidad de reroll
    void updateStatus(String mensaje);   // mostrar mensaje
    void finalizar();                    // notificar fin de juego
}
