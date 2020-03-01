package com.company;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

public class Main {
    static ArrayList<Integer> tickets = new ArrayList<>();
    static ArrayList<Integer> unlucky = new ArrayList<>();
    static int happy = 0;

    public static void main(String[] args) {
        final SynchronousQueue syncQueue = new SynchronousQueue();
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (;;){
                    Buyers buyers = new Buyers();
                    syncQueue.put(Buyers.ticket);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Программа завершена");;
                }
            }
        });
        newThread.start();
        tickets.add(0);
        for (;tickets.size() > 0;) {
            for (int r = 0; r < 5; r++) {
                if (tickets.get(tickets.size()-1) < 99) {
                    tickets.remove((Integer) 0);
                    try {
                        for (;tickets.size() < 20; ) {
                            tickets.add((Integer) syncQueue.take());
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getLuckyBastards();
                }
                else {
                    for(;tickets.size()>0;){
                        getLuckyBastards();
                        checkWhoWillLeave();
                    }
                    break;
                }
                checkWhoWillLeave();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.printf("Число счастливых покупателей - %d. Число несчастливых - %d\n", happy, 100 - happy);
        for (Integer list: unlucky){
            System.out.printf("Жетон номер %d - несчастливый\n",list);
        }
        newThread.interrupt();
    }
    public static int randomGeneration(){
        double number = Math.random()*tickets.size();
        return (int) number;
    }
    public static void checkWhoWillLeave(){
        for (int f = 0; f < tickets.size() - 1; f++) {
            if (tickets.get(tickets.size() - 1) > tickets.get(f) + 20) {
                System.out.printf("покупатель %d - %s\n", tickets.get(f), TicketStatus.UNHAPPY.getStatus());
                unlucky.add(tickets.get(f));
                tickets.remove(f);
            }
        }
    }
    public static void getLuckyBastards(){
        Integer buyer = tickets.get(randomGeneration());
        System.out.printf("покупатель %d - %s\n", buyer, TicketStatus.HAPPY.getStatus());
        happy++;
        System.out.print("счастливых всего - ");
        System.out.println(happy);
        tickets.remove(buyer);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
