package com.ajmalyousufza.attendanceregister.Models;

public class StudentListDataModel {

    String name;
    Boolean Present;

    public StudentListDataModel() {
    }

    public StudentListDataModel(String name, Boolean present) {
        this.name = name;
        Present = present;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPresent() {
        return Present;
    }

    public void setPresent(Boolean present) {
        Present = present;
    }
}
