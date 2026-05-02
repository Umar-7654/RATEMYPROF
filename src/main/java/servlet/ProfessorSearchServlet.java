package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Professor;
import service.DataStore;

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

        List<Professor> professors = DataStore.getInstance().searchProfessorsByName(typedText);

        for (int i=0; i<professors.size(); i=i+1) {
            Professor professor = professors.get(i);

            out.println(professor.getId() + "|" + professor.getName());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}