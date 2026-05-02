package service;

import model.Department;
import model.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static DataStore instance;
    private final ConcurrentHashMap<String, Department> departments = new ConcurrentHashMap<>();

    private DataStore() {
        seed();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seed() {
        Department cen = new Department("CEN", "College of Engineering", "img/aus-cen.jpeg", new ArrayList<>());
        cen.addProfessor(new Professor("p_cen_1", "Dr. Fadi Aloul",              "M", "CEN", 4.5, 62));
        cen.addProfessor(new Professor("p_cen_2", "Dr. Jamal El-Din Abdalla",    "M", "CEN", 4.1, 45));
        cen.addProfessor(new Professor("p_cen_3", "Prof. Gheith Abandah",        "M", "CEN", 3.8, 29));
        cen.addProfessor(new Professor("p_cen_4", "Dr. Rana Sabouni",            "F", "CEN", 3.2, 53));
        cen.addProfessor(new Professor("p_cen_5", "Dr. Abed Al-Nasser Abdallah", "M", "CEN", 2.7, 18));
        departments.put("CEN", cen);

        Department sba = new Department("SBA", "School of Business Administration", "img/aus-sba2.png", new ArrayList<>());
        sba.addProfessor(new Professor("p_sba_1", "Dr. Sarah Khan",          "F", "SBA", 4.3, 41));
        sba.addProfessor(new Professor("p_sba_2", "Prof. Ahmed Al-Mansoori", "M", "SBA", 3.6, 28));
        sba.addProfessor(new Professor("p_sba_3", "Dr. Layla Rashid",        "F", "SBA", 4.7, 55));
        departments.put("SBA", sba);

        Department caad = new Department("CAAD", "College of Architecture, Art and Design", "img/aus-caad.jpeg", new ArrayList<>());
        caad.addProfessor(new Professor("p_caad_1", "Prof. Omar Haddad",         "M", "CAAD", 4.0, 33));
        caad.addProfessor(new Professor("p_caad_2", "Dr. Mariam Al-Suwaidi",     "F", "CAAD", 3.5, 22));
        caad.addProfessor(new Professor("p_caad_3", "Dr. Yusuf Tariq",           "M", "CAAD", 2.9, 17));
        departments.put("CAAD", caad);
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments.values());
    }

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
