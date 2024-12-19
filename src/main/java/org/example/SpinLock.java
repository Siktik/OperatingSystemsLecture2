package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpinLock {

    private final AtomicBoolean lock = new AtomicBoolean(false);



    List<DataPoint> acquired = Collections.synchronizedList(new LinkedList<>());
    List<DataPoint> released = Collections.synchronizedList(new LinkedList<>());


    public boolean isLocked() {
        return lock.get();
    }

    public void run(int numberOfRuns, boolean writeToCSV){

        this.numberOfRuns= numberOfRuns;
        Thread tOne= new Thread(task, "A");
        Thread tTwo= new Thread(task, "B");
        tOne.start();
        tTwo.start();
        try {
            // Wait for both threads to complete
            tOne.join();
            tTwo.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted: " + e.getMessage());
        }



        System.out.println("Acquired size "+ acquired.size());
        for (int i=0; i< acquired.size(); i++){
            System.out.println(acquired.get(i).getThread()+","+released.get(i).getThread());
        }

        List<Long> values= new LinkedList<>();
        Utils.calculateValues(released,acquired,values);
        System.out.println("Overall DataPoints calculated are: "+ values.size()+"/"+ ((numberOfRuns*2)-2));
        if(writeToCSV)
            Utils.writeToCSV(values, "spinLockLatency.csv");

    }


    public void lock() {
        while (!lock.compareAndSet(false, true)) {
            // Spin-wait until the lock is acquired
        }
        long end= System.nanoTime();

        acquired.add(new DataPoint(Thread.currentThread().getName(), end));

    }
    public void unlock() {


        long start= System.nanoTime();
        released.add(new DataPoint(Thread.currentThread().getName(), start));
        lock.set(false);
    }

    int numberOfRuns; // in a perfect world this would lead to 2*numberOfRuns datapoints
    Runnable task = () ->{
        for(int y=0; y< numberOfRuns; y++) {
            //System.out.println(Thread.currentThread()+" iter "+ y);
            lock();
            int summedUp = 0;
            for (int i = 1; i < 50; i++) {

                summedUp += i;
            }
            unlock();
        }
    };






}
