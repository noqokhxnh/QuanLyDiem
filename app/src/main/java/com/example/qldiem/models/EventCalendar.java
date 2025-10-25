package com.example.qldiem.models;

/**
 * Model class cho bảng EventCalendar (Lịch sự kiện)
 */
public class EventCalendar {
    private int id;
    private String date; // Format: yyyy-MM-dd
    private String event;

    public EventCalendar() {
    }

    public EventCalendar(int id, String date, String event) {
        this.id = id;
        this.date = date;
        this.event = event;
    }

    public EventCalendar(String date, String event) {
        this.date = date;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return date + ": " + event;
    }
}
