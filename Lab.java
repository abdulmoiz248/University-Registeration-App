package com.example.assignment3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Lab implements Serializable {
    int id;
    String name;
    LabStaff incharge=new LabStaff();
    Boolean hasprojector;
    ObservableList<Computer> computers= FXCollections.observableArrayList();
    public void addcomputer(   String systemid, String systemname, String systemspeed, String ramsize, String harddisksize ,String lcdmodel )
    {
        Computer computer=new Computer();
        computer.harddisksize=harddisksize;
        computer.systemid=systemid;
        computer.lcdmodel=lcdmodel;
        computer.systemname=systemname;
        computer.ramsize=ramsize;
        computer.systemspeed=systemspeed;
        computers.add(computer);
    }
    public void copy(Lab l)
    {
        this.name= l.name;
        this.incharge.staffDetails.name=l.incharge.staffDetails.name;
        this.incharge.staffDetails.grade=l.incharge.staffDetails.grade;
    }
    public void removecomputer( int computer)
    {
        computers.remove(computer);
    }

    @Override
    public String toString() {
        return name;
    }
}
