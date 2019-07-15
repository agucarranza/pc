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
            //for (int i=0;i<1;i++) {
            while(true) {
                monitor.dispararTransicion2(transicion);
            }
        }
        catch (InterruptedException e) {
            System.out.println(e);
            System.out.println(Thread.currentThread().getName());
        }
    }
}