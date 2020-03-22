package com.company;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Human {
    private int id;
    private String status = "LOSER";
    private static BlockingQueue queue = new ArrayBlockingQueue(20);
    private static Map<Integer, Object> map = new HashMap<>();

    Human (int id, String status) {
        this.id = id;
        this.status = status;
    }

    public Human() {}

    void produce() throws InterruptedException {
        List<Human> generated = new ArrayList<>();

        for(int i = 1; i <= 100; i++)
            generated.add(new Human(i, status));

        Collections.shuffle(generated);

        for (Human list : generated)
            queue.put(list);
    }

    void consumer() throws InterruptedException {

        int product = 5;

        for (int i = 1; i <= 100; i++) {

            if (product > 0) {
                Thread.sleep(30);
                Human people = (Human) queue.take();
                //map.put(i, queue.take());
                people.status = "LUCKY";
                map.put(i, people);
                System.out.println("Queue size is " + queue.size());

                product -= 1;
            }

            if (product == 0) {
                Thread.sleep(100);
                product = 5;
            }
        }
    }

    void Info() {
        for(Map.Entry<Integer, Object> item : map.entrySet())
            System.out.printf("Порядковый номер: %d %20s \n", item.getKey(), item.getValue());

        System.out.printf("Количество счастливых билетов: %d\n", map.size());
        System.out.printf("Количество несчастливых билетов: %d", map.size() - 100);
    }

    @Override
    public String toString() {
        return "Human id=" + id + " " + status;
    }
}

