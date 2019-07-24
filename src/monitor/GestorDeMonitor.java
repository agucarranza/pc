package monitor;

import log.Log;
import org.apache.commons.math3.linear.RealVector;
import org.jetbrains.annotations.NotNull;


import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class GestorDeMonitor {

    private Semaphore mutex;
    private RdP red;
    private Politicas politica;
    private Colas colas;
    private int in =0;

    public GestorDeMonitor(@NotNull RdP red) {
        this.red = red;
        mutex = new Semaphore(1, true);
        colas = new Colas(red.getTransiciones());
        politica = new Politicas();
    }

    /**
    Prueba de función para ver cómo hacer andar bien el mutex. Aparentemente en la función
    anterior, cuando un hilo despierta a otro, no alcanza a salir del monitor después de liberar
    el mutex, y entra otro. Entonces quedan dos hilos adentro.
     @param t Transición para disparar.
     */

    public void dispararTransicion(int t) throws InterruptedException {

    	mutex.acquire();
    	while (!red.disparar(t)) {
            mutex.release();
            colas.encolar(t);
            mutex.acquire();
        }
    	if (t!=0){
    		 try{
    	  RealVector v_sensibilizadas = red.sensibilizadas();
          RealVector v_colas = colas.quienesEstan();
          RealVector m = v_sensibilizadas.ebeMultiply(v_colas);
          Log.log.log(Level.INFO, (in++)+"\tDISPARE!\t Marcado: " + red.getMarcadoActual().toString().substring(20) + "\t" + Thread.currentThread().getName() +"\tSensi:"+v_sensibilizadas.toString()+ "\tT:" + t+"\tColas: "+v_colas.toString()+"\tm: "+ m.toString()+"\tPolitica: "+red.politica(m));
         // System.out.println(t);
          assertTrue(this.red.checkPInvariant());
          if (!isCero(m)) {
              colas.desencolar(red.politica(m));
          }

    	 }catch(AssertionError e){
             System.out.println
             ("Msg From: "+Thread.currentThread().getName()+
              "\n[!] ERROR: Algun invariante no se cumple, terminando");
         System.exit(-1);
    	 }
    	}

    	else {
            Log.log.log(Level.INFO, (in++)+"\tDISPARE!\t : " + "\t" + Thread.currentThread().getName()  + t);
    void dispararTransicion(int t) throws InterruptedException  {
        mutex.acquire();
        while (!red.disparar(t)) {
            mutex.release();
            if (red.getVentana() > 0 && red.sensibilizadas().getEntry(t) == 1) //revisar!!
                try {
                    Thread.sleep(red.getVentana());
                    mutex.acquire();
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            else
                colas.encolar(t);
        }
        RealVector v_sensibilizadas = red.sensibilizadas();
        RealVector v_colas = colas.quienesEstan();
        RealVector m = v_sensibilizadas.ebeMultiply(v_colas);
        Log.log.log(Level.INFO, (i++)+"\tDISPARE!\t Marcado: " + red.getMarcadoActual().toString().substring(20) + "\t" + Thread.currentThread().getName() +"\tSensi:"+v_sensibilizadas.toString()+ "\tT:" + t+"\tColas: "+v_colas.toString()+"\tm: "+ m.toString()+"\tPolitica: "+politica.cual(m));
        if (!isCero(m)) {
            colas.desencolar(politica.cual(m));
        }
          mutex.release();

      }

    /**
    Esta función revisa el vector entero y devuelve true si es cero en todos sus componentes,
    si algún componente no es cero, devuelve false.
     */

    private boolean isCero(@NotNull RealVector vector) {
    for (double d: vector.toArray()) {
        if (d!=0)
            return false;
        }
    return true;
    }
}

