package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import model.DBConnection;

@WebServlet("/api/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT ID, full_name, email, user_type FROM users");

            ArrayNode users = mapper.createArrayNode();

            while (rs.next()) {
                ObjectNode user = mapper.createObjectNode();

                user.put("id", rs.getString("ID"));
                user.put("fullName", rs.getString("full_name"));
                user.put("email", rs.getString("email"));
                user.put("userType", rs.getString("user_type"));

                users.add(user);
            }

            out.print(mapper.writeValueAsString(users));

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        try {
            String id = req.getParameter("id");

            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder();

            sql.append("DELETE FROM users WHERE ID = '")
               .append(id)
               .append("'");

            int rowsDeleted = stmt.executeUpdate(sql.toString());

            if (rowsDeleted == 0) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("User not found")));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    private static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}