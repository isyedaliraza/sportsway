package com.example.android.sportsway.Model;

public class Event {
    private int _id;
    private String event_name;
    private String event_location;
    private String event_start_time;
    private int event_tickets;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_start_time() {
        return event_start_time;
    }

    public void setEvent_start_time(String event_start_time) {
        this.event_start_time = event_start_time;
    }

    public int getEvent_tickets() {
        return event_tickets;
    }

    public void setEvent_tickets(int event_tickets) {
        this.event_tickets = event_tickets;
    }
}
