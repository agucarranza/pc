package monitor;

import java.util.concurrent.Semaphore;

//import archivos.Matriz;

public class Colas {

    private int TRANS;
    private Semaphore[] colas;

    public Colas(int TRANS) {
        this.TRANS = TRANS;
        colas = new Semaphore[TRANS];
        for (int i = 0; i < TRANS; i++) {
            colas[i] = new Semaphore(0, true);
        }
    }

    public void adquirirCola(int t) {
        try {
            colas[t].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void liberarHilo(int t) {
        colas[t].release();
    }

    public Matriz quienesEstan() {
        Matriz enCola = new Matriz(1, TRANS);

        for (int i = 0; i < TRANS; i++) {
            if (colas[i].hasQueuedThreads()) {
                enCola.setValor(0, i, 1);
            } else {
                enCola.setValor(0, i, 0);
            }
        }
        return enCola;
    }

}
