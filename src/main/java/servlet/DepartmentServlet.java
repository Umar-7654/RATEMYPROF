package servlet;

import model.Department;
import service.DataStore;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/api/departments")
public class DepartmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ObjectMapper mapper = new ObjectMapper();
    private final DataStore store = DataStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        try {
            List<Department> all = store.getAllDepartments();
            String jsonResponse = mapper.writeValueAsString(all);
            out.print(jsonResponse);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        try {
            Department dept = mapper.readValue(req.getReader(), Department.class);

            store.addDepartment(dept);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.print(mapper.writeValueAsString(dept));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        try {
            Department incoming = mapper.readValue(req.getReader(), Department.class);

            Department existing = store.getDepartment(incoming.getShort_id());

            if (existing == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Department not found")));
                return;
            }

            existing.setName(incoming.getName());
            existing.setImage_path(incoming.getImage_path());

            out.print(mapper.writeValueAsString(existing));
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

            boolean removed = store.removeDepartment(id);

            if (!removed) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Department not found")));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
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