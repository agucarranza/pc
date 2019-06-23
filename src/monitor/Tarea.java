package monitor;

public class Tarea implements Runnable {

    private String nombre;
    private GestorDeMonitor monitor;

    public Tarea(GestorDeMonitor monitor) {
        this.monitor = monitor;

    }

    @Override
    public void run() {
        try {
            nombre = Thread.currentThread().getName();
            System.out.println("Soy el thread: " + nombre);
            monitor.dispararTransicion(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getNombre() {
        return nombre;
    }
}
