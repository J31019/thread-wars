package com.company;

public class MyThread extends Thread implements Runnable {
    public int Id=0;
    public int ticket=1;
    public Condition c = Condition.UNHAPPY;
    public MyThread(int ticket){
       this.ticket=ticket;
    }
    @Override
    public synchronized void run() {
        super.run();
        try {
            killSwitch();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void setId(int id){
            this.Id = id;
    }

    @Override
    public long getId() {
        return (Integer) this.Id;
    }
    public Integer getTicket(){
        return  this.ticket;
    }
    private synchronized void killSwitch() throws InterruptedException {
        this.sleep(1000);//создаем 1000мс задержку перед остановкой потока
        if (this.c==Condition.HAPPY){
            return;
        }
        Thread.currentThread().interrupt();
    }



}
