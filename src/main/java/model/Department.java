package model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Department {

    private String short_id;
    private String name;
    private String image_path;
    private List<Professor> professors;

    public Department() {
        this.professors = new CopyOnWriteArrayList<>();
    }

    public Department(String short_id, String name, String image_path, List<Professor> professors) {
        this.short_id = short_id;
        this.name = name;
        this.image_path = image_path;
        this.professors = new CopyOnWriteArrayList<>(professors);
    }

    public void addProfessor(Professor professor) {
        professors.add(professor);
    }

    public boolean removeProfessor(String prof_id) {
        return professors.removeIf(p -> p.getId().equals(prof_id));
    }

    public String getShort_id() {
        return short_id;
    }

    public void setShort_id(String short_id) {
        this.short_id = short_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = new CopyOnWriteArrayList<>(professors);
    }
}
