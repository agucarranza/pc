package monitor;

import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

    private RdP red;
    private Semaphore mutex;
    private boolean k;
    private RealVector m;
    private Colas colas;

    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex = new Semaphore(1, true);
        colas = new Colas(red.getTransiciones());
    }

    public void dispararTransicion(int transicion) throws InterruptedException {

        mutex.acquire();
        k = true;
        System.out.println(red.getMarcadoActual().toString());

        while (k) {
            k = red.disparar(transicion);
            if (k) {
                RealVector v_sensibilizadas = red.sensibilizadas();
                RealVector v_colas = colas.quienesEstan();
                m = v_sensibilizadas.ebeMultiply(v_colas);      //Aca se hace el and
                if (isCero(m))
                    k = false;
                else {
                    //POLITICAS
                    colas.desencolar(transicion);
                }
            } else {
                mutex.release();
                // Ver como saber si funciona el encolar.
                colas.encolar(transicion);
            }
            System.out.println(red.getMarcadoActual().toString());
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

