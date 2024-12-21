package org.example;

import org.zeromq.ZMQ;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ZeroMQInterProcessOne {

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("started");


        final ZMQ.Context context = ZMQ.context(1);

        List<Long> values= new LinkedList<>();
        // Thread 1: Sender
        Thread senderThread = new Thread(() -> {
            ZMQ.Socket sender = context.socket(ZMQ.PAIR);
            //docker
            //sender.bind("tcp://0.0.0.0:5555");
            //non docker
            sender.bind("ipc://test");
            long startTime = 0;

            for (int i = 0; i < 4000; i++) {
                startTime = System.nanoTime();
                sender.send(String.valueOf(startTime));
                String reply = sender.recvStr();
                long endTime = System.nanoTime();
                startTime= Long.parseLong(reply);
                values.add(endTime-startTime);
            }

            sender.close();
        });

        senderThread.start();
        senderThread.join();
        context.close();
        //docker
        //Utils.writeToCSV(values, "/app/data/InterProcessOneDockerized.csv", 500);
        //nonDocker
        Utils.writeToCSV(values, "InterProcessOne.csv", 500);
        values.forEach((e)-> System.out.println("RTT: " + e + " ns"));

    }


}
