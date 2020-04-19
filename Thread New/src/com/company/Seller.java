package com.company;

import java.util.Random;
import static com.company.Main.littleTurn;

public class Seller extends Thread {
    @Override
    public synchronized void run() {
        Thread Seller = Thread.currentThread();
        do {
            for (int j = 0; j < 5; j++) {
                try {
                    Seller.wait(0,1);  // wait для того что бы дать время создаться очереди littleTurn, так как стоят условия по размеру очереди
                    Random random = new Random();       // без wait код переодически работает не корректно, Seller не запускается ( примерно 1 из 5, 1 из 10 раз Seller без wait не запускается )
                    if(littleTurn.size() > 1){
                    int rNumber = random.nextInt(littleTurn.size() - 1);
                    Seller.sleep(30);
                    System.out.println("покупатель номер " + littleTurn.get(rNumber).getName() + " получил свой товар клас Seller");
                    littleTurn.get(rNumber).interrupt();
                    littleTurn.remove(rNumber); }
                    else{
                        if(littleTurn.size() == 1){
                        Seller.sleep(30);
                        Thread lastBuyer = littleTurn.get(0);
                        System.out.println("покупатель номер " + lastBuyer.getName() + " получил свой товар клас Seller");
                        lastBuyer.interrupt();
                        littleTurn.remove(0);}
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!littleTurn.isEmpty()){
                System.out.println("Я ухожу на склад, скоро вернусь!");
                try {
                    Seller.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
        }
       while (!littleTurn.isEmpty());
    }
}