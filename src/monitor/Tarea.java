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
            Thread.sleep(1000);
            monitor.dispararTransicion(transicion);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}