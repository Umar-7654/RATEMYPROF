package model;

public class Review {

    private String courseCode;
    private int rating;
    private int difficulty;
    private boolean wouldTakeAgain;
    private boolean forCredit;
    private boolean usedTextbook;
    private boolean attendanceMandatory;
    private String gradeReceived;
    private String tag1;
    private String tag2;
    private String tag3;
    private String reviewText;

    public Review() {
    }

    public Review(String courseCode, int rating, int difficulty,
                  boolean wouldTakeAgain, boolean forCredit,
                  boolean usedTextbook, boolean attendanceMandatory,
                  String gradeReceived, String tag1, String tag2,
                  String tag3, String reviewText) {
        this.courseCode = courseCode;
        this.rating = rating;
        this.difficulty = difficulty;
        this.wouldTakeAgain = wouldTakeAgain;
        this.forCredit = forCredit;
        this.usedTextbook = usedTextbook;
        this.attendanceMandatory = attendanceMandatory;
        this.gradeReceived = gradeReceived;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.reviewText = reviewText;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public boolean isWouldTakeAgain() { return wouldTakeAgain; }
    public void setWouldTakeAgain(boolean wouldTakeAgain) { this.wouldTakeAgain = wouldTakeAgain; }
    public boolean isForCredit() { return forCredit; }
    public void setForCredit(boolean forCredit) { this.forCredit = forCredit; }
    public boolean isUsedTextbook() { return usedTextbook; }
    public void setUsedTextbook(boolean usedTextbook) { this.usedTextbook = usedTextbook; }
    public boolean isAttendanceMandatory() { return attendanceMandatory; }
    public void setAttendanceMandatory(boolean attendanceMandatory) { this.attendanceMandatory = attendanceMandatory; }
    public String getGradeReceived() { return gradeReceived; }
    public void setGradeReceived(String gradeReceived) { this.gradeReceived = gradeReceived; }
    public String getTag1() { return tag1; }
    public void setTag1(String tag1) { this.tag1 = tag1; }
    public String getTag2() { return tag2; }
    public void setTag2(String tag2) { this.tag2 = tag2; }
    public String getTag3() { return tag3; }
    public void setTag3(String tag3) { this.tag3 = tag3; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}
