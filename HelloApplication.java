package com.example.assignment3;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;


public class HelloApplication extends Application {
    private static final String URL = "jdbc:mysql://localhost:3306/last2";
    private static final String USER = "root";

    private static final String PASSWORD = "punjabclg";
    static University[] university =new University[50];
    static int unicount=0;
    int position=0;
    String namecheck=null;
    public static void addcomputer(   int systemid, String systemname, String systemspeed, String ramsize, String harddisksize ,String lcdmodel, ObservableList<Computer> computerList  )
    {
        Computer computer = new Computer();
        computer.systemname = systemname;
        computer.ramsize = ramsize;
        computer.harddisksize = harddisksize;
        computer.lcdmodel = lcdmodel;
        computer.systemid = String.valueOf(systemid);
        computer.systemspeed = systemspeed;
        // Add the computer to the lab
        computerList.add(computer);
    }
    private static void createTables() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            // Create the "uni" table
            String uniTableQuery = "CREATE TABLE IF NOT EXISTS uni (" +
                    "uniId INT PRIMARY KEY," +"uniName VARCHAR(255),"+"unipass VARCHAR(255)"+
                    ")";
            statement.executeUpdate(uniTableQuery);

            // Create the "campus" table
            String campusTableQuery = "CREATE TABLE IF NOT EXISTS campus (" +
                    "campusId INT PRIMARY KEY," +
                    "campusName VARCHAR(255)," + "campusaddress VARCHAR(255),"+
                    "campusdirector VARCHAR(255),"+"dgrade VARCHAR(255),"+
                    "uniId INT," +
                    "FOREIGN KEY (uniId) REFERENCES uni(uniId)" +
                    ")";
            statement.executeUpdate(campusTableQuery);

            // Create the "department" table
            String departmentTableQuery = "CREATE TABLE IF NOT EXISTS department (" +
                    "departmentId INT PRIMARY KEY," +
                    "departmentName VARCHAR(255)," +"departmenthod VARCHAR(255),"+"hgrade VARCHAR(255),"+
                    "campusId INT," +
                    "FOREIGN KEY (campusId) REFERENCES campus(campusId)" +
                    ")";
            statement.executeUpdate(departmentTableQuery);

            // Create the "lab" table
            String labTableQuery = "CREATE TABLE IF NOT EXISTS lab (" +
                    "labId INT PRIMARY KEY," +
                    "labName VARCHAR(255)," + "labstaff VARCHAR(255),"+"lsgrade VARCHAR(255),"+
                    "departmentId INT," +
                    "FOREIGN KEY (departmentId) REFERENCES department(departmentId)" +
                    ")";
            statement.executeUpdate(labTableQuery);

            // Create the "computer" table
            String computerTableQuery = "CREATE TABLE IF NOT EXISTS computer (" +
                    "computerId INT PRIMARY KEY," +
                    "computerName VARCHAR(255)," +"ram VARCHAR(255),"+ "hard VARCHAR(255),"+"lcdmodel VARCHAR(255),"+"speed VARCHAR(255),"+
                    "labId INT," +
                    "FOREIGN KEY (labId) REFERENCES lab(labId)" +
                    ")";
            statement.executeUpdate(computerTableQuery);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addLab(String lname, String names, String grade, Boolean idk, ObservableList<Lab> labs, ObservableList<Computer> computerList){
        Lab lab=new Lab();
        lab.incharge.staffDetails.grade=grade;
        lab.incharge.staffDetails.name=names;
        lab.name=lname;
        lab.hasprojector=idk;
        for(Computer c: computerList)
        lab.computers.addAll(c);
        labs.add(lab);

    }
    public void adddepartment(String name, String hodname, String grade, ObservableList<Department> departments, ObservableList<Lab> labList)
    {
        Department d=new Department();
        d.name=name;
        d.hod.hodDetails.grade=grade;
        d.hod.hodDetails.name=name;
        for(Lab l:labList)
        d.labs.addAll(l);
        departments.add(d);
    }
    public void addCampus(String name, String add, String dname, String grade, ObservableList<Campus> campuses, ObservableList<Department> departmentList){
        Campus campus=new Campus();
        campus.name=name;
        campus.address=add;
        campus.director.DirectorDetails.name=dname;
        campus.director.DirectorDetails.grade=grade;
        for (Department d: departmentList)
       campus.departments.addAll(d);
        campuses.add(campus);

    }
    public void adduni(String name, String pass,ObservableList<Campus> campuses)
    {
        university[unicount].register.uniname=name;
        university[unicount].register.pass=pass;
        for(Campus c: campuses)
        university[unicount].campuses.add(c);
                System.out.println(name+""+unicount);
        unicount++;
    }
    public void fetchDataFromDatabase() {

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Retrieve data from the "uni" table
            Statement uniStatement = connection.createStatement();
            ResultSet uniResultSet = uniStatement.executeQuery("SELECT * FROM uni");
            while (uniResultSet.next()) {
                int uniId = uniResultSet.getInt("uniId");
                String uniName = uniResultSet.getString("uniName");
                String unipass1 = uniResultSet.getString("unipass");

              //  System.out.println(unipass1+"pass");
                ObservableList<Campus> campusList = FXCollections.observableArrayList();

                // Retrieve data from the "campus" table for the current university
                Statement campusStatement = connection.createStatement();
                ResultSet campusResultSet = campusStatement.executeQuery("SELECT * FROM campus WHERE uniId = " + uniId);
                while (campusResultSet.next()) {
                   {
                        int campusId = campusResultSet.getInt("campusId");
                        String campusName = campusResultSet.getString("campusName");
                        String campusdirector = campusResultSet.getString("campusdirector");
                        String campusaddress = campusResultSet.getString("campusaddress");
                        String dgrade = campusResultSet.getString("dgrade");
                        ObservableList<Department> departmentList = FXCollections.observableArrayList();

                        // Retrieve data from the "department" table for the current campus
                        Statement departmentStatement = connection.createStatement();
                        ResultSet departmentResultSet = departmentStatement.executeQuery("SELECT * FROM department WHERE campusId = " + campusId);
                        while (departmentResultSet.next()) {
                           {
                                int departmentId = departmentResultSet.getInt("departmentId");
                                String departmentName = departmentResultSet.getString("departmentName");
                                String departmenthod = departmentResultSet.getString("departmenthod");
                                String hgrade = departmentResultSet.getString("hgrade");
                                ObservableList<Lab> labList = FXCollections.observableArrayList();

                                // Retrieve data from the "lab" table for the current department
                                Statement labStatement = connection.createStatement();
                                ResultSet labResultSet = labStatement.executeQuery("SELECT * FROM lab WHERE departmentId = " + departmentId);
                                while (labResultSet.next()) {
                                  {
                                        int labId = labResultSet.getInt("labId");
                                        String labName = labResultSet.getString("labName");
                                        String labstaff = labResultSet.getString("labstaff");
                                        String lsgrade = labResultSet.getString("lsgrade");
                                        ObservableList<Computer> computerList = FXCollections.observableArrayList();

                                        // Retrieve data from the "computer" table for the current lab
                                        Statement computerStatement = connection.createStatement();
                                        ResultSet computerResultSet = computerStatement.executeQuery("SELECT * FROM computer WHERE labId = " + labId);
                                        while (computerResultSet.next()) {
                                          {
                                                int computerId = computerResultSet.getInt("computerId");
                                                String computerName = computerResultSet.getString("computerName");
                                                String ram = computerResultSet.getString("ram");
                                                String hard = computerResultSet.getString("hard");
                                                String lcdmodel = computerResultSet.getString("lcdmodel");
                                                String speed = computerResultSet.getString("speed");
                                           addcomputer(computerId,computerName,speed,ram,hard,lcdmodel,computerList);


                                            }//comp if
                                        }

                                        // Create the lab with its computers

                                        computerResultSet.close();
                                        computerStatement.close();
                                      addLab(labName,labstaff,lsgrade,true,labList,computerList);
                                    }//lab if
                                }
                                // Create the department with its labs

                                labResultSet.close();
                                labStatement.close();
                               adddepartment(departmentName,departmenthod,hgrade,departmentList,labList);
                            }// dep if
                        }

                        // Create the campus with its departments

                       addCampus(campusName,campusaddress,campusdirector,dgrade,campusList,departmentList);
                        departmentResultSet.close();
                        departmentStatement.close();
                    } //campus if
                    }


                adduni(uniName,unipass1,campusList);
                campusResultSet.close();
                campusStatement.close();
            }

            uniResultSet.close();
            uniStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void storeDataToDatabase(University uni) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Store or update university data
            Statement uniStatement = connection.createStatement();
            // Check if the university already exists in the database
            String checkQuery = "SELECT COUNT(*) FROM uni WHERE uniId = " + position;
            ResultSet resultSet = uniStatement.executeQuery(checkQuery);
            resultSet.next();
            int count = resultSet.getInt(1);
            // Prepare the SQL statement based on the university's existence
            String sqlQuery;
            if (count > 0) {
                // University exists, update the record
                sqlQuery = "UPDATE uni SET uniName = '" + uni.register.uniname + "', unipass = '" + uni.register.pass + "' WHERE uniId = " + position;
            } else {
                // University doesn't exist, insert a new record
                sqlQuery = "INSERT INTO uni (uniId, uniName, unipass) VALUES (" + position + ", '" + uni.register.uniname + "', '" + uni.register.pass + "')";
            }
            // Execute the SQL statement
            uniStatement.executeUpdate(sqlQuery);

            // Store or update campus data for the current university
            Statement campusStatement = connection.createStatement();
            for (Campus campus : uni.campuses) {
                // Check if the campus already exists in the database
                int pos = 0;
                try {
                    checkQuery = "SELECT COUNT(*) FROM campus WHERE campusId = " + campus.id;

                    resultSet = campusStatement.executeQuery(checkQuery);
                    resultSet.next();
                    count = resultSet.getInt(1);
//ye error asakta ha

                    // Prepare the SQL statement based on the campus's existence
                    if (count > 0) {
                        // Campus exists, update the record
                        sqlQuery = "UPDATE campus SET campusName = '" + campus.name + "', campusdirector = '" + campus.director.DirectorDetails.name + "', campusaddress = '" + campus.address + "', dgrade = '" +
                                campus.director.DirectorDetails.grade + "', uniId='" + position + "' WHERE campusId = " + campus.id;

                    } else {
                        // Campus doesn't exist, insert a new record
                        sqlQuery = "INSERT INTO campus (campusId, campusName, campusdirector, campusaddress, dgrade, uniId) VALUES (" + campus.id + ", '" + campus.name + "', '" + campus.director.DirectorDetails.name + "', '" + campus.address + "', '" + campus.director.DirectorDetails.grade + "', '" + position + "')";
                    }

                    // Execute the SQL statement
                    campusStatement.executeUpdate(sqlQuery);

                } catch (Exception e) {

                }

                // Store or update department data for the current campus
                Statement deptStatement = connection.createStatement();
                for (Department dept : campus.departments) {
                    try {


                        checkQuery = "SELECT COUNT(*) FROM department WHERE departmentId = " + dept.id;
                        resultSet = deptStatement.executeQuery(checkQuery);
                        resultSet.next();
                        count = resultSet.getInt(1);

                        if (count > 0) {
                            // Department exists in the database, perform update

                            sqlQuery = "UPDATE department SET departmentName='" + dept.name + "', departmentId='" + dept.id + "', departmenthod='" +
                                    dept.hod.hodDetails.name + "', hgrade='" + dept.hod.hodDetails.grade + "', campusId=" + campus.id;

                        } else {
                            // Department does not exist in the database, perform insertion
                            sqlQuery = "INSERT INTO department (departmentId, departmentName, departmenthod, hgrade, campusId) VALUES ('" + dept.id + "', '" + dept.name + "', '" + dept.hod.hodDetails.name + "','" +
                                    dept.hod.hodDetails.grade + "','" + campus.id + "');";
                        }
                        deptStatement.executeUpdate(sqlQuery);

                    } catch (Exception e) {

                    }
                    Statement labStatement = connection.createStatement();

                    for (Lab lab : dept.labs) {
                        try {

                            checkQuery = "SELECT COUNT(*) FROM lab WHERE labId = " + lab.id;
                            resultSet = labStatement.executeQuery(checkQuery);
                            resultSet.next();
                            count = resultSet.getInt(1);

                            if (count > 0) {
                                sqlQuery = "UPDATE lab SET labName='" + lab.name + "', labId='" + lab.id + "', labstaff='" +
                                        lab.incharge.staffDetails.name + "', lsgrade='" + lab.incharge.staffDetails.grade +
                                        "', departmentId='" + dept.id + "'";
                            } else {
                                sqlQuery = "INSERT INTO lab (labId, labName, labstaff, lsgrade, departmentId) VALUES (" + lab.id + ", '" +
                                        lab.name + "', '" + lab.incharge.staffDetails.name + "', '" + lab.incharge.staffDetails.grade +
                                        "','" + dept.id + "')";
                            }
                            labStatement.executeUpdate(sqlQuery);

                        } catch (Exception e) {

                        }
                        Statement compStatement = connection.createStatement();
                        for (Computer comp : lab.computers) {
                            try {
                                checkQuery = "SELECT COUNT(*) FROM computer WHERE computerId = " + comp.systemid;
                                resultSet = labStatement.executeQuery(checkQuery);
                                resultSet.next();
                                count = resultSet.getInt(1);

                                if (count > 0) {
                                    sqlQuery = "UPDATE computer SET computerName='" + comp.systemname + "', computerId='" + comp.systemid +
                                            "', ram=" + comp.ramsize + ", hard='" + comp.harddisksize + "', lcdmodel='" + comp.lcdmodel +
                                            "', speed='" + comp.systemspeed + "', labId='" + lab.id + "'";
                                } else {
                                    sqlQuery = "INSERT INTO computer (computerId, computerName, ram, hard, lcdmodel, speed, labId) VALUES (" +
                                            comp.systemid + ", '" + comp.systemname + "', " + comp.ramsize + ", '" + comp.harddisksize +
                                            "', '" + comp.lcdmodel + "', '" + comp.systemspeed + "', '" + lab.id + "')";
                                }
                                compStatement.executeUpdate(sqlQuery);
// Close Statement for computer
                                compStatement.close();
                            } catch (Exception e) {

                            }

                        }

                        // Close Statement for lab
                        labStatement.close();
                    }

                    // Close Statement for department
                    deptStatement.close();
                }

                // Close ResultSet for uni
                resultSet.close();


                // Close Statement and connection
                uniStatement.close();
                connection.close();
                System.out.println("Data stored or updated successfully in the database.");


            }
        } catch (Exception e) {

        }
    }



    @Override
    public void start(Stage stage) throws IOException {

        for(int i=0;i<university.length;i++) {
            university[i] = new University();
        }
        createTables();
        fetchDataFromDatabase();
        GridPane g=new GridPane();
        Scene s=new Scene(g,700,700);
        GridPane g1=new GridPane();
        Scene s1=new Scene(g1,700,700);
        GridPane g2=new GridPane();
        Scene s2=new Scene(g2,700,700);
        GridPane g3=new GridPane();
        Scene s3=new Scene(g3,700,700);
        GridPane g4=new GridPane();
        Scene s4=new Scene(g4,700,700);
        GridPane g5=new GridPane();
        Scene s5=new Scene(g5,700,700);
        GridPane g6=new GridPane();
        Scene s6=new Scene(g6,700,700);
        stage.setScene(s1);
        GridPane g7=new GridPane();
        Scene s7=new Scene(g7,700,700);
        g7.setVgap(10);
        g7.setHgap(10);
        g7.setAlignment(Pos.CENTER);
        GridPane g8=new GridPane();
        Scene s8=new Scene(g8,700,700);
        g8.setVgap(10);
        g8.setHgap(10);
        g8.setAlignment(Pos.CENTER);
        GridPane g9=new GridPane();
        Scene s9=new Scene(g9,700,700);
        g9.setVgap(10);
        g9.setHgap(10);
        g9.setAlignment(Pos.CENTER);
        GridPane g10=new GridPane();
        Scene s10=new Scene(g10,700,700);
        g10.setVgap(10);
        g10.setHgap(10);
        g10.setAlignment(Pos.CENTER);
        GridPane g11=new GridPane();
        Scene s11=new Scene(g11,700,700);
        g11.setVgap(10);
        g11.setHgap(10);
        g11.setAlignment(Pos.CENTER);
        GridPane g12=new GridPane();
        Scene s12=new Scene(g12,700,700);
        g12.setVgap(10);
        g12.setHgap(10);
        g12.setAlignment(Pos.CENTER);
        GridPane g13=new GridPane();
        Scene s13=new Scene(g13,700,700);
        g13.setVgap(10);
        g13.setHgap(10);
        g13.setAlignment(Pos.CENTER);
        GridPane g14=new GridPane();
        Scene s14=new Scene(g14,700,700);
        g14.setVgap(10);
        g14.setHgap(10);
        g14.setAlignment(Pos.CENTER);
        GridPane g15=new GridPane();
        Scene s15=new Scene(g15,700,700);
        g15.setVgap(10);
        g15.setHgap(10);
        g15.setAlignment(Pos.CENTER);
        GridPane g16=new GridPane();
        Scene s16=new Scene(g16,700,700);
        g16.setVgap(10);
        g16.setHgap(10);
        g16.setAlignment(Pos.CENTER);
        GridPane g17=new GridPane();
        Scene s17=new Scene(g17,700,700);
        g17.setVgap(10);
        g17.setHgap(10);
        g17.setAlignment(Pos.CENTER);
        GridPane g18=new GridPane();
        Scene s18=new Scene(g18,700,700);
        g18.setVgap(10);
        g18.setHgap(10);
        g18.setAlignment(Pos.CENTER);
        GridPane g19=new GridPane();
        Scene s19=new Scene(g19,700,700);
        g19.setVgap(10);
        g19.setHgap(10);
        g19.setAlignment(Pos.CENTER);
        Font font=new Font(40);
        Label front=new Label("University ");
        Label front1=new Label("Registeration ");
        Label front2=new Label("App");

        front.setFont(font);
        front1.setFont(font);
        front2.setFont(font);
        Button log=new Button("Login");
        Button reg=new Button("Register");
        Button ret=new Button("Exit");
        ret.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        g1.add(front,3,2);
        g1.add(front1,4,2);
        g1.add(front2,5,2);
      g1.add(ret,4,5);
       g1.add(log,4,4);
        g1.add(reg,4,3);

        g1.setAlignment(Pos.CENTER);
        g1.setVgap(20);
        reg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s2);

            }
        });
        log.setOnAction(e -> stage.setScene(s));
        Label uniname=new Label("Enter Uni Name ");
        Label unipass=new Label("Enter Password ");
        TextField uninamet=new TextField();
        PasswordField unipasst=new PasswordField();
        Button saveuni=new Button("Save");
        Button canceluni=new Button("Cancel");
        Label q=new Label("Register");
        q.setFont(font);
        g2.add(q,1,0);
        g2.add(uniname,0,3);
        g2.add(uninamet,1,3);
        g2.add(unipass,0,4);
        g2.add(unipasst,1,4);
        HBox SC=new HBox(saveuni,canceluni);
        g2.add(SC,1,5);
        g2.setHgap(10);
        g2.setVgap(10);
        g2.setAlignment(Pos.CENTER);
        canceluni.setOnAction(e -> stage.setScene(s1));
        saveuni.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
             if(uninamet.getText()!=null)
               if( unipasst.getText()!=null)
             {
                 university[unicount].register.uniname = uninamet.getText();
                 university[unicount].register.pass = unipasst.getText();
                // fileReader.writeToFile(f,university[unicount].register.uniname+" %"+university[unicount].register.pass);

                 unicount++;
                 uninamet.clear();
                 unipasst.clear();
                 stage.setScene(s);

             }
            }
        });
        Button b1=new Button("Add Campus ");
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s4);
            }
        });
        Label camname=new Label("Campus Name ");
        TextField camnamet=new TextField();
        Label campadd=new Label("Address ");
        TextField campaddt=new TextField();
        Label dirname=new Label("Director Name ");
        TextField dirnamet=new TextField();
        Label dirgrade=new Label("Grade ");
        TextField dirgradete=new TextField();
        Label campshead=new Label("Add Campus");
        Font f=new Font(30);
        campshead.setFont(f);
        g4.add(campshead,1,0);
        g4.add(camname,0,1);
        g4.add(camnamet,1,  1);
        g4.add(campadd,0,2);
        g4.add(campaddt,1,2);
        g4.add(dirname,0,3);
        g4.add(dirnamet,1,3);
        g4.add(dirgrade,0,4);
        g4.add(dirgradete,1,4);
        Button savecam=new Button("Save");
        savecam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                university[position].addCampus(camnamet.getText(),campaddt.getText(),dirnamet.getText(),dirgrade.getText());
                stage.setScene(s3);
                camnamet.clear();
                campaddt.clear();
                dirnamet.clear();
                dirgradete.clear();

            }
        });
        Button cancam=new Button("Cancel");
        cancam.setOnAction(e->stage.setScene(s3));
        HBox SC1=new HBox(savecam,cancam);
        g4.add(SC1,1,5);
        g4.setAlignment(Pos.CENTER);
        g4.setVgap(10);
        g4.setHgap(10);

        Button b2=new Button("Add Department ");
      ;

        ComboBox<Campus > comboBoxc = new ComboBox<>();
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s6);
                comboBoxc.setItems(university[position].campuses);
            }
        });

        // ObservableList<String> namecampus=FXCollections.observableArrayList();

        Label w=new Label("Add Department");
        w.setFont(f);
        g6.add(w,4,0);

        g6.add(comboBoxc,4,2);
        g6.setAlignment(Pos.CENTER);
        g6.setHgap(10);
        g6.setVgap(10);
        Label selcam=new Label("Select Campus");
        g6.add(selcam,3,2);
        Label depnam=new Label("Department Name ");
        g6.add(depnam,3,3);
        TextField depnamt=new TextField();
        g6.add(depnamt,4,3);
        Label hodnam=new Label("HOD Name ");
        TextField hodnamt=new TextField();
        g6.add(hodnam,3,4);
        g6.add(hodnamt,4,4);
        Label hodg=new Label("Grade ");
        TextField hodgt=new TextField();
        g6.add(hodg,3,5);
        g6.add(hodgt,4,5);
        Button hcan=new Button("Cancel");
        hcan.setOnAction(e->stage.setScene(s3));
        Button hsave=new Button("Save");
        hsave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Campus campuses=university[position].campuses.get(comboBoxc.getSelectionModel().getSelectedIndex());
                campuses.adddepartment(depnamt.getText(),hodnamt.getText(),hodgt.getText());
                university[position].campuses.set(comboBoxc.getSelectionModel().getSelectedIndex(),campuses);
                stage.setScene(s3);
                depnamt.clear();
                hodnamt.clear();
                hodgt.clear();

            }
        });
        HBox SC3=new HBox(hsave,hcan);
        g6.add(SC3,4,6);

        Button b3=new Button("Add Employee ");
        ComboBox<Campus> comboBox1=new ComboBox<>();
        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                comboBox1.setItems(university[position].campuses);
                stage.setScene(s5);

            }
        });
        ObservableList<String> emp=FXCollections.observableArrayList();
        emp.addAll("Director","HOD","Lab Staff");
        ComboBox<String> menu=new ComboBox<>(emp);
 //    //


        ComboBox<Department> comboBox2=new ComboBox<>();
        ComboBox<Lab> comboBox3=new ComboBox<>();
        comboBox1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus campuses=university[position].campuses.get(comboBox1.getSelectionModel().getSelectedIndex());
               comboBox2.setItems(campuses.departments);

            }
        });
        comboBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus campuses=university[position].campuses.get(comboBox1.getSelectionModel().getSelectedIndex());
                Department d=campuses.departments.get(comboBox2.getSelectionModel().getSelectedIndex());
                comboBox3.setItems(d.labs);
            }
        });
        g5.add(new Label("Add Employee"),4,0);
        g5.add(comboBox1,4,2);
        Label caml=new Label("Select Campus ");
        g5.add(caml,3,2);
        Label depl=new Label("Select Department");
        g5.add(depl,3,3);
        g5.add(comboBox2,4,3);
        Label labl=new Label("Select Lab");
        g5.add(labl,3,4);
        g5.add(comboBox3,4,4);
        g5.add(menu,4,5);
        Label type=new Label("Employee Type ");
        g5.add(type,3,5);
        Label naam=new Label("Name ");
        g5.add(naam,3,6);
        TextField naamt=new TextField();
        g5.add(naamt,4,6);
        Label grade=new Label("Grade ");
        g5.add(grade,3,7);
        TextField gradet=new TextField();
        g5.add(gradet,4,7);
        Button canemp=new Button("Cancel");
        canemp.setOnAction(e->stage.setScene(s3));
        Button saveemp=new Button("Save");
        HBox SC4=new HBox(saveemp,canemp);
        g5.add(SC4,4,8);
        g5.setVgap(10);
        g5.setHgap(10);
        g5.setAlignment(Pos.CENTER);
        saveemp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int x= menu.getSelectionModel().getSelectedIndex();
                Campus campuses=university[position].campuses.get(comboBox1.getSelectionModel().getSelectedIndex());

                 if (x==0) {
                    //dir
                    campuses.director.DirectorDetails.name=naamt.getText();
                    campuses.director.DirectorDetails.grade=gradet.getText();
                } else if (x==1) {
                    //hod
                     Department d=campuses.departments.get(comboBox2.getSelectionModel().getSelectedIndex());
                    d.hod.hodDetails.name=naamt.getText();
                    d.hod.hodDetails.grade=gradet.getText();
                }else
                {//lab
                    Department d=campuses.departments.get(comboBox2.getSelectionModel().getSelectedIndex());
                    Lab l=d.labs.get(comboBox3.getSelectionModel().getSelectedIndex());
                    l.incharge.staffDetails.name=naamt.getText();
                    l.incharge.staffDetails.grade=gradet.getText();
                }
                 stage.setScene(s3);
                 naamt.clear();
                 gradet.clear();
            }
        });

        Button b4=new Button("Add Lab ");
        ComboBox<Campus> comboBox4 = new ComboBox<>();
        b4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            stage.setScene(s7);
            comboBox4.setItems(university[position].campuses);
            }
        });

        ComboBox<Department> comboBox5=new ComboBox<>();
        comboBox4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus campuses=university[position].campuses.get(comboBox4.getSelectionModel().getSelectedIndex());
                comboBox5.setItems(campuses.departments);
            }
        });
        g7.add(new Label("Add Lab"),4,0);
        g7.add(comboBox4,4,1);
        Label cam=new Label("Select Campus ");
        g7.add(cam,3,1);
        Label dep=new Label("Select Department");
        g7.add(dep,3,2);
        g7.add(comboBox5,4,2);
      Label lname=new Label("Lab Name ");
      Label lstaffname=new Label("Incharge Name ");
      Label lgrade=new Label("Grade ");
      Label project=new Label("Projector ");
      TextField lnamet=new TextField();
      TextField lstafft=new TextField();
      TextField lgradet=new TextField();
      CheckBox projector=new CheckBox();
      g7.add(lname,3,3);
      g7.add(lnamet,4,3);
      g7.add(lstaffname,3,4);
      g7.add(lstafft,4,4);
      g7.add(lgrade,3,5);
      g7.add(lgradet,4,5);
      g7.add(project,3,6);
      g7.add(projector,4,6);
      Button can7=new Button("Cancel");
      can7.setOnAction(e->stage.setScene(s3));
      Button save7=new Button("Save");
      save7.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              Campus campuses=university[position].campuses.get(comboBox4.getSelectionModel().getSelectedIndex());
              Department d=campuses.departments.get(comboBox5.getSelectionModel().getSelectedIndex());
              d.addLab(lnamet.getText(),lstafft.getText(),lgradet.getText(),projector.isSelected());
              stage.setScene(s3);
              lnamet.clear();
              lstafft.clear();
              lgradet.clear();
          }
      });
        HBox SC5=new HBox(save7,can7);
        g7.add(SC5,4,7);

        Button b5=new Button("Add Computer to Lab");
        ComboBox<Campus> comboBox6 = new ComboBox<>();
         b5.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 stage.setScene(s8);
                 comboBox6.setItems(university[position].campuses);
             }
         });

        ComboBox<Department> comboBox7=new ComboBox<>();
        ComboBox<Lab> comboBox8=new ComboBox<>();
        comboBox6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus campuses=university[position].campuses.get(comboBox6.getSelectionModel().getSelectedIndex());
                comboBox7.setItems(campuses.departments);

            }
        });
        comboBox7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus campuses=university[position].campuses.get(comboBox6.getSelectionModel().getSelectedIndex());
                Department d=campuses.departments.get(comboBox7.getSelectionModel().getSelectedIndex());
                comboBox8.setItems(d.labs);

            }
        });
       Label campc=new Label("Select Campus ");
       g8.add(campc,3,3);
       g8.add(comboBox6,4,3);
       Label depc=new Label("Select Department ");
       g8.add(depc,3,4);
       g8.add(comboBox7,4,4);
       Label labc=new Label("Select Lab ");
       g8.add(labc,3,5);
       g8.add(comboBox8,4,5);
       Label id=new Label("System ID ");
       Label ram=new Label("Ram Size ");
       Label name=new Label("System Name ");
       Label spd=new Label("Speed ");
       Label hard=new Label("Hard Disk Size");
       Label lcd=new Label("LCD Model ");
       TextField idt=new TextField();
       TextField ramt=new TextField();
       TextField namet=new TextField();
       TextField hardt=new TextField();
       TextField spdt=new TextField();
       TextField lcdt=new TextField();
       g8.add(new Label("Add Computer"),3,0);
       g8.add(id,1,3);
       g8.add(idt,2,3);
       g8.add(name,5,3);
       g8.add(namet,6,3);
       g8.add(ram,1,4);
       g8.add(ramt,2,4);
       g8.add(hard,5,4);
       g8.add(hardt,6,4);
       g8.add(spd,1,5);
       g8.add(spdt,2,5);
       g8.add(lcd,5,5);
       g8.add(lcdt,6,5);
       Button canc=new Button("Cancel");
       canc.setOnAction(e->stage.setScene(s3));
       Button savec=new Button("Save");
       savec.setAlignment(Pos.CENTER_LEFT);
       g8.add(savec,3,7);
       g8.add(canc,4,7);
      //  HBox SC6=new HBox(savec,canc);
      // g8.add(SC6,4,6);
     savec.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent actionEvent) {
             Campus campuses=university[position].campuses.get(comboBox6.getSelectionModel().getSelectedIndex());
             Department d=campuses.departments.get(comboBox7.getSelectionModel().getSelectedIndex());
             Lab l=d.labs.get(comboBox8.getSelectionModel().getSelectedIndex());
             l.addcomputer(idt.getText(),namet.getText(),spdt.getText(),ramt.getText(),hardt.getText(),lcdt.getText());
             stage.setScene(s3);
             idt.clear();
             namet.clear();
             spdt.clear();
             ramt.clear();
             hardt.clear();
             lcdt.clear();

         }
     });


        Button b6=new Button("Search for Employee?");
        b6.setOnAction(e->stage.setScene(s9));
        Label sname=new Label("Name ");
        TextField tname=new TextField();
        g9.add(sname,3,3);
        g9.add(tname,4,3);
        g9.add(new Label("Search Employee"),4,0);
        Button cans=new Button("Cancel");
        Label notfound=new Label();
        cans.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                notfound.setText(null);
                stage.setScene(s3);
                tname.clear();
            }
        });
        Button search1=new Button("Search");
        Label dcheck=new Label();
        Label dgrade=new Label();
        Label dcampus=new Label();
        Label dtype=new Label();
        g10.add(dcheck,3,4);
        g10.add(dgrade,3,5);
        g10.add(dcampus,3,8);
        g10.add(dtype,3,6);
        Label ddepart=new Label();
        Button back=new Button("Back");
        g10.add(back,3,10);
        g10.add(ddepart,3,7);
        Label dlab=new Label();
        g10.add(dlab,3,9);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s3);
               notfound.setText(null);
              tname.clear();
            }
        });
         g10.add(new Label("Employee Details"),3,0);
        search1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                namecheck=tname.getText();
             for(Campus c: university[position].campuses)
             {
                 if(c.director.DirectorDetails.name.equals(namecheck))
                 {
                     stage.setScene(s10);
                      dcheck.setText ("Name: "+c.director.DirectorDetails.name);
                      dgrade.setText("Grade: "+c.director.DirectorDetails.grade);
                      dcampus.setText("Campus: "+c.name);
                      dtype.setText("Employee Type: Director");

                     break;
                 }
                 for(Department d:c.departments )
                 {

                     System.out.println(d.hod.hodDetails.name);
                     if(d.hod.hodDetails.name.equals(namecheck))
                     {
                         stage.setScene(s10);
                         System.out.println(d.hod.hodDetails.name);
                          dcheck.setText("Name: "+d.hod.hodDetails.name);
                         dgrade.setText("Grade: "+d.hod.hodDetails.grade);
                          dcampus.setText("Campus: "+c.name);
                          ddepart.setText("Department: "+d.name);
                          dtype.setText("Employee Type: HOD");
                         break;
                     }
                     for(Lab l:d.labs)
                     {
                         if(l.incharge.staffDetails.name.equals(namecheck))
                         {
                             stage.setScene(s10);
                             dcheck.setText("Name: "+l.incharge.staffDetails.name);
                             dgrade.setText("Grade: "+l.incharge.staffDetails.grade);
                             dcampus.setText("Campus: "+c.name);
                             ddepart.setText("Department: "+d.name);
                             dtype.setText("Employee Type: Lab Staff");
                             dlab.setText("Lab: "+l.name);
                             break;
                         }
                     }
                 }
             }
               notfound.setText("No Search Found..!!");
               tname.clear();
            }
        });
        HBox SC6=new HBox(search1,cans);
        g9.add(SC6,4,4);
         g9.add(notfound,4,5);


        Button b7=new Button("Search for Computer?");
        b7.setOnAction(e->stage.setScene(s11));
        Label sid=new Label("ID");
        TextField sidt=new TextField();
        g11.add(sid,3,4);
        g11.add(sidt,4,4);
        g11.add(new Label("Search Computer"),4,0);
        Button cans1=new Button("Cancel");
        Label notfound1=new Label();
        cans1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s3);
                notfound1.setText(null);
                sidt.clear();
            }
        });
        Button search2=new Button("Search");
        HBox SC7=new HBox(search2,cans1);
        g11.add(SC7,4,5);
        g11.add(notfound1,4,6);
        Label id1=new Label();
        Label ram1=new Label();
        Label name1=new Label();
        Label spd1=new Label();
        Label hard1=new Label();
        Label lcd1=new Label();
        g12.add(id1,3,3);
        g12.add(name1,3,4);
        g12.add(spd1,3,5);
        g12.add(lcd1,3,6);
        g12.add(ram1,3,7);
        g12.add(hard1,3,8);
        Button back2=new Button("Back");
        g12.add(back2,3,9);
       g12.add(new Label("Computer Details"),3,0);
        back2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s3);
                notfound1.setText(null);
                sidt.clear();
            }
        });
       Label notfound3=new Label();
        search2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                namecheck=sidt.getText();
                for(Campus c: university[position].campuses)
                {
                    for(Department d:c.departments )
                    {
                        for(Lab l:d.labs)
                        {
                           for(Computer cs:l.computers)
                           {
                               if(cs.systemid.equals(namecheck))
                               {
                                   stage.setScene(s12);
                                   id1.setText("ID: "+cs.systemid);
                                   ram1.setText("Ram: "+cs.ramsize);
                                   hard1.setText("Hard Disk: "+cs.harddisksize);
                                   lcd1.setText("LCD Model: "+cs.lcdmodel);
                                   spd1.setText("Speed: "+cs.systemspeed);
                                   name1.setText("Name: "+cs.systemname);
                               }
                           }
                        }
                    }
                }
                notfound1.setText("No Search Found..!!");

            }
        });


        Button b8=new Button("Search for Lab?");
        b8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s13);
            }
        });
        Label namel1=new Label("Name");
        g13.add(namel1,3,3);
        TextField namelt=new TextField();
        g13.add(namelt,4,3);
        Button cans2=new Button("Cancel");
        cans2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s3);
                notfound3.setText(null);
                namelt.clear();
            }
        });

        Button search3=new Button("Search");
        HBox SC8=new HBox(search3,cans2);
        g13.add(SC8,4,4);
        g13.add(new Label("Search Lab"),4,0);
        Label labname=new Label();
        Label hasproject=new Label();
        Label incharge=new Label();
        Label totalcomputer=new Label();
        g14.add(new Label("Lab Details"),3,0);
        g14.add(labname,3,3);
        g14.add(hasproject,3,5);
        g14.add(incharge,3,4);
        g14.add(totalcomputer,3,6);
        Button back1=new Button("Back");
        g14.add(back1,3,7);
        back1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s3);
                notfound3.setText(null);
                namelt.clear();
            }
        });
        search3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                namecheck=namelt.getText();
                for(Campus c: university[position].campuses)
                {
                    for(Department d:c.departments )
                    {
                        for(Lab l:d.labs)
                        {
                            if(l.name.equals(namecheck))
                            {
                                stage.setScene(s14);
                              labname.setText("Name: "+namecheck);
                              incharge.setText("Incharge: "+l.incharge.staffDetails.name);
                              hasproject.setText("Projector: "+l.hasprojector);
                              totalcomputer.setText("Total Number of Computer: "+l.computers.size());
                                 namelt.clear();
                                break;
                            }
                        }
                    }
                }
                notfound3.setText("No Search Found");
            }
        });

        Button b10=new Button("Remove Campus");
        b10.setOnAction(e->stage.setScene(s15));
        ComboBox<Campus> remove=new ComboBox<>(university[position].campuses);
        Label camr=new Label("Select Campus ");
        g15.add(camr,3,3);
        g15.add(remove,4,3);
        Button removeb=new Button("Remove");
        removeb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                university[position].removeCampus(remove.getSelectionModel().getSelectedIndex());
                stage.setScene(s3);
            }
        });
        Button cancelb=new Button("Cancel");
        HBox SC9=new HBox(removeb,cancelb);
        g15.add(SC9,4,4);
        g15.add(new Label("Remove Campus"),4,0);
        cancelb.setOnAction(e->stage.setScene(s3));
        ComboBox<Campus> campusComboBox=new ComboBox<>(university[position].campuses);
        ComboBox<Department> departmentComboBox=new ComboBox<>();
        campusComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox.getSelectionModel().getSelectedIndex());
                departmentComboBox.setItems(c.departments);
            }
        });


        Button b11=new Button("Remove Department");
        b11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s16);
            }
        });
        g16.add(new Label("Remove Department"),4,0);
        Label camname1=new Label("Select Campus ");
        g16.add(camname1,3,3);
        g16.add(campusComboBox,4,3);
        Label depname3=new Label("Select Department ");
        g16.add(depname3,3,4);
        g16.add(departmentComboBox,4,4);
        Button removeb5=new Button("Remove");
        Button cancelb5=new Button("Cancel");
        HBox SC10=new HBox(removeb5,cancelb5);
        g16.add(SC10,4,5);
        cancelb5.setOnAction(e->stage.setScene(s3));
        removeb5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox.getSelectionModel().getSelectedIndex());
                c.removedepartment(departmentComboBox.getSelectionModel().getSelectedIndex());
            }
        });


        Button b12=new Button("Remove Lab");
        b12.setOnAction(e->stage.setScene(s17));
        ComboBox<Campus> campusComboBox1=new ComboBox<>(university[position].campuses);
        ComboBox<Department> departmentComboBox1=new ComboBox<>();
        ComboBox<Lab> labComboBox1=new ComboBox<>();
        campusComboBox1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox1.getSelectionModel().getSelectedIndex());
                departmentComboBox1.setItems(c.departments);
            }
        });
        departmentComboBox1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox1.getSelectionModel().getSelectedIndex());
                Department d=c.departments.get(departmentComboBox1.getSelectionModel().getSelectedIndex());
                labComboBox1.setItems(d.labs);
            }
        });
        Button removeb1=new Button("Remove");
        Button cancelb1=new Button("Cancel");
        cancelb1.setOnAction(e->stage.setScene(s3));
        removeb1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox1.getSelectionModel().getSelectedIndex());
                Department d=c.departments.get(departmentComboBox1.getSelectionModel().getSelectedIndex());
                d.removeLab(labComboBox1.getSelectionModel().getSelectedIndex());

            }
        });

        HBox SC11=new HBox(removeb1,cancelb1);
        g17.add(new Label("Remove Lab"),4,0);
        g17.add(new Label("Select Campus"),3,3);
        g17.add(campusComboBox1,4,3);
        g17.add(new Label("Select Department"),3,4);
        g17.add(new Label("Select Lab"),3,5);
        g17.add(departmentComboBox1,4,4);
        g17.add(labComboBox1,4,5);
        g17.add(SC11,4,6);


        Button b13=new Button("Remove Computer");
        b13.setOnAction(e->stage.setScene(s18));

        ComboBox<Campus> campusComboBox2=new ComboBox<>(university[position].campuses);ComboBox<Department> departmentComboBox2=new ComboBox<>();
        ComboBox<Lab> labComboBox2=new ComboBox<>();
        ComboBox<Computer> computerComboBox2=new ComboBox<>();
        campusComboBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox2.getSelectionModel().getSelectedIndex());
                departmentComboBox2.setItems(c.departments);
            }
        });
        departmentComboBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox2.getSelectionModel().getSelectedIndex());
                Department d=c.departments.get(departmentComboBox2.getSelectionModel().getSelectedIndex());
                labComboBox2.setItems(d.labs);
            }
        });
        labComboBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Campus c=university[position].campuses.get(campusComboBox2.getSelectionModel().getSelectedIndex());
                Department d=c.departments.get(departmentComboBox2.getSelectionModel().getSelectedIndex());
                Lab l=d.labs.get(labComboBox2.getSelectionModel().getSelectedIndex());
                computerComboBox2.setItems(l.computers);
            }
        });
        Button removeb3=new Button("Remove");
        Button cancelb3=new Button("Cancel");
        HBox SC12=new HBox(removeb3,cancelb3);
    cancelb3.setOnAction(e->stage.setScene(s3));
        removeb3.setOnAction(new EventHandler<ActionEvent>() {
                                 @Override
                                 public void handle(ActionEvent actionEvent) {
                                     Campus c = university[position].campuses.get(campusComboBox2.getSelectionModel().getSelectedIndex());
                                     Department d = c.departments.get(departmentComboBox2.getSelectionModel().getSelectedIndex());
                                     Lab l = d.labs.get(labComboBox2.getSelectionModel().getSelectedIndex());
                                     l.removecomputer(computerComboBox2.getSelectionModel().getSelectedIndex());
                                 }


                             });
          g18.add(new Label("Remove Computer"),4,0);
          g18.add(new Label("Select Campus"),3,3);
          g18.add(campusComboBox2,4,3);
          g18.add(new Label("Select Department"),3,4);
          g18.add(departmentComboBox2,4,4);
          g18.add(new Label("Select Lab"),3,5);
          g18.add(labComboBox2,4,5);
          g18.add(new Label("Select Computer"),3,6);
          g18.add(computerComboBox2,4,6);
          g18.add(SC12,4,7);

        Button b9=new Button("Log out");
        b9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s1);
                try {
                    storeDataToDatabase(university[position]);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Label unidis=new Label();
       g3.add(unidis,3,2);

        g3.add(b1,2,3);
        g3.add(b2,4,3);
        g3.add(b3,2,4);
        g3.add(b4,4,4);
        g3.add(b5,2,5);
        g3.add(b6,4,5);
        g3.add(b7,2,6);
        g3.add(b8,4,6);
        g3.add(b10,2,7);
        g3.add(b11,4,7);
        g3.add(b12,2,8);
        g3.add(b13,4,8);
        g3.add(b9,3,9);
        g3.setVgap(10);
        g3.setAlignment(Pos.CENTER);
        b9.setAlignment(Pos.CENTER);
          Label namel=new Label("University Name ");
          TextField logname=new TextField();
          Label passl=new Label("Password ");
         PasswordField passlp=new PasswordField();
         g.add(new Label("Login"),1,0);
         g.add(namel,0,3);
         g.add(logname,1,3);
         g.add(passl,0,4);
         g.add(passlp,1,4);
         g.setHgap(10);
         g.setVgap(10);
         g.setAlignment(Pos.CENTER);
        Button login=new Button("Login");
        Button cancl=new Button("Cancel");
        cancl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(s1);
                logname.clear();
                passlp.clear();

            }
        });
        HBox SC2=new HBox(login,cancl);
        g.add(SC2,1,5);
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String lnaam=logname.getText();
                String lpass=passlp.getText();
                for(int i=0;i<=unicount;i++)
                {
                  try {
                      if (university[i].register.uniname.equals(lnaam))
                          if (university[i].register.pass.equals(lpass)) {
                              position = i;
                              stage.setScene(s3);
                              logname.clear();
                              passlp.clear();
                              unidis.setText("Welcome to " + university[position].register.uniname);
                              break;
                          }
                  }catch (Exception e)
                  {

                  }
                }
            }
        });
        //  ComboBox<String> comboBox = new ComboBox<>(list);
        // g1.getChildren().add(comboBox);
        stage.show();
    }

    public static void main(String[] args) throws Exception {

        //Scanner reader=new Scanner(reg);
        launch();




    }




}
//remove me changing krni ha aray scn me add krne