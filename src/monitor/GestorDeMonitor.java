package monitor;

import log.Log;
import org.apache.commons.math3.linear.RealVector;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

public class GestorDeMonitor {

    public Semaphore mutex;
    private RdP red;
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
        mutex.acquire();

        if (mutex.availablePermits()!=0)
            throw new InterruptedException("Mutex Entrada Mal, Permiso es: "+mutex.availablePermits()+"\t"+Thread.currentThread().getName()+"\tT:"+transicion);

        k = true;
        while (k) {
            //k = true;
            k = red.disparar(transicion);
            if (k) {
                Log.log.log(Level.INFO, "DISPARE!\t Marcado: "+red.getMarcadoActual().toString().substring(20)+"\t"+Thread.currentThread().getName()+"\tT:"+transicion);
                RealVector v_sensibilizadas = red.sensibilizadas();
                RealVector v_colas = colas.quienesEstan();
                m = v_sensibilizadas.ebeMultiply(v_colas);
                if (isCero(m))
                    k = false;
                else {
                    int p = red.politica(m);
                    colas.desencolar(p);
                    mutex.acquire();
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
            } else {
                mutex.release();
                colas.encolar(transicion);

            }
        }
        mutex.release();
        if (mutex.availablePermits()!=1)
            throw new InterruptedException("Mutex Salida Mal, Permiso es: "+mutex.availablePermits()+"\t"+Thread.currentThread().getName()+"\tT:"+transicion);
    }

    /*
    Prueba de función para ver cómo hacer andar bien el mutex. Aparentemente en la función
    anterior, cuando un hilo despierta a otro, no alcanza a salir del monitor despues de liberar
    el mutex, y entra otro. Entonces quedan dos hilos adentro.
     */

    public void dispararTransicion2(int t) throws InterruptedException {
        mutex.acquire();

        while (!red.disparar(t)) {
            mutex.release();
            colas.encolar(t);
        }
        Log.log.log(Level.INFO, "DISPARE!\t Marcado: "+red.getMarcadoActual().toString().substring(20)+"\t"+Thread.currentThread().getName()+"\tT:"+t);
        RealVector v_sensibilizadas = red.sensibilizadas();
        RealVector v_colas = colas.quienesEstan();
        m = v_sensibilizadas.ebeMultiply(v_colas);
        if (!isCero(m))
            colas.desencolar(red.politica(m));



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

