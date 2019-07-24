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
    private long ventana;
    private RealMatrix incidencia;
    private RealMatrix marcadoActual;
    //private RealMatrix inhibicion;
    private RealVector alfa;
    private RealVector beta;
    private long[] timeStamp;
    private RealVector vSensib;

    public RdP(String incidenceFile,
               String markingFile,
               //  String inhibitionFile,
               String alfa,
               String beta) {

        incidencia = Tools.parseFile(incidenceFile);
        marcadoActual = Tools.parseFile(markingFile); //cargo el marcado inicial
        //inhibicion = Tools.parseFile(inhibitionFile);
        transiciones = incidencia.getColumnDimension();
        this.alfa = Tools.parseFile(alfa).getRowVector(0);
        this.beta = Tools.parseFile(beta).getRowVector(0);
        Log.log.log(Level.INFO,"INICIO\t\t Marcado: "+getMarcadoActual().toString().substring(20)+"\t"+ currentThread().getName());
        System.out.println(this.alfa.toString());
        //timeStamp = Collections.synchronizedList(new ArrayList<>(transiciones));
        vSensib = sensibilizadas();
        timeStamp = new long[transiciones];
        Arrays.fill(timeStamp, 0);



    }

    /**
     disparar() utiliza la función de estado para generar un nuevo estado a partir del
     actual.
     Devuelve k = true cuando pudo disparar una transicion. False cuando no encontró
     ninguna transición para disparar en sensibilizadas.
     CUANDO LA VENTANA TIENE VALOR SALE DE LA FUNCIÓN
     */

    boolean disparar(int transicion)  {
        try {
            if ((transicion < 0) || (transicion > transiciones))
                throw new RuntimeException("Numero de transicion fuera de limites");
            if (sensibilizadas().getEntry(transicion) != 1) {
                ventana = 0;
                return false;
            }
            ventana = this.getVentanaDeTiempo(transicion);
            chequearPrioridad();
            if (ventana != 0)
                return false;
            RealMatrix marcadoNuevo;
            RealMatrix disparo = getVectorDisparo(transicion);
            RealMatrix marcado = marcadoActual.transpose();
            marcadoNuevo = marcado.add(incidencia.multiply(disparo));
            marcadoActual = marcadoNuevo.transpose();
            // timeStamp.set(transicion, 0.0);
            timeStamp[transicion] = 0;
            calculoTimeStamp();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     Este método utiliza la ecuación de estado de la RDP para intentar disparar
     todas las transiciones (de a una por vez) con el bucle. Aquellas cuyo nuevo estado
     es mayor o igual que 0, están habilitadas. Las demás están deshabilitadas. Al principio
     del bucle, pongo un uno en la transicion que quiero disparar, y al final lo saco para
     que luego el bucle lo agregue en la transicion siguiente.
     */

    RealVector sensibilizadas() {

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

    /**
    Este método crea un vector con todos los valores ceros salvo el valor que le pase
    por el parámetro transicion. Se usa en el método disparar para implementar la ecuación de estado.
     */

    private RealMatrix getVectorDisparo(int transicion) {
        RealMatrix disparo = new Array2DRowRealMatrix(transiciones,1);
        disparo.setEntry(transicion,0,1);
        return disparo;
    }

    /**
     * Método para saber si una transición es temporizada o inmediata.
     * @param transicion Es la transición sobre la cual se está consultando.
     * @return True si alguno de los parámetros de tiempo es no nulo. False si ningún parámetro tiene
     * valor no nulo.
     */

    private boolean tieneTiempo(int transicion) {
        return alfa.getEntry(transicion) != 0 || beta.getEntry(transicion) != 0;
    }

    /**
     * Función que retorna el valor
     * @param   transicion Es el número de transición sobre la cual se controla.
     * @return Devuelve 0 si es una transición sin tiempo o con tiempo y DENTRO de la ventana
     *
     */

    private long getVentanaDeTiempo(int transicion) {
        long ahora = System.currentTimeMillis();
        // Es una transición sin tiempo.
        if(!this.tieneTiempo(transicion))
            return 0;
            // Es una transición con tiempo y está DENTRO de la ventana.
        else if (this.cumpleVentanaDeTiempo(transicion, ahora) == 1)
            return 0;
            // Es una transición con tiempo y está ANTES de la ventana.
        else if (this.cumpleVentanaDeTiempo(transicion, ahora) == 0)
            return dormir(transicion, ahora);
        // Es una transición sin tiempo o con tiempo DESPUÉS de la ventana.
        return -1;
    }

    /**
     * tiempoSensibilizada es la diferencia entre el tiempo actual y el tiempo almacenado en timestamp.
     * @param transicion Transición
     * @param tiempo Es el instante actual.
     * @return  Si beta no tiene tiempo, controla alfa. tiempoSensibilizada debe ser mayor que alfa.
     *          tiempoSensibilizada tiene que estar entre alfa y beta para que retorne true.
     * ENTIEMPO de cristian.
     *
     */

    private int cumpleVentanaDeTiempo(int transicion, double tiempo) {
        double alfa = this.alfa.getEntry(transicion);
        double beta = this.beta.getEntry(transicion);
        double tiempoSensibilizada = tiempo - timeStamp[transicion]; //timeStamp.get(transicion);
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
                return -1; //Indica que la transicion excedió el tiempo de la ventana
        }
    }

    /**
     * Función que viene de getVentanaDeTiempo porque la transición se quiso disparar antes de alfa.
     * @param transicion Es la transición en cuestión.
     * @param tiempo Tiempo es el valor que le pasa getVentanaDeTiempo, que es el tiempo actual.
     * @return TERMINAR DE ENTENDER.
     */
    private long dormir(int transicion, long tiempo) {
        long alfa = (long) this.alfa.getEntry(transicion);
        // return (alfa-(tiempo-timeStamp.get(transicion)));
        return (alfa - (tiempo - timeStamp[transicion]));
    }

    /**
     * CalculoTimeStamp recorre el array completo. Para cada transición que está sensibilizada
     * guarda el tiempo actual. Si no está sensibilizada, pone en cero el timestamp.
     * (Ver si hay que preguntar si el valor del array es cero en el if).
     */

    private void calculoTimeStamp() {
        for (int i = 0; i < timeStamp.length; i++) {
            if (sensibilizadas().getEntry(i) == 1 && timeStamp[i] == 0)
                timeStamp[i] = System.currentTimeMillis();
            else
                timeStamp[i] = 0;
        }
    }

    private void chequearPrioridad() {
        if (Thread.currentThread().getPriority() == Thread.MAX_PRIORITY) {
            ventana = 0;
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }
    }

    int getTransiciones() {
        return transiciones;
    }

    RealMatrix getMarcadoActual() {
        return marcadoActual;
    }

    long getVentana() {
        return ventana;
    }

}