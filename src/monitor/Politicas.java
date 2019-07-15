package monitor;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Politicas {
    private RealMatrix politica;

    public Politicas() {
        politica = Tools.parseFile("./petri-nets/politicas.csv");
    }

    public int cual(RealVector vector) {
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
