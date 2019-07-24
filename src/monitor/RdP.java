package monitor;

import log.Log;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.logging.Level;

import static java.lang.Thread.currentThread;


public class RdP {

    private int transiciones;
    private RealMatrix incidencia;
    private RealMatrix incidencianeg;
    private RealMatrix marcadoActual;
    private RealMatrix politica;
    private PInvariant[] invariantes;

    public RdP(String incidenceFile,
               String markingFile,
               String incidencianegFile,
               String policyFile) {

        incidencia = Tools.parseFile(incidenceFile);
        incidencianeg = Tools.parseFile(incidencianegFile);
        marcadoActual = Tools.parseFile(markingFile); //cargo el marcado inicial
        politica = Tools.parseFile(policyFile);
        transiciones = incidencia.getColumnDimension();
        Log.log.log(Level.INFO,"INICIO\t\t Marcado: "+getMarcadoActual().toString().substring(20)+"\t"+ currentThread().getName());
    }

    /*
    disparar() utiliza la función de estado para generar un nuevo estado a partir del
    actual.
    Devuelve k = true cuando pudo disparar una transicion. False cuando no encontró
    ninguna transición para disparar en sensibilizadas.
     */

    public boolean disparar(int transicion) throws RuntimeException {
      
        	
        if (transicion == 0) {   //Inhibidor
        	RealVector v1 =this.marcadoActual.getRowVector(0);
        	if (v1.getEntry(21) > 0 || v1.getEntry(22)==0) {  //si hay lugares de estacionamiento, o si el cartel ya esta prendido
        		return false;
        	}
        	RealMatrix marcadoNuevo;
            RealMatrix disparo = getVectorDisparo(transicion);
            RealMatrix marcado = marcadoActual.transpose();
            marcadoNuevo = marcado.add(incidencia.multiply(disparo));
            marcadoActual = marcadoNuevo.transpose();

            	
            return true;
        	        	
        }
        	  
        else {
        if ((transicion < 0) || (transicion > transiciones))
            throw new RuntimeException("Numero de transicion fuera de limites");
        
        if (sensibilizadas().getEntry(transicion) != 1)
            return false;

        RealMatrix marcadoNuevo;
        RealMatrix disparo = getVectorDisparo(transicion);
        RealMatrix marcado = marcadoActual.transpose();
        marcadoNuevo = marcado.add(incidencia.multiply(disparo));
        marcadoActual = marcadoNuevo.transpose();

        	
        return true;
        }
    }

    /*
    Este método utiliza la ecuación de estado de la RDP para intentar disparar
    todas las transiciones (de a una por vez) con el bucle. Aquellas cuyo nuevo estado
    es mayor o igual que 0, están habilitadas. Las demás están deshabilitadas. Al principio
    del bucle, pongo un uno en la transicion que quiero disparar, y al final lo saco para
    que luego el bucle lo agregue en la transicion siguiente.
     */

    public RealVector sensibilizadas() {

        RealVector vector;
        RealVector vector_sensibilizadas = new ArrayRealVector(transiciones);
        RealMatrix disparo = new Array2DRowRealMatrix(transiciones, 1);
        RealMatrix marcado = marcadoActual.transpose();

        for (int k = 0; k < transiciones; k++) { //Dispara todas las transiciones
            disparo.setEntry(k, 0, 1);
            vector = marcado.add(incidencianeg.multiply(disparo)).getColumnVector(0);
            if (vector.getMinValue() >= 0)
                vector_sensibilizadas.setEntry(k, 1);
            disparo.setEntry(k, 0, 0);
        }
        return vector_sensibilizadas;
    }

    /*
    Este método crea un vector con todos los valores ceros salvo el valor que le pase
    por el parámetro transicion. Se usa en el método disparar para implementar la ecuación de estado.
     */

    private RealMatrix getVectorDisparo(int transicion) {
        RealMatrix disparo = new Array2DRowRealMatrix(transiciones,1);
        disparo.setEntry(transicion,0,1);
        return disparo;
    }

    /*
     * Este método recibe el vector de sensibilizadas como parámetro.
     * Se posmultiplica con la matriz de politica previamente cargado.
     * De ese resultado, elijo la primera transicion que encuentre y borro las otras.
     * Luego la posmultiplico con la matriz de politica nuevamente y consigo el
     * el indice de la transicion a disparar.
     */

    public int politica(RealVector vector) {

        RealMatrix politica = this.politica;
        RealVector t1 = politica.operate(vector);
        RealVector t2 = new ArrayRealVector(t1.getDimension());
        for (int i = 0; i < t1.getDimension(); i++) {
            if (t1.getEntry(i) > 0) {
                t2.setEntry(i, 1);
                break;
            }
        }
        RealVector td = politica.transpose().operate(t2);
        return td.getMaxIndex();

    }
    
    public int getTransiciones() {
        return transiciones;
    }

    public RealMatrix getMarcadoActual() {
        return marcadoActual;
    }
    
    public void setPInvariants(PInvariant[] inv){
        this.invariantes = inv;
    }
    
    public boolean checkPInvariant(){
    //	System.out.println("entra assert");
        for(PInvariant inv: this.invariantes){
            int[] plist = inv.getPlaza();
            int valor = inv.getValor();
            RealVector vaux = new ArrayRealVector(this.incidencia.getRowDimension());
            Arrays.stream(plist).forEach(i -> vaux.setEntry(i, 1.));
            if(valor != (int) vaux.dotProduct(this.marcadoActual.getRowVector(0)))return false;
        }
      //  System.out.println("sale assert");
        return true;
      
    }
  
}
