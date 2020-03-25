package com.company;

public class Buyers implements Runnable {

    public int ticket;
    Thread thread;

    public Buyers(int ticket){
        this.ticket = ticket;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            Seller.lostTickets.add(ticket);
        } catch (InterruptedException ignore) {
            Seller.buyers.remove(this);
        }
    }
    public void start () {
        if (thread == null) {
            thread = new Thread (this);
            thread.start ();
        }
    }
    public void interrupt(){
        thread.interrupt();
    }
}
