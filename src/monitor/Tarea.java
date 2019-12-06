package monitor;

/**
 * Tarea ejecuta en un mismo thread todas las transiciones que se le pasan por parámetro. Esto ocurre en un
 * bucle infinito y sólo se interrumpe si se detiene la ejecución del programa de manera externa.
 */
public class Tarea implements Runnable {

    private GestorDeMonitor monitor;
    private int[] transicion;

    public Tarea(GestorDeMonitor monitor, int [] transicion) {
        this.monitor = monitor;
        this.transicion = transicion;
    }

    /**
     * Función implementada a partir de la interface Runnable para poder trabajar con multi-thread y cuya
     * función es disparar infinitamente las transiciones asignadas.
     */
    @Override
    public void run() {
        try {
            while (true) {
                for (int value : transicion) {
                    monitor.dispararTransicion(value);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
