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
            while(true)
                monitor.dispararTransicion(transicion);
        }
        catch (InterruptedException e) {
            System.out.println("Termine pero "+Thread.currentThread().getName()+" quedo en la cola.");
        }
    }
}