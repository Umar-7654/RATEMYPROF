package model;

public class Admin extends User {

    public Admin() {
    }

    public Admin(String ID, String full_name, String email, String password, String user_type, String admin_role) {
        super(ID, full_name, email, password, user_type);
    }
}
