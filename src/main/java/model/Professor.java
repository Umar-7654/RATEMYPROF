package model;

public class Professor {

    private String id;
    private String name;
    private String gender;
    private String dept_short_id;
    private double rating;
    private int reviews;

    public Professor() {
    }

    public Professor(String id, String name, String gender, String dept_short_id, double rating, int reviews) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dept_short_id = dept_short_id;
        this.rating = rating;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDept_short_id() {
        return dept_short_id;
    }

    public void setDept_short_id(String dept_short_id) {
        this.dept_short_id = dept_short_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }
}
