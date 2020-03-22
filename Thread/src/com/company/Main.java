package com.company;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Human human = new Human();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    human.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    human.consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        human.Info();
    }
}