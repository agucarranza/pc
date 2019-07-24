package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

class Politicas {
    private RealMatrix politica;

    Politicas() {
        politica = Tools.parseFile("./petri-nets/identidad.csv");
    }

    /**
     * Este método recibe el vector de sensibilizadas como parámetro.
     * Se posmultiplica con la matriz de politica previamente cargado.
     * De ese resultado, elijo la primera transicion que encuentre y borro las otras.
     * Luego la posmultiplico con la matriz de politica nuevamente y consigo el
     * el indice de la transicion a disparar.
     */

    int cual(RealVector vector) {
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
}
