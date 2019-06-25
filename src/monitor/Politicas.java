package monitor;

import org.apache.commons.math3.linear.RealVector;

public class Politicas {

    public Politicas() {
    }


    public int cual(RealVector vector) {

        if (vector.getEntry(0)==1)
            return 0;

        return -1;
    }


}


