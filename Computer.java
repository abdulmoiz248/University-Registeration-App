package com.example.assignment3;

import java.io.Serializable;

public class Computer implements Serializable {
    String systemid;
    String systemname;
    String systemspeed;
    String ramsize;
    String harddisksize;
    String lcdmodel;

    @Override
    public String toString() {
        return systemid +". "+systemname;
    }
    public void copy(Computer computer)
    {
        this.systemspeed=computer.systemspeed;
        this.harddisksize=computer.harddisksize;
        this.lcdmodel=computer.lcdmodel;
        this.ramsize=computer.ramsize;
        this.systemname=computer.systemname;
        this.systemid=computer.systemid;
    }

    public String getComputerDetails() {
        return systemid;
    }
}
