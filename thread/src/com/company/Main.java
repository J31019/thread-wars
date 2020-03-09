package com.company;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static  void main(String[] args)  {
        int lucky=0;
        int people=100;

        Thread ct=Thread.currentThread();
        ArrayList<Integer> unLuckyMan=new ArrayList<>();
        ArrayList<Integer> luckyMan=new ArrayList<>();
        //ArrayList<Integer> ticketsN=new ArrayList<>();
        ArrayList<Integer> ticketsInside=new ArrayList<>();
        ArrayList<MyThread> thread=new ArrayList<MyThread>(20);
        ct.setName("MainThread");
        for (int i=1;i<=20;i++){
            thread.add(newThread(i));
            //System.out.println(thread.size());
        }
        //System.out.println(thread.toString());
        Thread arrThreadAdd = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                //System.out.println(thread.size());
                int nextThread=20;
                while (true){
                    //System.out.println("пробуем найти тут");
                    //System.out.println(thread.size());
                    if (thread.size()<20){
                        if (nextThread>people-1){
                            thread.trimToSize();
                            //System.out.println(thread.size()+"ARRRRGGGG");
                            return;
                        }
                        thread.add(newThread(nextThread+1));
                        //System.out.println("Проверка на добавление");
                        nextThread++;
                    }
                }
            }
        });
        arrThreadAdd.setName("guard");
        arrThreadAdd.start();
        SynchronousQueue<MyThread> syncQ= new SynchronousQueue<>() ;
       Thread arrThreadRemove = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (true) {

                    thread.trimToSize();
                    if (thread.size() == 0) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Random ra = new Random();
                    //System.out.println("Размер - " + thread.size());
                    int p = ra.nextInt(thread.size());
                    MyThread myThreadTemp = thread.get(p);
                    if (!myThreadTemp.isAlive()) {//если поток остановлен, то он удаляется из массива потоков
                        Count.addSadness(myThreadTemp.ticket);
                        thread.remove(myThreadTemp);
                        //System.out.println("Точка смерти");
                        continue;
                    }
                    try {
                        //myThreadTemp.notify();
                        myThreadTemp.c=Condition.HAPPY;
                        if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                            return;
                        }
                        //Thread.currentThread().sleep(1);
                        //synchronized (syncQ) {
                            syncQ.put(thread.remove(p));
                        //}
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (ArrayIndexOutOfBoundsException ex){
                        System.err.println("OOps");
                        continue;
                    }
                }
            }



        });

        arrThreadRemove.setName("cashier");
        arrThreadRemove.start();

        Thread arrThreadLucky = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while (true){
                    for (int i =1;i<6;i++){
                        /*if (thread.size()<=0){
                            Thread.currentThread().interrupt();
                            return;
                        }*/
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                                return;
                            }
                            //Thread.currentThread().sleep(3);
                            synchronized (syncQ) {
                                MyThread temp = syncQ.take();
                                temp.c=Condition.HAPPY;
                                if (temp.ticket!=0){
                                    Count.addHappy(temp.ticket);
                            }
                            //System.out.print("\nВот такой билет - "+temp.ticket);

                            Count.countL+=1;
                            }
                            if (thread.size()<=0){
                                return;
                            }
                            //System.out.println(luckyMan.toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!arrThreadRemove.isAlive()){
                            return;
                        }
                        if (i==5){
                            if (thread.size()<=0){
                                return;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                //System.err.println("Не убивай меня, я просто сплю");
                                continue;
                            }
                        }

                    }

                }

            }
        });
        arrThreadLucky.setName("Exit");
        arrThreadLucky.start();
        Thread showThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                        synchronized (arrThreadRemove) {
                            System.out.println("Внимание! Вот они номера счастливых билетов");
                            System.out.println(Count.happy.toString());
                            //System.out.println("Размер массива удачников - " + Count.happy.size());
                            System.out.println("Какая неудача. Возможно вам повезет в другой раз");
                            System.out.println("\n");
                            System.out.print("Количество неудачливых жетонов- ");
                            System.out.print( Count.unlucky.size()+"\n");
                            //Count.unlucky.trimToSize();
                            System.out.println(Count.unlucky.toString());
                            //System.out.println("Размер массива неудачников - " + Count.unlucky.size());
                            boolean br = true;
                            if (100 - (Count.unlucky.size() + Count.happy.size()) == 0) {
                                //System.out.println("Подсчет точный");
                            } else {
                                //System.out.println("Подсчет неточный - " + (Count.unlucky.size() - Count.happy.size()));
                            }
                            //arrThreadLucky.interrupt();
                            //arrThreadAdd.interrupt();
                            //arrThreadRemove.interrupt();
                            return;
                        }
                    }
                }
            }
        });
        showThread.setName("shower");
        showThread.start();
    }
    @Deprecated
    public static int randomN(ArrayList<Thread> arr){
        Random random=new Random();
        int r=0;
        while (true) {
            r = random.nextInt(100);
            if (arr.contains(r)){
                return r;
            }
        }
    }
    public synchronized static MyThread newThread (int ticket){
        MyThread tempT=new MyThread(ticket);
        tempT.start();
        return tempT;
    }
    public static boolean happy(MyThread myThread){
        if (myThread.c==Condition.HAPPY){
            return true;
        }
        return false;
    }
    public synchronized static void kill(Thread thread){
        Thread tt=thread;
        thread.interrupt();
    }
}
