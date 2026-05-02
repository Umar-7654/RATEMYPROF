package servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Professor;
import service.DataStore;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/api/professors")
public class ProfessorServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final DataStore store = DataStore.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            JsonNode body = mapper.readTree(req.getReader());
            String deptId = body.get("dept_short_id").asText();
            Professor prof = mapper.treeToValue(body.get("professor"), Professor.class);
            boolean added = store.addProfessor(deptId, prof);
            if (!added) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Department not found")));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.print(mapper.writeValueAsString(prof));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            JsonNode body = mapper.readTree(req.getReader());
            String deptId = body.get("dept_short_id").asText();
            Professor prof = mapper.treeToValue(body.get("professor"), Professor.class);
            boolean updated = store.updateProfessor(deptId, prof);
            if (!updated) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Professor or department not found")));
                return;
            }
            out.print(mapper.writeValueAsString(prof));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(mapper.writeValueAsString(new ErrorResponse(e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            String deptId = req.getParameter("dept");
            String profId = req.getParameter("id");
            boolean removed = store.removeProfessor(deptId, profId);
            if (!removed) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Professor or department not found")));
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
        ErrorResponse(String error) { this.error = error; }
    }
}