package com.company;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class Test2 {

    static Queue<Buyers> queue1;
    static Queue<Buyers> mainQueue1;
    static SynchronousQueue<Buyers> synchronousQueue;

    public static void main(String[] args) throws InterruptedException {
        mainQueue1 = new PriorityQueue<>();
        queue1 = new PriorityQueue<>();
        ArrayList <Integer> tokens = new ArrayList<>();
        for (int i = 1; i <= 100 ; i++) {
            tokens.add(i);
        }

        for (int i = 0; i < 100; i++) {
            Buyers buyers = new Buyers("Buyer" + i);
            mainQueue1.add(buyers);
        }

        for (int i = 0; i < 20; i++) {
            queue1.poll().token = tokens.get((int) (Math.random()*tokens.size()));

            queue1.poll().start();

        }

        synchronousQueue = new SynchronousQueue<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable door = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        synchronousQueue.put(mainQueue1.poll());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable seller = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!queue1.isEmpty()) {
                        for (int i = 5; i > 0; i--) {
                            Thread.sleep(1000);
                            queue1.poll().interrupt();
                            if (!mainQueue1.isEmpty()) {
                                queue1.add(synchronousQueue.take());

                            } else {
                                return;
                            }
                        }
                        Thread.sleep(3000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        executor.execute(door);
        executor.execute(seller);

    }
}


class Buyers extends Thread implements Comparable {

    int token;


    public Buyers(String name) {
        this.setName(name);
    }

    @Override
    public void run() {

        try {
            System.out.println(this.getName() + " is coming");
            sleep(5000);
            System.out.println(this.getName() + " I am going out!!! I am not happy!!!");
            Test2.queue1.remove(this);
            Test2.queue1.add(Test2.synchronousQueue.take());

        } catch (InterruptedException e) {
            System.out.println(this.getName() + " I am going out!!! I am happy!!!");
        }

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}




