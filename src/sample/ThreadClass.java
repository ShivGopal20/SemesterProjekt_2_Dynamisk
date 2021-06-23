package sample;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadClass {
    Measurements measurements = new Measurements();

    //Her sættes en boolean variabel, som skal bruges til at afgøre om MainThread skal gøre.
    //Den bruges også i forbindelse med stop-knappen.
    public void setActiveChecker(boolean activeChecker) {
        ActiveChecker = activeChecker;
    }

    boolean ActiveChecker = true;
    private final ExecutorService sqlt = Executors.newSingleThreadExecutor();

    public ExecutorService getSqlt() { return sqlt; }

    //Dette er MainThread, altså hovedtråde som indeholder en run() metode.
    private final Thread MainThread = new Thread(new Runnable() {

        @Override
        public void run() {
            //I run() metoden behandles dataen først og indeles i en Array.
            //Herefter aktiveres Plotter tråden med en runLater. Plotter tråden sætter ShowValues() metoden igang.
            //SqlTR aktiveres også, som indsætter værdierne i databasen.
            while (ActiveChecker) {
                measurements.DataProcessing();
                Platform.runLater(Plotter);
                getSqlt().execute(SqlTR);
            }
        }
    });

    //getThread bruges til at opstarte tråden, hvis den ikke allerede gøre,
    public void getThread(Thread thread) {
        if (!thread.isAlive()) {
            Thread threadStarter = new Thread(thread);
            threadStarter.start();
        }
    }

    //Denne tråd indeholder de to tråde, som tilkaldes i MainThreads run-metode.
    public Thread getMainThread() { return MainThread; }

    private final Thread Plotter = new Thread(() -> { measurements.ShowValues(); });
    private final Thread SqlTR = new Thread(() -> {
        measurements.SaveData();
    });
}
