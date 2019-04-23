package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

    private RdP red;
    private Semaphore mutex;
    private boolean k;


    public GestorDeMonitor(RdP red) {

        this.red = red;
        mutex = new Semaphore(1, true);
    }

    public void dispararTransicion(int transicion) throws InterruptedException {


        mutex.acquire();
        k = true;
        //red.disparar();

        RealVector vector;
        vector = red.sensibilizadas();

        double[] d = {1, 1, 0, 0};
        RealVector vector1 = new ArrayRealVector(d);
        System.out.println(vector.toString());
        System.out.println(vector1.toString());
        System.out.println(vector.ebeMultiply(vector1).toString()); // Funcion que uso para la operacion AND

    }




}
