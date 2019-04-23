import monitor.GestorDeMonitor;
import monitor.RdP;

public class Main {

    public static void main(String[] args) {

        RdP red = new RdP("./petri-nets/incidencia.csv",
                "./petri-nets/marcado.csv",
                "./petri-nets/inhibicion.csv");

        GestorDeMonitor monitor = new GestorDeMonitor(red);

        
        try {
            monitor.dispararTransicion(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
