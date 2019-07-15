package monitor;

import log.Log;
import org.apache.commons.math3.linear.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import static java.lang.Thread.*;

public class RdP {

    private int transiciones;
    private double ventana;
    private RealMatrix incidencia;
    private RealMatrix marcadoActual;
    private RealMatrix inhibicion;
    private RealVector alfa;
    private RealVector beta;
    private List<Double> timeStamp;

    public RdP(String incidenceFile,
               String markingFile,
               String inhibitionFile,
               String alfa,
               String beta) {

        incidencia = Tools.parseFile(incidenceFile);
        marcadoActual = Tools.parseFile(markingFile); //cargo el marcado inicial
        inhibicion = Tools.parseFile(inhibitionFile);
        transiciones = incidencia.getColumnDimension();
        this.alfa = Tools.parseFile(alfa).getRowVector(0);
        this.beta = Tools.parseFile(beta).getRowVector(0);
        Log.log.log(Level.INFO,"INICIO\t\t Marcado: "+getMarcadoActual().toString().substring(20)+"\t"+ currentThread().getName());
        System.out.println(this.alfa.toString());
        timeStamp = Collections.synchronizedList(new ArrayList<>());
    }

    /*
    disparar() utiliza la función de estado para generar un nuevo estado a partir del
    actual.
    Devuelve k = true cuando pudo disparar una transicion. False cuando no encontró
    ninguna transición para disparar en sensibilizadas.
     */

    public boolean disparar(int transicion)  {
        try {
            if ((transicion < 0) || (transicion > transiciones))
                throw new RuntimeException("Numero de transicion fuera de limites");
            if (sensibilizadas().getEntry(transicion) != 1)
                return false;

            RealMatrix marcadoNuevo;
            RealMatrix disparo = getVectorDisparo(transicion);
            RealMatrix marcado = marcadoActual.transpose();
            marcadoNuevo = marcado.add(incidencia.multiply(disparo));
            marcadoActual = marcadoNuevo.transpose();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return true;
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
            vector = marcado.add(incidencia.multiply(disparo)).getColumnVector(0);
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

    private boolean tieneTiempo(int transicion) {

        if(alfa.getEntry(transicion)!= 0 ||beta.getEntry(transicion) != 0)
            return true;
        else
            return false;
    }

    private int cumpleVentanaDeTiempo(int transicion, double tiempo) {
        double alfa = this.alfa.getEntry(transicion);
        double beta = this.beta.getEntry(transicion);
        double tiempoSensibilizada = tiempo - timeStamp.get(transicion);
        if(beta ==0 ) {
            if(alfa > tiempoSensibilizada)
                return 0;
            else
                return 1;
        }
        else {
            if(alfa > tiempoSensibilizada)
                return 0;
            else if(alfa <= tiempoSensibilizada && beta >tiempoSensibilizada  )
                return 1;
            else
                return -1; //Indica que la transicion excedio el tiempo de la ventana
        }
    }

    private double dormir(int transicion, double tiempo) {
        double alfa = this.alfa.getEntry(transicion);
        return (alfa-(tiempo-timeStamp.get(transicion)));
    }

    public double getVentanaDeTiempo(int transicion) {
        double tiempo = System.currentTimeMillis();
        if(!this.tieneTiempo(transicion))
            return 0;
        else if(this.cumpleVentanaDeTiempo(transicion, tiempo) == 1)
            return 0;
        else if(this.cumpleVentanaDeTiempo(transicion, tiempo) == 0)
            return dormir(transicion,tiempo);
        return -1; //La transicion excedio el tiempo de la ventana
    }

    public int getTransiciones() {
        return transiciones;
    }

    public RealMatrix getMarcadoActual() {
        return marcadoActual;
    }
}
