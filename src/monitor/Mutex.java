package monitor;


import static java.lang.Boolean.FALSE;

public class Mutex {

    private boolean k = FALSE;

    public boolean acquire() {

        return k;

    }

    public boolean release() {

        return k;

    }
}
