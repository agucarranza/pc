package monitor;

public class GestorDeMonitor {

    private boolean k;
    private Mutex mutex;
    private RdP red;



    public GestorDeMonitor() {

        //m.acquire();

    }

    public void dispararTransicion() {
        mutex.acquire();

    }




}
