package model;

public abstract class User {

    private String ID;
    private String full_name;
    private String email;
    private String password;
    private String user_type;

    public User() {
    }

    public User(String ID, String full_name, String email, String password, String user_type) {
        this.ID = ID;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
