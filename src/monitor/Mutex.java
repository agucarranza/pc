package monitor;


import java.util.concurrent.Semaphore;

public class Mutex {
/*
    private final BlockingQueue<Thread> mutex;

    public Mutex () {
        mutex = new ArrayBlockingQueue<>(1, true);
    }

    public void adquirirMutex () {
        try {
            mutex.put(Thread.currentThread());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void liberarMutex () {
        try {
            mutex.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

    private Semaphore mutex;

    public Mutex() {
        mutex = new Semaphore(1, true);
    }

    public void adquirirMutex() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void liberarMutex() {
        mutex.release();
    }

    public int permisos() {
        return mutex.availablePermits();
    }

}