package com.example.assignment3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Department implements Serializable {
    int id=0;
    static int idgiver=0;
    HOD hod=new HOD();
    String name;
      ObservableList<Lab> labs = FXCollections.observableArrayList();
      public void copy(Department d)
      {
          this.name=d.name;
          this.hod.hodDetails.grade=d.hod.hodDetails.grade;
          this.hod.hodDetails.name=d.hod.hodDetails.name;
      }
    public void addLab(String lname,String names,String grade,Boolean idk){
        Lab lab=new Lab();
        lab.incharge.staffDetails.grade=grade;
        lab.incharge.staffDetails.name=names;
        lab.name=lname;
        lab.hasprojector=idk;
        lab.id=idgiver;
        labs.add(lab);
        idgiver++;
    }
    public void removeLab(int Lab){

        labs.remove(Lab);
    }
    public Lab getLab(String Name){
        for(Lab l: labs)
            if(l.name.equals(Name))
                return l;
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
