package com.ajmalyousufza.attendanceregister.Models;

public class ViewAll_Model {

    String name;
    String attendance;

    public ViewAll_Model() {
    }

    public ViewAll_Model(String name, String attendance) {
        this.name = name;
        this.attendance = attendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }
}
