package com.company;

public class Main extends Thread {

    public static void main(String[] args) throws InterruptedException {
        Seller seller = new Seller();
        seller.seller();
    }
}
