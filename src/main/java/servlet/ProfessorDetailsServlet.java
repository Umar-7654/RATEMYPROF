package servlet;

import model.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@WebServlet("/api/professor")
public class ProfessorDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ObjectMapper mapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String professorId = request.getParameter("id");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (professorId == null || professorId.equals("")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing professor id\"}");
            return;
        }

        professorId = clean(professorId);

        ObjectNode result = mapper.createObjectNode();

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            StringBuilder professorSql = new StringBuilder();
            professorSql.append("SELECT p.id, p.name, p.gender, p.dept_short_id, p.rating, p.reviews, ");
            professorSql.append("d.name AS department_name ");
            professorSql.append("FROM professors p ");
            professorSql.append("LEFT JOIN departments d ON p.dept_short_id = d.short_id ");
            professorSql.append("WHERE p.id = '").append(professorId).append("'");

            ResultSet profRs = stmt.executeQuery(professorSql.toString());

            if (!profRs.next()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Professor not found\"}");
                conn.close();
                return;
            }

            result.put("id", profRs.getString("id"));
            result.put("name", profRs.getString("name"));
            result.put("gender", profRs.getString("gender"));
            result.put("deptShortId", profRs.getString("dept_short_id"));
            result.put("departmentName", profRs.getString("department_name"));

            double averageRating = profRs.getDouble("rating");
            int reviewCount = profRs.getInt("reviews");

            profRs.close();

            int amazing = 0;
            int great = 0;
            int good = 0;
            int ok = 0;
            int bad = 0;

            int wouldTakeAgainCount = 0;
            int totalReviews = 0;
            int totalDifficulty = 0;
            int totalRating = 0;

            ArrayNode reviewsArray = mapper.createArrayNode();

            StringBuilder reviewSql = new StringBuilder();
            reviewSql.append("SELECT * FROM reviews ");
            reviewSql.append("WHERE professor_id = '").append(professorId).append("' ");
            reviewSql.append("ORDER BY created_at DESC");

            ResultSet reviewRs = stmt.executeQuery(reviewSql.toString());

            while (reviewRs.next()) {
                int rating = reviewRs.getInt("rating");
                int difficulty = reviewRs.getInt("difficulty");
                boolean wouldTakeAgain = reviewRs.getBoolean("would_take_again");

                totalReviews = totalReviews + 1;
                totalRating = totalRating + rating;
                totalDifficulty = totalDifficulty + difficulty;

                if (wouldTakeAgain) {
                    wouldTakeAgainCount = wouldTakeAgainCount + 1;
                }

                if (rating >= 5) {
                    amazing = amazing + 1;
                } else if (rating >= 4) {
                    great = great + 1;
                } else if (rating >= 3) {
                    good = good + 1;
                } else if (rating >= 2) {
                    ok = ok + 1;
                } else {
                    bad = bad + 1;
                }

                ObjectNode reviewNode = mapper.createObjectNode();

                reviewNode.put("courseCode", reviewRs.getString("course_code"));
                reviewNode.put("rating", rating);
                reviewNode.put("difficulty", difficulty);
                reviewNode.put("wouldTakeAgain", wouldTakeAgain);
                reviewNode.put("forCredit", reviewRs.getBoolean("for_credit"));
                reviewNode.put("usedTextbook", reviewRs.getBoolean("used_textbook"));
                reviewNode.put("attendanceMandatory", reviewRs.getBoolean("attendance_mandatory"));
                reviewNode.put("gradeReceived", reviewRs.getString("grade_received"));
                reviewNode.put("tag1", reviewRs.getString("tag1"));
                reviewNode.put("tag2", reviewRs.getString("tag2"));
                reviewNode.put("tag3", reviewRs.getString("tag3"));
                reviewNode.put("reviewText", reviewRs.getString("review_text"));
                reviewNode.put("createdAt", reviewRs.getString("created_at"));

                reviewsArray.add(reviewNode);
            }

            reviewRs.close();

            if (totalReviews > 0) {
                averageRating = (double) totalRating / totalReviews;
                reviewCount = totalReviews;
            }

            int wouldTakeAgainPercent = 0;
            int difficultyPercent = 0;

            if (totalReviews > 0) {
                wouldTakeAgainPercent = (wouldTakeAgainCount * 100) / totalReviews;

                double averageDifficulty = (double) totalDifficulty / totalReviews;
                difficultyPercent = (int) Math.round((averageDifficulty / 5.0) * 100);
            }

            result.put("averageRating", averageRating);
            result.put("reviewCount", reviewCount);
            result.put("wouldTakeAgainPercent", wouldTakeAgainPercent);
            result.put("difficultyPercent", difficultyPercent);

            ObjectNode breakdown = mapper.createObjectNode();
            breakdown.put("Amazing", amazing);
            breakdown.put("Great", great);
            breakdown.put("Good", good);
            breakdown.put("Ok", ok);
            breakdown.put("Bad", bad);

            result.set("breakdown", breakdown);
            result.set("reviews", reviewsArray);

            response.getWriter().write(mapper.writeValueAsString(result));

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }

    private String clean(String value) {
        return value.replace("'", "''");
    }
}