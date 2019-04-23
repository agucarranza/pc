package monitor;

import org.apache.commons.math3.linear.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class RdP {

    private int plazas;
    private int transiciones;
    private RealMatrix incidencia;
    private RealMatrix marcadoInicial;
    private RealMatrix inhibicion;
    private RealVector sensibilizadas;

    private boolean k;

    public RdP(String incidenceFile,
               String markingFile,
               String inhibitionFile) {

        incidencia = parseFile(incidenceFile);
        marcadoInicial = parseFile(markingFile);
        inhibicion = parseFile(inhibitionFile);
        plazas = incidencia.getRowDimension();
        transiciones = incidencia.getColumnDimension();
    }

    public RealMatrix parseFile(String fileName) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            String[] items = line.split(",");
            items = Arrays.copyOfRange(items, 1, items.length); //Discarding first empty object
            int columnas = items.length;
            ArrayList<double[]> linelist = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                items = line.split(",");
                items = Arrays.copyOfRange(items, 1, items.length); //Discarding first column
                linelist.add(Arrays.stream(items).mapToDouble(Double::parseDouble).toArray());
            }
            RealMatrix m = MatrixUtils.createRealMatrix(linelist.toArray(new double[linelist.size()][columnas]));
            br.close();
            return m;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Archivo " + fileName + " no encontrado.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error: Error de entrada/salida");
            System.exit(-1);
        }
        return null;
    }

    /*
    disparar() utiliza la funcion de estado para generar un nuevo estado a partir del
    actual.
    Devuelve k = true cuando pudo disparar una transicion. False cuando no encontró
    ninguna transición para disparar en sensibilizadas ni en las colas.
     */
    public boolean disparar() {

        RealVector nuevoEstado;
        RealVector viejoEstado;
        // Real Vector enCola;
        RealVector m;


        return k;
    }

    /*
    Este metodo utiliza la ecuacion de estado de la RDP para intentar disparar
    todas las transiciones (de a una por vez) con el bucle. Aquellas cuyo nuevo estado
    es mayor o igual que 0, estan habilitadas. Las demás estan deshabilitadas. Al principio
    del bucle, pongo un uno en la transicion que quiero disparar, y al final lo saco para
    que luego el bucle lo agregue en la transicion siguiente.
     */

    public RealVector sensibilizadas() {

        RealVector vector;
        RealVector vector_sensibilizadas = new ArrayRealVector(transiciones);
        RealMatrix disparo = new Array2DRowRealMatrix(transiciones, 1);
        RealMatrix marcado = marcadoInicial.transpose();

        for (int k = 0; k < transiciones; k++) { //Dispara todas las transiciones

            disparo.setEntry(k, 0, 1);

            vector = marcado.add(incidencia.multiply(disparo)).getColumnVector(0);

            if (vector.getMinValue() >= 0)
                vector_sensibilizadas.setEntry(k, 1);

            disparo.setEntry(k, 0, 0);
        }
        return vector_sensibilizadas;
    }
}
