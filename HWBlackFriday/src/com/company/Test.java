package com.company;

import javax.xml.namespace.QName;
import java.util.PriorityQueue;
import java.util.Queue;

public class Test {
    public static void main(String[] args) {
        Queue<Integer> queue1 = new PriorityQueue<>();
        Queue<Integer> queue2 = new PriorityQueue<>();

        for (int i = 0; i < 10; i++) {
            queue1.add(i);
        }

        for (int i = 0; i < 10 ; i++) {
            queue2.add(queue1.poll());
        }

        System.out.println("Queue 1 " + queue1.size());
        System.out.println("Queue 2 " + queue2.size());

    }
}
