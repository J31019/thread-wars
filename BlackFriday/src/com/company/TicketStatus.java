package com.company;

public enum TicketStatus {
    HAPPY("Счастливый"),
    UNHAPPY("Несчастливый");

    String status;

    TicketStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
