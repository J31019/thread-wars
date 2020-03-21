package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    static int tripsToWarehouse = 0;
    static LinkedList<String> unHappyList = new LinkedList<>();
    static LinkedList<String> happyList = new LinkedList<>();
    static ArrayList<Thread> list20 = new ArrayList<>();

    public static void main(String[] args) {

        class People extends Thread {
            int numName;
            People(int numName) {
                this.numName = numName;
            }
            @Override
            public synchronized void run() {
                synchronized (unHappyList) {
                    Thread ct = Thread.currentThread();
                    try {
                        Thread.sleep(1000);//время ожидания покупателя в магазине
                        unHappyList.add(ct.getName());
                        list20.remove(ct);
                        System.out.println(ct.getName() + " Я устал,я ухожу!");

                    } catch (InterruptedException e) {
                        System.out.println(ct.getName() + " Я все купил!");
                    }
                }
            }
        }
        Thread addPeople = new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                    for (int i = 1;i <= 100;) {
                    if (list20.size() >= 20) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        People people = new People(i);
                        list20.add(people);
                        people.start();
                        i++;
                    }
                }
                System.out.println("покупателей больше нет у входа");
            }
        });
        Thread seller = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                int productInCounter = 5;
                while (!list20.isEmpty() | addPeople.isAlive()) {
                    if (productInCounter > 0) {
                        try {
                            Thread.sleep(30);//время обслуживания одного клиента
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (list20) {
                            int rand = new Random().nextInt(list20.size());
                            Thread randPeople = list20.get(rand);
                            randPeople.interrupt();
                            happyList.add(randPeople.getName());
                            list20.remove(randPeople);
                            productInCounter--;
                        }
                    } else {
                        try {
                            Thread.sleep(100);//время похода на склад
                            System.out.println("я на склад");
                            tripsToWarehouse++;
                            productInCounter = 5;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        addPeople.start();
        seller.start();
        while (true) {
            if (!addPeople.isAlive() && !seller.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Список недовольных покупателей:  " + unHappyList);
                System.out.println("Колличество покупателей которые остались недовольными: " + unHappyList.size());
                System.out.println("Список довольных покупателей: " + happyList);
                System.out.println("Колличество покупателей которые остались довольными:" + happyList.size());
                System.out.println("Количество походов на склад: " + tripsToWarehouse);
                return;
            }
        }
    }
}
