package com.company;

public class Buyers {
    static int ticket = -1;
    TicketStatus status;

    public Buyers(){
        ticket++;
        this.status = TicketStatus.UNHAPPY;
    }
}
