package com.example.assignment3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class University implements Serializable {
    static int idgiver=0;

    Register register=new Register();
    ObservableList<Campus> campuses = FXCollections.observableArrayList();

    @Override
    public String toString() {
        return register.uniname;
    }
    public void copy(University u)
    {
        register.uniname=u.register.uniname;
        register.pass= u.register.pass;
        // copyall(c);
    }

    public void copyall(ObservableList<Campus> c)
    {
              for (Campus c1: c)
              {
                  addCampus(c1.name, c1.address, c1.director.DirectorDetails.name,c1.director.DirectorDetails.grade);
                  for (Department d: c1.departments)
                  {
                      c1.adddepartment(d.name,d.hod.hodDetails.name,d.hod.hodDetails.grade);
                      for (Lab l: d.labs)
                      {
                          d.addLab(l.name, l.incharge.staffDetails.name,l.incharge.staffDetails.grade,true);
                          for (Computer c2: l.computers)
                          {
                              l.addcomputer(c2.systemid, c2.systemname, c2.systemspeed, c2.ramsize, c2.harddisksize, c2.lcdmodel);
                          }
                      }
                  }
              }
    }


    public void addCampus(String name, String add, String dname, String grade){
        Campus campus=new Campus();
        campus.name=name;
        campus.address=add;
        campus.director.DirectorDetails.name=dname;
        campus.director.DirectorDetails.grade=grade;
        campus.id=idgiver;
        campuses.add(campus);
        idgiver++;
        System.out.println(campus.name);
    }
    public void removeCampus(int index){

        campuses.remove(index);
    }
    public Campus getCampus(String campusName){
        for(Campus c:campuses){
            if(c.name.equals(campusName))
                return c;
        }
        return null;
    }


}