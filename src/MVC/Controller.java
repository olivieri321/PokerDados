package MVC;

public class Controller {
    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void notificarAbandono(int id){
        try {
            model.abandono(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void tirarDados(int id){
        try {
            model.tirarDados(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void tirarDados(int id, boolean[] dadosElejidos){
        try {
            model.tirarDados(id, dadosElejidos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void confirmarDados(){
        try {
            model.confirmarDados();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
