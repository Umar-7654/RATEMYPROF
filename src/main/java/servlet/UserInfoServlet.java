package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserInfoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        HttpSession session = request.getSession(false);

        PrintWriter out = response.getWriter();

        if (session == null || session.getAttribute("fullName") == null) {
            out.print("not_logged_in");
            return;
        }

        String fullName = (String) session.getAttribute("fullName");

        String[] nameParts = fullName.split(" ");

        String initials = "";

        if (nameParts.length >= 2) {
            initials = nameParts[0].substring(0, 1).toUpperCase() + nameParts[1].substring(0, 1).toUpperCase();
        } else {
            initials = fullName.substring(0, 1).toUpperCase();
        }

        out.print(initials);
    }
}