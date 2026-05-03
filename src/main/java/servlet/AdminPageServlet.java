package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/admin-departments", "/admin-professors"})
public class AdminPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userType") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String userType = (String) session.getAttribute("userType");

        if (!userType.equals("admin")) {
            response.sendRedirect("homepage.html");
            return;
        }

        String page = request.getServletPath();
        String filePath = "";

        if (page.equals("/admin-departments")) {
            filePath = "/WEB-INF/admin-departments.html";
        } else if (page.equals("/admin-professors")) {
            filePath = "/WEB-INF/admin-professors.html";
        }

        InputStream inputStream = getServletContext().getResourceAsStream(filePath);

        if (inputStream == null) {
            response.setContentType("text/plain");
            response.getWriter().println("Admin page not found.");
            return;
        }

        String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        response.setContentType("text/html");
        response.getWriter().write(html);
    }
}