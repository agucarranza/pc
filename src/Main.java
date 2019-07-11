import monitor.GestorDeMonitor;
import monitor.RdP;
import monitor.Tarea;
import monitor.Colas;
import log.Log;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.ArrayRealVector;

import java.lang.reflect.Array;

public class Main {



    public static void main(String[] args) throws InterruptedException {

        RdP red = new RdP ("./petri-nets/incidencia.csv",
                           "./petri-nets/marcado.csv",
                "./petri-nets/inhibicion.csv",
                "./petri-nets/politicas.csv");
       new Log();

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
        hilo0.start();
        hilo1.start();
        hilo2.start();
        hilo3.start();

       // Thread.sleep(5000);
//        hilo0.interrupt();
//        hilo1.interrupt();
//        hilo2.interrupt();
//        hilo3.interrupt();
//        System.exit(0);

        double d[] = {1,1,1,1};
//        while (true) {
//            if (monitor.getColas().quienesEstan().toString().equals(new ArrayRealVector(d).toString())) {
//                monitor.getColas().desencolar(0);
//                monitor.getColas().desencolar(1);
//                monitor.getColas().desencolar(2);
//                monitor.getColas().desencolar(3);
//                System.out.println(hilo0.getName()+hilo0.getState().toString());
//                System.out.println(hilo1.getName()+hilo1.getState().toString());
//                System.out.println(hilo2.getName()+hilo2.getState().toString());
//                System.out.println(hilo3.getName()+hilo3.getState().toString());

            //System.out.println(monitor.getColas().quienesEstan().toString());
//            Thread.sleep(1000);
//                }
            }
        }