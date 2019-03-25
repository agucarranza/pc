package monitor;

import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

    private RdP red;
    private Semaphore mutex;


    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex = new Semaphore(1, true);
    }

    public void dispararTransicion(int transicion) throws InterruptedException {
        mutex.acquire();

    }




}
