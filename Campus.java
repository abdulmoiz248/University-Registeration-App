package com.example.assignment3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class Campus implements Serializable {
    int id;
    static int idgiver=0;
    String name;
    String address;
    Director director=new Director();
   // ArrayList<Department> departments = new ArrayList<>();
   ObservableList<Department> departments= FXCollections.observableArrayList();
   public void copy(Campus c)
   {
       this.name=c.name;
       this.address= c.address;
       this.director.DirectorDetails.name=c.director.DirectorDetails.name;
       this.director.DirectorDetails.grade=c.director.DirectorDetails.grade;
   }

    public void adddepartment(String name,String hodname,String grade)
    {
        Department d=new Department();
        d.name=name;
        d.hod.hodDetails.grade=grade;
        d.hod.hodDetails.name=name;
        d.id=idgiver;
        departments.add(d);
        idgiver++;

    }
    public void removedepartment(int index)
    {
        departments.remove(index);
    }
    public Department getDepartment(String Name){
        for(Department d: departments)
            if(d.name.equals(Name))
                return d;
        return null;
    }

    @Override
    public String toString() {
        System.out.println(name);
       return name;
    }
}