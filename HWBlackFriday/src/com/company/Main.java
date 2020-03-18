package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    volatile static List<Buyer> queue;
    volatile static int numberOfHappy = 0;
    volatile static int numberOfUnhappy = 0;

    public static void main(String[] args) {
        Random random = new Random();
        queue = new ArrayList<>();

        ArrayList<Integer> tokens = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            tokens.add(i);
        }

        Thread seller = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!queue.isEmpty()) {
                        for (int i = 5; i > 0; i--) {
                            Thread.sleep(30);
                            if (!queue.isEmpty()) {
                                queue.get(random.nextInt(queue.size())).interrupt();
                            }
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread door = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!tokens.isEmpty()) {
                    if (queue.size() < 20) {
                        Integer token = tokens.get(random.nextInt(tokens.size()));
                        tokens.remove(token);
                        Buyer buyer = new Buyer(token);
                        queue.add(buyer);
                        buyer.start();

                    }
                }
            }
        });

        for (int i = 1; i <= 20; i++) {
            Integer token = tokens.get(random.nextInt(tokens.size()));
            tokens.remove(token);
            Buyer buyer = new Buyer(token);
            queue.add(buyer);
            buyer.start();

        }
        door.start();
        seller.start();
        while (seller.isAlive()){
            Thread.yield();
        }

        System.out.println("");
        System.out.println("The day is over");
        System.out.print("The number of happy clients is ");
        System.out.println(numberOfHappy);
        System.out.print("The number of unhappy clients is ");
        System.out.println(numberOfUnhappy);


    }

}

class Buyer extends Thread {
    private int token;

    public Buyer(int token) {
        this.token = token;
    }

    @Override
    public void run() {

        System.out.println("Buyer " + token + " is coming");
        try {
            sleep(1000);
            Main.queue.remove(this);
            System.out.println("Buyer " + token + " is leaving unhappy");
            Main.numberOfUnhappy++;

        } catch (InterruptedException e) {
            Main.queue.remove(this);
            System.out.println("Buyer " + token + " is leaving happy");
            Main.numberOfHappy++;

        }

    }
}
