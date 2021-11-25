package com.example.alertschedule;

public class dataForfireBase {

    String starttime,endtime,mode;

    public dataForfireBase() {

    }


    public dataForfireBase(String starttime, String endtime, String mode) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.mode = mode;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
