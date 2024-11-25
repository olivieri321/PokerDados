
package clases;
import java.io.Serializable;
import java.util.Random;

public class Jugador implements Serializable {

    private Dado[] dados;

    public Jugador() {
        this.dados = new Dado[5];
        for (int i = 0; i < dados.length; i++) {
            dados[i] = new Dado();
        }
    }

    public int tirarDados() {  // simulara tirar todos los dados que tiene
        for (int i = 0; i < dados.length; i++) {
            dados[i].tirarDado();
        }
        return 0;
    }

    public int tirarDados(boolean[] dadosElejidos) {  // simulara tirar todos los dados que tiene
        for (int i = 0; i < dados.length; i++) {
            if (dadosElejidos[i]){
                dados[i].tirarDado();
            }
        }
        return 0;
    }

    public Dado[] getDados() {
        return dados;
    }

    public String getDadosString(){
        String string = "";
        for (int i = 0; i < dados.length; i++) {
            string = string + dados[i].getValor() + " ";
        }
        return string;
    }
}
