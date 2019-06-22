package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
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

        while (k==true) {
         // k = red.disparar();
            if (k==true) {
                RealVector v_sensibilizadas = red.sensibilizadas();
             // RealVector v_colas = colas.quienesEstan();
                double[] d = {1, 1, 0, 0};
                RealVector v_colas = new ArrayRealVector(d);
                m = v_sensibilizadas.ebeMultiply(v_colas);      //Aca se hace el and
                System.out.println("m: "+m.toString());

                System.out.println(isCero(m));
            }
            break;
        }
    }
/*
Esta funcion revisa todo el vector y devuelve true si es cero en todos sus componentes,
si algun componente no es cero, devuelve false.
 */

public boolean isCero(RealVector vector) {
    for (double d: vector.toArray()) {
        if (d!=0)
            return false;
        }
    return true;
    }

}

