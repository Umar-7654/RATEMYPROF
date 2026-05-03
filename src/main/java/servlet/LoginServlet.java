package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.DBConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String userType = rs.getString("user_type");

                HttpSession session = request.getSession();

                session.setAttribute("fullName", fullName);
                session.setAttribute("email", email);
                session.setAttribute("userType", userType);

                if (userType.equals("admin")) {
                    response.sendRedirect("admin-departments");
                } else {
                    response.sendRedirect("homepage.html");
                }
            } else {
                PrintWriter out = response.getWriter();

                out.println("<h1>Login failed</h1>");
                out.println("<p>Email or password is incorrect.</p>");
                out.println("<a href='login.html'>Go back</a>");
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e) {
            PrintWriter out = response.getWriter();

            out.println("<h1>Login error</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='login.html'>Go back</a>");
        }
    }
}