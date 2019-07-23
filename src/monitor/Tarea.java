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
                monitor.dispararTransicion(transicion);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName());
        }
    }
}