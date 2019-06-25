import monitor.GestorDeMonitor;
import monitor.RdP;
import monitor.Tarea;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        RdP red = new RdP ("./petri-nets/incidencia.csv",
                           "./petri-nets/marcado.csv",
                "./petri-nets/inhibicion.csv",
                "./petri-nets/politicas.csv");

        GestorDeMonitor monitor = new GestorDeMonitor(red);
        Tarea tarea0 = new Tarea(monitor,0);
        Tarea tarea1 = new Tarea(monitor,1);
        Tarea tarea2 = new Tarea(monitor,2);
        Tarea tarea3 = new Tarea(monitor,3);


        // Ver como asignar las transiciones a cada tarea.

        Thread hilo0 = new Thread(tarea0);
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread(tarea2);
        Thread hilo3 = new Thread(tarea3);


//        hilo0.start();
//        hilo1.start();
//        hilo2.start();
//        hilo3.start();
        double[] da = {1, 0, 1, 1};
        RealVector d = new ArrayRealVector(da);

        System.out.println(red.politica(d));
//        while (true) {
//            System.out.println("Cola:\t\t\t"+monitor.getColas().quienesEstan().toString()+Thread.currentThread().getName());
//            Thread.sleep(1000);
//        }

    }
}
