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

        while (red.disparar(0)) {
            if (k) {
                RealVector v_sensibilizadas = red.sensibilizadas();
                RealVector v_colas = colas.quienesEstan();
                m = v_sensibilizadas.ebeMultiply(v_colas);      //Aca se hace el and
                System.out.println("m: "+m.toString());
                System.out.println(isCero(m));
                if (isCero(m))
                    k = false;
                else {
                    //POLITICAS
                    colas.desencolar(transicion);
                }
            } else {
                mutex.release();
                colas.encolar(transicion);
            }
            System.out.println(red.getVectorDisparo(0).toString());
            System.out.println(red.getMarcadoActual().toString());
            break;
        }
        mutex.release();
    }
/*
Esta función revisa todo el vector y devuelve true si es cero en todos sus componentes,
si algún componente no es cero, devuelve false.
 */

    private boolean isCero(RealVector vector) {
    for (double d: vector.toArray()) {
        if (d!=0)
            return false;
        }
    return true;
    }

}

