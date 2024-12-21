package org.example;

import org.zeromq.ZMQ;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ZeroMQInterProcessTwo {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("started");


        final ZMQ.Context context = ZMQ.context(1);
        // Thread 2: Receiver
        List<Long> values= new LinkedList<>();
        Thread receiverThread = new Thread(() -> {
            ZMQ.Socket receiver = context.socket(ZMQ.PAIR);
            //docker
            //receiver.connect("tcp://interprocessone:5555");
            //non docker
            receiver.connect("ipc://test");


            for (int i = 0; i < 4000; i++) {

                String message = receiver.recvStr();
                long endTime= System.nanoTime();
                long startTime = Long.parseLong(message);
                values.add(endTime-startTime);
                receiver.send(String.valueOf(System.nanoTime()));
            }

            receiver.close();
        });

        receiverThread.start();
        receiverThread.join();
        context.close();
        //docker
        //Utils.writeToCSV(values, "/app/data/InterProcessTwoDockerized.csv", 500);
        //nonDocker
        Utils.writeToCSV(values, "InterProcessTwo.csv", 500);
        values.forEach((e)-> System.out.println("RTT: " + e + " ns"));

    }

}
