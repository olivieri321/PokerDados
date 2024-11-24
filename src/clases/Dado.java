package clases;
import java.io.Serializable;
import java.util.Random;

public class Dado implements Serializable {
    private int valor;

    public Dado() {
        valor = 0;
    }

    public void tirarDado(){
        Random random = new Random();
        this.valor = random.nextInt(5) + 1;
    }

    public int getValor() {
        return valor;
    }

}
