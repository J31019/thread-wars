package com.company;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static  void main(String[] args)  {
        int people=100;//... and we give it back to you. The people.
        Thread ct=Thread.currentThread();
        ArrayList<MyThread> thread=new ArrayList<MyThread>(20);//стэк из 20 человек внутри магазина
        ct.setName("MainThread");
        for (int i=1;i<=20;i++){
            thread.add(newThread(i));
        }
        Thread arrThreadAdd = new Thread(new Runnable() {// охранник - запускает людей в помещение магазина
            @Override
            public synchronized void run() {
                int nextThread=20;
                while (true){
                    if (thread.size()<20){
                        if (nextThread>people-1){
                            thread.trimToSize();
                            return;
                        }
                        thread.add(newThread(nextThread+1));
                        nextThread++;
                    }
                }
            }
        });
        arrThreadAdd.setName("guard");
        arrThreadAdd.start();
        SynchronousQueue<MyThread> syncQ= new SynchronousQueue<>() ;
       Thread arrThreadRemove = new Thread(new Runnable() {//иммитация работы кассира
            @Override
            public synchronized void run() {
                while (true) {
                    thread.trimToSize();
                    if (thread.size() == 0) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Random ra = new Random();
                    int p = ra.nextInt(thread.size());
                    MyThread myThreadTemp = thread.get(p);
                    if (!myThreadTemp.isAlive()) {//если поток остановлен, то он удаляется из массива потоков
                        Count.addSadness(myThreadTemp.ticket);
                        thread.remove(myThreadTemp);
                        continue;
                    }
                    try {
                        myThreadTemp.c=Condition.HAPPY;
                        if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                            return;
                        }
                            syncQ.put(thread.remove(p));
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
        Thread arrThreadLucky = new Thread(new Runnable() {//поток иммитирует окончание покупки
            @Override
            public synchronized void run() {
                while (true){
                    for (int i =1;i<6;i++){//5 товаров на полке
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                                return;
                            }
                            synchronized (syncQ) {//
                                MyThread temp = syncQ.take();
                                temp.c=Condition.HAPPY;
                                Count.countL+=1;
                                if (temp.ticket!=0){
                                    Count.addHappy(temp.ticket);
                                }
                            }
                            if (thread.size()<=0){
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!arrThreadRemove.isAlive()){
                            return;
                        }
                        if (i==5){//товаров на полке не осталось, придется опять идти на склад (((((
                            if (thread.size()<=0){
                                return;
                            }
                            try {
                                Thread.sleep(100);//поход на склад
                            } catch (InterruptedException e) {
                                continue;
                            }
                        }

                    }

                }

            }
        });
        arrThreadLucky.setName("Exit");
        arrThreadLucky.start();
        Thread showThread = new Thread(new Runnable() {//поток контралирует окончание работы магазина
            // и по окнчании выводит необходимую информацию на табло
            @Override
            public void run() {
                while (true){
                    if ((people-(Count.happy.size()+Count.unlucky.size()))==0){
                        synchronized (arrThreadRemove) {
                            System.out.println("Внимание! Вот они номера счастливых билетов");
                            System.out.println(Count.happy.toString());
                            System.out.println("Какая неудача. Возможно вам повезет в другой раз");
                            System.out.print("\n");
                            System.out.print("Количество неудачливых жетонов- ");
                            System.out.print( Count.unlucky.size()+"\n");
                            System.out.println(Count.unlucky.toString());
                            boolean br = true;
                            if (100 - (Count.unlucky.size() + Count.happy.size()) == 0) {
                            } else {
                            }

                            return;
                        }
                    }
                }
            }
        });
        showThread.setName("scoreboard");
        showThread.start();
    }
    @Deprecated
    public static int randomN(ArrayList<Thread> arr){//рандомизатор
        Random random=new Random();
        int r=0;
        while (true) {
            r = random.nextInt(100);
            if (arr.contains(r)){
                return r;
            }
        }
    }
    public synchronized static MyThread newThread (int ticket){//наполняет массив потоками(покупателями)
        MyThread tempT=new MyThread(ticket);
        tempT.start();
        return tempT;
    }
    @Deprecated
    public static boolean happy(MyThread myThread){
        //никто:
        //абсолютно никто:
        //я: создам ка я еще один метод для проверки работы программы
        if (myThread.c==Condition.HAPPY){
            return true;
        }
        return false;
    }
    @Deprecated
    public synchronized static void kill(Thread thread){//остановка потока
        Thread tt=thread;
        thread.interrupt();
    }
}
