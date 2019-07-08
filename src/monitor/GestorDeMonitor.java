package monitor;

import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

    private RdP red;
    public Semaphore mutex;
    private boolean k;
    private RealVector m;
    private Colas colas;

    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex = new Semaphore(1, true);
        colas = new Colas(red.getTransiciones());
    }

    /*
     * Poner comentario de cómo funciona la función Disparar transición.
     *
     */

    public void dispararTransicion(int transicion) throws InterruptedException {
        //
        mutex.acquire();
        k = true;
        while (k) {
            System.out.println("Voy a disparar la transicion:" + transicion+Thread.currentThread().getName());
            System.out.println("El marcado es: "+red.getMarcadoActual().toString()+Thread.currentThread().getName());
            k = red.disparar(transicion);
            System.out.println("DISPARE! El nuevo marcado es: "+red.getMarcadoActual().toString()+Thread.currentThread().getName());
            if (k) {
                RealVector v_sensibilizadas = red.sensibilizadas();
                System.out.println("sensibilizadas:\t"+v_sensibilizadas.toString()+Thread.currentThread().getName());
                RealVector v_colas = colas.quienesEstan();
                m = v_sensibilizadas.ebeMultiply(v_colas);
                if (isCero(m))
                    k = false;
                else {
                    int p = red.politica(v_sensibilizadas);
                    colas.desencolar(p);
                   // transicion = p;
                }
            } else {
                mutex.release();
                colas.encolar(transicion);
            }
        }
        mutex.release();
    }
/*
Esta función revisa el vector entero y devuelve true si es cero en todos sus componentes,
si algún componente no es cero, devuelve false.
 */

    private boolean isCero(RealVector vector) {
    for (double d: vector.toArray()) {
        if (d!=0)
            return false;
        }
    return true;
    }

    public Colas getColas() {
        return colas;
    }

}

