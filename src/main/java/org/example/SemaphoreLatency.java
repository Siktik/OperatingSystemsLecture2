package org.example;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

class SemaphoreLatency {
    private Semaphore semaphore = new Semaphore(1, true);  // Producer can start immediately
    List<DataPoint> acquired = Collections.synchronizedList(new LinkedList<>());
    List<DataPoint> released = Collections.synchronizedList(new LinkedList<>());


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
        Utils.writeToCSV(values, "semaphoreLatency.csv", 20);



    }

    private void lock(){

        semaphore.acquireUninterruptibly();
        long end= System.nanoTime();
        acquired.add(new DataPoint(Thread.currentThread().getName(), end));

    }
    private void unlock(){

        long start= System.nanoTime();
        released.add(new DataPoint(Thread.currentThread().getName(), start));
        semaphore.release();

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




