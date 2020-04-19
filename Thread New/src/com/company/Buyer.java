package com.company;

import java.util.ArrayList;
import static com.company.Main.littleTurn;
public class Buyer extends Thread {
    static ArrayList<Thread> notHappyTurn = new ArrayList();
   static ArrayList <Thread> happyTurn  = new ArrayList();
    int nameBuyer;
    Buyer(int nameBuyer ){
        this.nameBuyer = nameBuyer;
    }
    @Override
    public synchronized void run(){
        Thread Buyer = Thread.currentThread();
        try {
            Buyer.sleep(1000);// Покупатель ждёт 1000мс
            System.out.println(Buyer.getName() + " я недоволен класс Buyer"); // Если ничего не происходит уходит из магазина
            notHappyTurn.add(Buyer);
            Buyer.interrupt();
            littleTurn.remove(Buyer);
        } catch (InterruptedException e) {
           // System.out.println(Buyer.getName() + " Я нашёл всё что нужно. класс Buyer");
            happyTurn.add(Buyer);
        }
    }
}


