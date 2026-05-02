package service;

import model.DBConnection;
import model.Department;
import model.Professor;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static DataStore instance;
    
    private DataStore() {
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    //NEW
    
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM departments");

            while (rs.next()) {
                String shortId = rs.getString("short_id");

                Department department = new Department(
                        shortId,
                        rs.getString("name"),
                        rs.getString("image_path"),
                        getProfessorsByDepartment(shortId)
                );

                departments.add(department);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }
    
    
    
    //OLD

    public Department getDepartment(String short_id) {
        return departments.get(short_id);
    }

    public void addDepartment(Department department) {
        departments.put(department.getShort_id(), department);
    }

    public boolean removeDepartment(String short_id) {
        return departments.remove(short_id) != null;
    }

    public boolean addProfessor(String dept_short_id, Professor professor) {
        Department dept = departments.get(dept_short_id);
        if (dept == null) return false;
        dept.addProfessor(professor);
        return true;
    }

    public boolean removeProfessor(String dept_short_id, String prof_id) {
        Department dept = departments.get(dept_short_id);
        if (dept == null) return false;
        return dept.removeProfessor(prof_id);
    }

    public boolean updateProfessor(String dept_short_id, Professor updated) {
        Department dept = departments.get(dept_short_id);
        if (dept == null) return false;
        List<Professor> profs = dept.getProfessors();
        for (int i = 0; i < profs.size(); i++) {
            if (profs.get(i).getId().equals(updated.getId())) {
                dept.removeProfessor(updated.getId());
                dept.addProfessor(updated);
                return true;
            }
        }
        return false;
    }
}
