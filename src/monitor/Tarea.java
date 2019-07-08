package monitor;

import static java.lang.Thread.*;

public class Tarea implements Runnable {

    private GestorDeMonitor monitor;
    private int transicion;

    public Tarea(GestorDeMonitor monitor, int transicion) {
        this.monitor = monitor;
        this.transicion = transicion;
    }

    @Override
    public void run() {
        try {
            while(true){
                monitor.dispararTransicion2(transicion);
               // Thread.sleep(10);
                }
        }
        catch (InterruptedException e) {
            System.out.println(e);
           // System.out.println("Termine pero "+ currentThread().getName()+" quedo en la cola.");
        }
    }
}