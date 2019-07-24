package monitor;


import log.Log;
import org.apache.commons.math3.linear.RealVector;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;

public class GestorDeMonitor {

    public Semaphore mutex;
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

    /*
     * Poner comentario de cómo funciona la función Disparar transición.
     *
     */

    public void dispararTransicion(int t) throws InterruptedException {
    	
    	mutex.acquire();
    	while (!red.disparar(t)) {
            mutex.release();
            colas.encolar(t);
            mutex.acquire();
        }
    
    		 try{
    	  RealVector v_sensibilizadas = red.sensibilizadas();
          RealVector v_colas = colas.quienesEstan();
          RealVector m = v_sensibilizadas.ebeMultiply(v_colas);
          Log.log.log(Level.INFO, (in++)+"\tDISPARE!\t Marcado: " + red.getMarcadoActual().toString().substring(20) + "\t" + Thread.currentThread().getName() +"\tSensi:"+v_sensibilizadas.toString()+ "\tT:" + t+"\tColas: "+v_colas.toString()+"\tm: "+ m.toString()+"\tPolitica: "+red.politica(m));
         // System.out.println(t);
        //  assertTrue(this.red.checkPInvariant());  descomentar cuando esten cargadas las p invariantes en el main
          if (!isCero(m)) {
              colas.desencolar(politica.cual(m));
          }
         
    	 }catch(AssertionError e){
             System.out.println
             ("Msg From: "+Thread.currentThread().getName()+
              "\n[!] ERROR: Algun invariante no se cumple, terminando");
         System.exit(-1);
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

