package com.ingeniousat.com.attendancetrackerr;

/**
 * Created by TONMOYPC on 10/25/2017.
 */
public class Employee {
    String name,remarks,inTime,outTime,date,location,status,totaltime;

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

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    Employee(String name, String inTime, String outTime, String remarks, String date, String location, String status, String totaltime){
        this.name = name;
        this.inTime = inTime;
        this.outTime = outTime;
        this.remarks = remarks;
        this.date = date;
        this.location = location;

        this.status = status;
        this.totaltime = totaltime;

    }
}
