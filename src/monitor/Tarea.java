package monitor;
public class Tarea implements Runnable {

    private GestorDeMonitor monitor;
    private int[] transicion;

    public Tarea(GestorDeMonitor monitor, int [] transicion) {
        this.monitor = monitor;
        this.transicion = transicion;
    }

    @Override
    public void run() {
        try {
        	while (true) {
        	
            for (int i =0;i<transicion.length;i++) {
                monitor.dispararTransicion(transicion[i]);
                }
          //  System.out.println(Thread.currentThread().getName()+" se murio");
            
        	 }
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
