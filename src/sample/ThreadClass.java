package sample;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadClass {
    Measurements measurements = new Measurements();

 /*   public boolean isActiveChecker() {
        return ActiveChecker;
    }
  */

    public void setActiveChecker(boolean activeChecker) {
        ActiveChecker = activeChecker;
    }


    boolean ActiveChecker = true;
    private final  ExecutorService sqlt = Executors.newSingleThreadExecutor() ;
    public ExecutorService getSqlt(){return sqlt;}

    private final Thread MainThread = new Thread(new Runnable() {

        @Override
        public void run() {
            while (ActiveChecker) {
                measurements.DataProcessing();
                Platform.runLater(Plotter);
                getSqlt().execute(SqlTR);
            }
        }
    });
    public void getThread(Thread thread){
        if(!thread.isAlive()) {
            Thread threadStarter = new Thread(thread);
            threadStarter.start();
        }
    }
    public Thread getMainThread(){
        return MainThread;
    }

    private final Thread Plotter = new Thread(() -> {
        measurements.ShowValues();

    });
    private final Thread SqlTR = new Thread(() -> {
        measurements.SaveData();

    });
}
