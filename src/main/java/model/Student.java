package model;

public class Student extends User {

    public Student(){
    }

    public Student(String ID, String full_name, String email, String password, String user_type){
        super(ID, full_name, email, password, user_type);
    }
}
