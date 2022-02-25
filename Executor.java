import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.Condition;


/**
 * Overriding and Extension of the ScheduledThreadPoolExecutor Class
 * Adds ability to pause and resume execution on new tasks
 * Checks and holds execution before tasks across all threads
 * 
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public class Executor extends ScheduledThreadPoolExecutor {
    private ReentrantLock lock = new ReentrantLock();
    private Condition change = lock.newCondition();
    private boolean paused;

    /**
     * Default constructor for a fixed set of threads thread
     */
    public Executor(int coreNumber) {
        super(coreNumber);
    }

    /**
     * What is executed before a task is given to a thread.
     * Pauses execution if paused until execution resumed
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        lock.lock();
            try {
            while (paused)
                change.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            lock.unlock();
        }
        super.beforeExecute(t, r);
    } 

    /**
     * Resumes the execution
     */
    public void resume() {
        lock.lock();
        try {
            paused = false;
            change.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Pauses the execution. Current task will continue
     * But new tasks will not start until the execution service is resumed.
     */
    public void pause() {
        lock.lock();
        try {
            paused = true;
        } finally {
            lock.unlock();
        }
    }
}
