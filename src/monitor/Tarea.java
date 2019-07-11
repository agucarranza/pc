package monitor;


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
            for (int i =0;i<10000;i++) {
                monitor.dispararTransicion3(transicion);
                }
          //  System.out.println(Thread.currentThread().getName()+" se murio");
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}