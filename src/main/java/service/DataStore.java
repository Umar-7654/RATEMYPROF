package service;

import model.DBConnection;
import model.Department;
import model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM departments";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

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

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    public Department getDepartment(String short_id) {
        Department department = null;

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM departments WHERE short_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, short_id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                department = new Department(
                    rs.getString("short_id"),
                    rs.getString("name"),
                    rs.getString("image_path"),
                    getProfessorsByDepartment(rs.getString("short_id"))
                );
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    public List<Professor> getProfessorsByDepartment(String dept_short_id) {
        List<Professor> professors = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM professors WHERE dept_short_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, dept_short_id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Professor professor = new Professor(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("dept_short_id"),
                    rs.getDouble("rating"),
                    rs.getInt("reviews")
                );

                professors.add(professor);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professors;
    }

    public List<Professor> searchProfessorsByName(String typedText) {
        List<Professor> professors = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM professors WHERE LOWER(name) LIKE ? LIMIT 4";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, typedText.toLowerCase() + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Professor professor = new Professor(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("dept_short_id"),
                    rs.getDouble("rating"),
                    rs.getInt("reviews")
                );

                professors.add(professor);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professors;
    }

    public boolean addDepartment(Department department) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO departments (short_id, name, image_path) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, department.getShort_id());
            stmt.setString(2, department.getName());
            stmt.setString(3, department.getImage_path());

            int rowsAdded = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsAdded > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean removeDepartment(String short_id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM departments WHERE short_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, short_id);

            int rowsDeleted = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addProfessor(String dept_short_id, Professor professor) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO professors (id, name, gender, dept_short_id, rating, reviews) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, professor.getId());
            stmt.setString(2, professor.getName());
            stmt.setString(3, professor.getGender());
            stmt.setString(4, dept_short_id);
            stmt.setDouble(5, professor.getRating());
            stmt.setInt(6, professor.getReviews());

            int rowsAdded = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsAdded > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean removeProfessor(String dept_short_id, String prof_id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM professors WHERE id = ? AND dept_short_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, prof_id);
            stmt.setString(2, dept_short_id);

            int rowsDeleted = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateProfessor(String dept_short_id, Professor updated) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE professors SET name = ?, gender = ?, dept_short_id = ?, rating = ?, reviews = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, updated.getName());
            stmt.setString(2, updated.getGender());
            stmt.setString(3, dept_short_id);
            stmt.setDouble(4, updated.getRating());
            stmt.setInt(5, updated.getReviews());
            stmt.setString(6, updated.getId());

            int rowsUpdated = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}