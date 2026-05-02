package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/searchProfessor")
public class ProfessorSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ObjectMapper mapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String typedText = request.getParameter("query");

        if (typedText == null) {
            typedText = "";
        }

        typedText = clean(typedText.toLowerCase());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ArrayNode results = mapper.createArrayNode();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.id, p.name, d.name AS department_name ");
            sql.append("FROM professors p ");
            sql.append("LEFT JOIN departments d ON p.dept_short_id = d.short_id ");
            sql.append("WHERE LOWER(p.name) LIKE '").append(typedText).append("%' ");
            sql.append("LIMIT 5");

            ResultSet rs = stmt.executeQuery(sql.toString());

            while (rs.next()) {
                ObjectNode professor = mapper.createObjectNode();

                professor.put("id", rs.getString("id"));
                professor.put("name", rs.getString("name"));
                professor.put("departmentName", rs.getString("department_name"));

                results.add(professor);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.getWriter().write(mapper.writeValueAsString(results));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String clean(String value) {
        return value.replace("'", "''");
    }
}