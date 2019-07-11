package monitor;

import log.Log;
import org.apache.commons.math3.linear.RealVector;

import javax.swing.plaf.TableHeaderUI;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static java.lang.Thread.sleep;

public class GestorDeMonitor {

    public Semaphore mutex;
    public Lock mutex3;
    private RdP red;
    private boolean k;
    private RealVector m;
    private Colas colas;
    private int i =0;

    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex3 = new ReentrantLock();
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
                Log.log.log(Level.INFO, (i++)+"DISPARE!\t Marcado: "+red.getMarcadoActual().toString().substring(20)+"\t"+Thread.currentThread().getName()+"\tT:"+transicion);
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
        if (mutex.availablePermits()!=1)
            return;
        mutex.acquire();
        if (mutex.availablePermits()!=0)
            throw new InterruptedException("Mutex Entrada Mal, Permiso es: "+mutex.availablePermits()+"\t"+Thread.currentThread().getName()+"\tT:"+t);


        while (!red.disparar(t)) {
            mutex.release();
            while (mutex.availablePermits()!=1)
                sleep(1);

            colas.encolar(t);

            if (mutex.availablePermits()!=1)
                return;
            mutex.acquire();
        }
        Log.log.log(Level.INFO, "DISPARE!\t Marcado: "+red.getMarcadoActual().toString().substring(20)+"\t"+Thread.currentThread().getName()+"\tT:"+t);
        RealVector v_sensibilizadas = red.sensibilizadas();
        RealVector v_colas = colas.quienesEstan();
        m = v_sensibilizadas.ebeMultiply(v_colas);
        if (!isCero(m)) {
            colas.desencolar(red.politica(m));
            sleep(10);
        }
        mutex.release();
        while (mutex.availablePermits()!=1)
            sleep(1);
        if (mutex.availablePermits()!=1)
            throw new InterruptedException("Mutex Salida Mal, Permiso es: "+mutex.availablePermits()+"\t"+Thread.currentThread().getName()+"\tT:"+t);
    }

    public void dispararTransicion3 (int t) throws InterruptedException {
        mutex3.lock();

        try {
            while (!red.disparar(t)) {
                mutex3.unlock();
                colas.encolar(t);
                mutex3.lock();
//                if (red.sensibilizadas().getEntry(t)==0) //aca el problema cuando lo despiertan, no está en la cola y ya no está sensibilizado. Entonces con la matriz m nunca lo va a despertar nadie.
//                        continue;
            }
            RealVector v_sensibilizadas = red.sensibilizadas();
            RealVector v_colas = colas.quienesEstan();
            m = v_sensibilizadas.ebeMultiply(v_colas);
            Log.log.log(Level.INFO, (i++)+"\tDISPARE!\t Marcado: " + red.getMarcadoActual().toString().substring(20) + "\t" + Thread.currentThread().getName() +"\tSensi:"+v_sensibilizadas.toString()+ "\tT:" + t+"\tColas: "+colas.quienesEstan().toString()+"\tm: "+m.toString()+"\tPolitica: "+red.politica(m));
            if (!isCero(m)) {
                colas.desencolar(red.politica(m));
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            }else
                if ((red.sensibilizadas().getEntry(t)==0)) {//&&(colas.quienesEstan().getEntry(t)==0)) {
                    //Log.log.log(Level.INFO,red.sensibilizadas().toString() + colas.quienesEstan());
                    return;//Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }


        }
        catch(Exception e) {
            e.printStackTrace();

        }
        finally {
            mutex3.unlock();
        }
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

