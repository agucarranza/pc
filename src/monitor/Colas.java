package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import java.util.concurrent.Semaphore;

class Colas {

    private Semaphore[] arregloSemaphores;

    Colas(int tamano) {
        arregloSemaphores = new Semaphore[tamano];
        for (int i = 0; i < tamano; i++) {
            arregloSemaphores[i] = new Semaphore(0, true);
        }
    }

    void desencolar(int i) {
        if (arregloSemaphores[i] != null) {
            arregloSemaphores[i].release();
        }
    }

    RealVector quienesEstan() {
        RealVector Vc = new ArrayRealVector(arregloSemaphores.length);
        Vc.set(0);
        for (int i = 0; i < arregloSemaphores.length; i++) {
            if (arregloSemaphores[i].getQueueLength() != 0)
                Vc.setEntry(i,1);
        }
        return Vc;
    }

    void encolar(int i) throws InterruptedException {
        if (arregloSemaphores[i] != null) {
            arregloSemaphores[i].acquire();
        }
    }
}