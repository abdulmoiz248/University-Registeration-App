package com.example.assignment3;

import java.io.Serializable;

public class Employee implements Serializable{
    String name;
    String grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getEmployeeDetails() {
        return "Name= "+name+" Grade= "+grade;
    }
}
