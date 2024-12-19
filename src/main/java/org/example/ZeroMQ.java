package org.example;

import org.zeromq.ZMQ;

import java.util.LinkedList;
import java.util.List;

public class ZeroMQ {
    public static void main(String[] args) throws InterruptedException {
        final ZMQ.Context context = ZMQ.context(0);

        List<Long> values= new LinkedList<>();
        // Thread 1: Sender
        Thread senderThread = new Thread(() -> {
            ZMQ.Socket sender = context.socket(ZMQ.PAIR);
            sender.bind("inproc://test");
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

        // Thread 2: Receiver
        Thread receiverThread = new Thread(() -> {
            ZMQ.Socket receiver = context.socket(ZMQ.PAIR);
            receiver.connect("inproc://test");

            for (int i = 0; i < 4000; i++) {

                String message = receiver.recvStr();
                long endTime= System.nanoTime();
                long startTime = Long.parseLong(message);
                values.add(endTime-startTime);
                receiver.send(String.valueOf(System.nanoTime()));
            }

            receiver.close();
        });

        senderThread.start();
        receiverThread.start();
        senderThread.join();
        receiverThread.join();
        context.close();
        Utils.writeToCSV(values, "InProcZeroMQ.csv", 1000);
        values.forEach((e)-> System.out.println("RTT: " + e + " ns")
);
    }
}

