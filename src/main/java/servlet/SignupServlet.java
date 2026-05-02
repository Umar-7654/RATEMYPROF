package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.DBConnection;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SignupServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String ausID = request.getParameter("ausID");
        String password = request.getParameter("password");

        String fullName = firstName + " " + lastName;
        String email = ausID + "@aus.edu";
        String userType = "student";

        response.setContentType("text/html");

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO users (ID, full_name, email, password, user_type) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, ausID);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, userType);

            stmt.executeUpdate();

            stmt.close();
            conn.close();

            response.sendRedirect("login.html");
        }
        catch (SQLException e) {
            PrintWriter out = response.getWriter();

            out.println("<h1>Signup failed</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='signup.html'>Go back</a>");
        }
    }
}