import monitor.GestorDeMonitor;
import monitor.RdP;
import monitor.Tarea;

public class Main {

    public static void main(String[] args) {

        RdP red = new RdP ("./petri-nets/incidencia.csv",
                           "./petri-nets/marcado.csv",
                           "./petri-nets/inhibicion.csv");

        GestorDeMonitor monitor = new GestorDeMonitor(red);
        Tarea tarea0 = new Tarea(monitor);
        Tarea tarea1 = new Tarea(monitor);

        // Ver como asignar las transiciones a cada tarea.

        Thread hilo0 = new Thread(tarea0);
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread();
        Thread hilo3 = new Thread();
        Thread hilo4 = new Thread();


        hilo0.start();
        hilo1.start();
        // System.out.println(monitor.getColas().quienesEstan().toString());
    }
}
