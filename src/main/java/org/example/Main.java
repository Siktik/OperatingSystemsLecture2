package org.example;

public class Main {
    public static void main(String[] args) {

        int numberOfRuns=50000;
        boolean writeToCSV = false;

        //SpinLock lock= new SpinLock();
        //lock.run(numberOfRuns, writeToCSV);
        SemaphoreLatency semaphore= new SemaphoreLatency();
        semaphore.run(numberOfRuns, writeToCSV);
    }





}