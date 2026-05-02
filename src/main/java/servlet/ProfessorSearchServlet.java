package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/searchProfessor")
public class ProfessorSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProfessorSearchServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String typedText = request.getParameter("query");

        response.setContentType("text/plain");

        PrintWriter out = response.getWriter();

        if (typedText == null) {
            typedText = "";
        }

        typedText = typedText.toLowerCase();

        String[] professors = {
            "Dana Dyghyam",
            "Daniel Abbott",
            "David Gray",
            "Jonathan Davis",
            "Darius Spieth"
        };

        int count = 0;

        for (int i=0; i<professors.length; i=i+1) {
            String professorName = professors[i];

            if (professorName.toLowerCase().startsWith(typedText) && count < 3) {
                out.println(professorName);
                count=count+1;
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}