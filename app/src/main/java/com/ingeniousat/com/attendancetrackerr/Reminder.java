package com.ingeniousat.com.attendancetrackerr;

/**
 * Created by Andy on 7/31/2016.
 */
public class Reminder {
    private int _id;
    private String _date;
    private String _time;
    private String _context;
    private String _name;

    public Reminder() {
    }

    public Reminder(int id, String date, String name, String time, String context) {
        this._id = id;
        this._date = date;
        this._time = time;
        this._context = context;
        this._name = name;
    }

    public Reminder( String date, String name, String time, String context) {
        this._date = date;
        this._time = time;
        this._context = context;
        this._name = name;
    }

    public void setID(int id) { this._id = id; }

    public int getID() { return this._id; }

    public void setName(String name) { this._name = name; }

    public String getName() { return this._name; }

    public void setDate(String date) { this._date = date; }

    public String getDate() { return this._date; }

    public void setTime(String time) { this._time = time; }

    public String getTime() { return this._time; }

    public void setContext(String context) { this._context = context; }

    public String getContext() { return this._context; }

}
