package com.company;

import java.util.ArrayList;

public class Count {//класс создан для подсчета всех итераций и необходимых значений
    static int countL=0;
    static  int countRemove=0;
    static int countAdd=0;
    static ArrayList<Integer> unlucky=new ArrayList<>();
    static ArrayList<Integer> happy=new ArrayList<>();
    public synchronized static void showUnlucky(){
        System.out.println("статик метод");
        System.out.println("количество несчастных - "+ unlucky.size());
    }
    public synchronized static void addHappy(Integer e){
        happy.add(e);
    }
    public synchronized static void addSadness(Integer e){
        unlucky.add(e);
    }
}
