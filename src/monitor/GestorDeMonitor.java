package monitor;


import log.Log;
import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Clase principal dónde se controla el acceso y se dan permisos o no para disparar transiciones.
 */
public class GestorDeMonitor {

    private Semaphore mutex;
    private RdP red;
    private Colas colas;
    private Politicas politica;
    private int in =0;

    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex = new Semaphore(1, true);
        colas = new Colas(red.getTransiciones());
        politica = new Politicas();
    }

    /**
     * Esta función trabaja con un mutex que es un semáforo. Si no se puede disparar la transición, libera el mutex y
     * encola la transición actual.
     * Si no se cumple el requisito de la venta de tiempo, se va a dormir hasta que se pueda disparar.
     * Luego de disparar, obtiene las transiciones que están en cola y, a su vez, sensibilizadas. Con ellas, va
     * a preguntar a la política cuál debe desencolar para que sea la próxima en dispararse.
     * También controla que los invariante de plaza se cumplan. Cuando terminó de realizar todas estas tareas
     * libera el mutex para que otro hilo pueda entrar.
     * @param t Transición que se va a intentar disparar.
     */

    void dispararTransicion(int t) throws InterruptedException {
        mutex.acquire();
        while (!red.disparar(t)) {
            mutex.release();
            if (red.getVentana() <= 0 && red.sensibilizadas().getEntry(t) == 0) {
                colas.encolar(t);
            } else {
                try {
                    Thread.sleep(red.getVentana());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mutex.acquire();
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            }
        }
        try {
            RealVector v_sensibilizadas = red.sensibilizadas();
            RealVector v_colas = colas.quienesEstan();
            RealVector m = v_sensibilizadas.ebeMultiply(v_colas);
            //System.out.println(t);
            assertTrue(this.red.checkPInvariant());
            if (!isCero(m)) {
                colas.desencolar(politica.cual(m));
                Log.log.log(Level.INFO, (in++) + "\tDISPARE! " + "T:" + t +
                        "\t\tMarcado: " + red.getMarcadoActual().toString().substring(20) +
                        "\t" + Thread.currentThread().getName() + "\tSens:" + v_sensibilizadas.toString() +
                        "\tColas: " + v_colas.toString() + "\tm: " + m.toString() +
                        "\tPolitica: " + politica.cual(m));
            } else
                Log.log.log(Level.INFO, (in++) + "\tDISPARE! " + "T:" + t +
                        "\t\tMarcado: " + red.getMarcadoActual().toString().substring(20) +
                        "\t" + Thread.currentThread().getName() + "\tSens:" + v_sensibilizadas.toString() +
                        "\tColas: " + v_colas.toString() + "\tm: " + m.toString());
        } catch (AssertionError e) {
            System.out.println("Msg From: " + Thread.currentThread().getName() +
                    "\n[!] ERROR: Alguna invariante no se cumple, terminando");
            System.exit(-1);
        }
        mutex.release();
    }

    /**
     Función auxiliar para comprobar si un vector es un vector nulo.
     @param vector El vector que se quiere analizar.
     @return True si es cero en todos sus componentes, si algún componente no es cero, devuelve false.
     */

    private boolean isCero(RealVector vector) {
        for (double d : vector.toArray()) {
            if (d != 0)
                return false;
        }
        return true;
    }
}

