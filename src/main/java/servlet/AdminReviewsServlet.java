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

@WebServlet("/api/admin/reviews")
public class AdminReviewsServlet extends HttpServlet {

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

            StringBuilder sql = new StringBuilder();

            sql.append("SELECT r.id, r.course_code, r.rating, r.difficulty, ");
            sql.append("r.review_text, p.name AS professor_name, u.full_name AS student_name ");
            sql.append("FROM reviews r ");
            sql.append("LEFT JOIN professors p ON r.professor_id = p.id ");
            sql.append("LEFT JOIN users u ON r.student_id = u.ID ");
            sql.append("ORDER BY r.id DESC");

            ResultSet rs = stmt.executeQuery(sql.toString());

            ArrayNode reviews = mapper.createArrayNode();

            while (rs.next()) {
                ObjectNode review = mapper.createObjectNode();

                review.put("id", rs.getInt("id"));
                review.put("professorName", rs.getString("professor_name"));
                review.put("studentName", rs.getString("student_name"));
                review.put("courseCode", rs.getString("course_code"));
                review.put("rating", rs.getInt("rating"));
                review.put("difficulty", rs.getInt("difficulty"));
                review.put("reviewText", rs.getString("review_text"));

                reviews.add(review);
            }

            out.print(mapper.writeValueAsString(reviews));

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

            sql.append("DELETE FROM reviews WHERE id = ")
               .append(id);

            int rowsDeleted = stmt.executeUpdate(sql.toString());

            if (rowsDeleted == 0) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(mapper.writeValueAsString(new ErrorResponse("Review not found")));
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