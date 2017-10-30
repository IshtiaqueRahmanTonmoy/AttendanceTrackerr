package com.ingeniousat.com.attendancetrackerr;

/**
 * Created by TONMOYPC on 10/25/2017.
 */
public class Employee {
    String name,remarks,inTime,outTime,date,status;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String name) {
        this.remarks = remarks;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    Employee(String name, String inTime, String outTime, String remarks, String date, String status){
        this.name = name;
        this.inTime = inTime;
        this.outTime = outTime;
        this.remarks = remarks;
        this.date = date;
        this.status = status;

    }
}
