package com.company;

import java.util.ArrayList;

public class Seller {

    public static ArrayList<Buyers> buyers = new ArrayList<>();
    public static ArrayList<Integer> lostTickets = new ArrayList<>();
    int ticket;


    public void createBuyers(){
        if (ticket<100) {
            for (; buyers.size() < 20; ) {
                Buyers buyer = new Buyers(ticket++);
                buyer.start();
                buyers.add(buyer);
            }
        }
    }
    public void seller(){
        for(;ticket<100;){
            for (int i = 0;i <5;i++){
                createBuyers();
                try {
                    Thread.sleep(30);
                    int random = random();
                    buyers.get(random).interrupt();
                    buyers.remove(buyers.get(random));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
            showLosers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public int random(){
        return (int) (Math.random() * (buyers.size()-1));
    }
    public void showLosers(){
        int i = 0;
        for (; i < lostTickets.size(); i++){
            System.out.printf("Номер %d несчастливый\n", lostTickets.get(i));
        }
        System.out.println("Всего ушедших:");
        System.out.println(i);
    }
}