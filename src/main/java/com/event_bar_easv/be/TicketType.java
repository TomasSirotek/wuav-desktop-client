package com.event_bar_easv.be;

public class TicketType {


    private int id;
    private String type;

    private String benefit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }


    @Override
    public String toString() {
        return "TicketType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", benefit='" + benefit + '\'' +
                '}';
    }
}
