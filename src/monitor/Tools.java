package monitor;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Incluye métodos comunes a todas las clases para realizar operaciones de parsing.
 */
class Tools {

    /**
     Esta función abre el archivo que se le pasa por parámetro y retorna una RealMatrix con los datos. Descarta
     la primer fila y la primer columna que son los índices de plazas y transiciones.
     @param fileName La ruta del archivo que se quiere convertir.
     @return Una matriz con los valores cargados a partir del String pasado por parámetro.
     */

    static RealMatrix parseFile(String fileName) {

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
}
