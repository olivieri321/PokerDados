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

    public int eleccionReroll(int opcion, int id){
        if (opcion == 1){
            model.tirarDados(id);
            return 0;
        }
        else if (opcion == 2){
            this.confirmarDados();
            return 0;
        }else{
            return -1;
        }
    }

    public void tirarDados(int id){
        try {
            model.tirarDados(id);
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

    public boolean validarEmptyInputConsola(String input){
        if (!input.isEmpty()) {
            System.out.println("Solo debes presionar Enter\n");
            return false;
        }
        return true;
    }
}
