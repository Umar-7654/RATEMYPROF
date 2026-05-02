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
import model.Review;

@WebServlet("/submitReview")
public class ReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ReviewServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get professor ID from form
        String professorId = request.getParameter("professorId");

     // Get student ID from session
        HttpSession session = request.getSession(false);
        String studentId = null;
        if (session != null && session.getAttribute("email") != null) {
            String email = (String) session.getAttribute("email");
            try {
                Connection connUser = DBConnection.getConnection();
                PreparedStatement stmtUser = connUser.prepareStatement("SELECT ID FROM users WHERE email = ?");
                stmtUser.setString(1, email);
                ResultSet rs = stmtUser.executeQuery();
                if (rs.next()) {
                    studentId = rs.getString("ID");
                }
                rs.close();
                stmtUser.close();
                connUser.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        // Block if not logged in
        if (studentId == null) {
            response.sendRedirect("login.html");
            return;
        }

        String courseCode = request.getParameter("courseCode");
        int rating = Integer.parseInt(request.getParameter("rating"));
        int difficulty = Integer.parseInt(request.getParameter("difficulty"));
        boolean wouldTakeAgain = "yes".equals(request.getParameter("wouldTakeAgain"));
        boolean forCredit = "on".equals(request.getParameter("forCredit"));
        boolean usedTextbook = "on".equals(request.getParameter("usedTextbook"));
        boolean attendanceMandatory = "on".equals(request.getParameter("attendanceMandatory"));
        String gradeReceived = request.getParameter("gradeReceived");
        String tag1 = request.getParameter("tag1") != null ? request.getParameter("tag1") : "";
        String tag2 = request.getParameter("tag2") != null ? request.getParameter("tag2") : "";
        String tag3 = request.getParameter("tag3") != null ? request.getParameter("tag3") : "";
        String reviewText = request.getParameter("reviewText");

        Review review = new Review(courseCode, rating, difficulty, wouldTakeAgain,
                forCredit, usedTextbook, attendanceMandatory, gradeReceived,
                tag1, tag2, tag3, reviewText);

        response.setContentType("text/html");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO reviews (professor_id, student_id, course_code, rating, difficulty, " +
                         "would_take_again, for_credit, used_textbook, attendance_mandatory, grade_received, " +
                         "tag1, tag2, tag3, review_text) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorId);
            stmt.setString(2, studentId);
            stmt.setString(3, review.getCourseCode());
            stmt.setInt(4, review.getRating());
            stmt.setInt(5, review.getDifficulty());
            stmt.setBoolean(6, review.isWouldTakeAgain());
            stmt.setBoolean(7, review.isForCredit());
            stmt.setBoolean(8, review.isUsedTextbook());
            stmt.setBoolean(9, review.isAttendanceMandatory());
            stmt.setString(10, review.getGradeReceived());
            stmt.setString(11, review.getTag1());
            stmt.setString(12, review.getTag2());
            stmt.setString(13, review.getTag3());
            stmt.setString(14, review.getReviewText());

            stmt.executeUpdate();
            stmt.close();
            conn.close();

            response.sendRedirect("homepage.html");

        } catch (SQLException e) {
            PrintWriter out = response.getWriter();
            out.println("<h1>Error submitting review</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='submitrating.html'>Go back</a>");
        }
    }
}