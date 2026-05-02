document.addEventListener("DOMContentLoaded", function () {
    loadProfessor();
});

function loadProfessor() {
    var params = new URLSearchParams(window.location.search);
    var professorId = params.get("id");

    if (professorId == null || professorId == "") {
        document.getElementById("professor-name").innerHTML = "No professor selected";
        return;
    }

    document.getElementById("write-review-button").onclick = function () {
        window.location.href = "submitrating.html?professorId=" + professorId;
    };

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "api/professor?id=" + professorId, true);

    xhr.onload = function () {
        if (xhr.status == 200) {
            var professor = JSON.parse(xhr.responseText);

            showProfessorInfo(professor);
            showRatingBreakdown(professor);
            showReviews(professor.reviews);
        } else {
            document.getElementById("professor-name").innerHTML = "Professor not found";
        }
    };

    xhr.send();
}

function showProfessorInfo(professor) {
    document.getElementById("professor-name").innerHTML = professor.name;

    document.getElementById("professor-affiliation").innerHTML =
        "Professor in the American University of Sharjah's " + professor.departmentName + " Department";

    document.getElementById("biggest-rating").innerHTML =
        professor.averageRating.toFixed(1) + ' <span id="out-of-rating">/5</span>';

    document.querySelector("#professor-would-take .prof-stat-number").innerHTML =
        professor.wouldTakeAgainPercent + "%";

    document.querySelector("#professor-would-take .gauge-fill").style.setProperty(
        "--percent",
        professor.wouldTakeAgainPercent
    );

    document.querySelector("#professor-difficulty .prof-stat-number").innerHTML =
        professor.difficultyPercent + "%";

    document.querySelector("#professor-difficulty .gauge-fill").style.setProperty(
        "--percent",
        professor.difficultyPercent
    );
}

function showRatingBreakdown(professor) {
    var container = document.getElementById("rating-breakdown-content");

    var amazing = professor.breakdown.Amazing;
    var great = professor.breakdown.Great;
    var good = professor.breakdown.Good;
    var ok = professor.breakdown.Ok;
    var bad = professor.breakdown.Bad;

    var total = amazing + great + good + ok + bad;

    var html = "";

    html += '<p id="rating-breakdown-title">Rating Breakdown</p>';
    html += makeRatingRow("Amazing", amazing, total);
    html += makeRatingRow("Great", great, total);
    html += makeRatingRow("Good", good, total);
    html += makeRatingRow("Ok", ok, total);
    html += makeRatingRow("Bad", bad, total);

    container.innerHTML = html;
}

function makeRatingRow(label, count, total) {
    var percent = 0;

    if (total > 0) {
        percent = (count / total) * 100;
    }

    var html = "";

    html += '<div class="rating-row">';
    html += '<span class="rating-category col-lg-2">' + label + '</span>';
    html += '<div class="rating-bar">';
    html += '<div class="rating-fill" style="width: ' + percent + '%;"></div>';
    html += '</div>';
    html += '<span class="rating-count">' + count + '</span>';
    html += '</div>';

    return html;
}

function showReviews(reviews) {
    var reviewsList = document.getElementById("reviews-list");

    if (reviews == null || reviews.length == 0) {
        reviewsList.innerHTML = "<p>No reviews yet.</p>";
        return;
    }

    var html = "";

    for (var i = 0; i < reviews.length; i++) {
        var review = reviews[i];

        html += '<div class="card mb-4">';
        html += '<div class="card-body row review-card">';

        html += '<div class="col-lg-10 d-flex flex-column">';

        html += '<div class="card-title">';
        html += review.courseCode;
        html += '</div>';

        html += '<div class="card-text">';
        html += '<span class="review-labels">For Credit:</span> ' + yesNo(review.forCredit) + ' &nbsp; &nbsp;';
        html += '<span class="review-labels">Attendance:</span> ' + attendanceText(review.attendanceMandatory) + ' &nbsp; &nbsp;';
        html += '<span class="review-labels">Would Take Again:</span> ' + yesNo(review.wouldTakeAgain) + ' &nbsp; &nbsp;';
        html += '<span class="review-labels">Grade:</span> ' + review.gradeReceived + ' &nbsp; &nbsp;';
        html += '<span class="review-labels">Textbook:</span> ' + yesNo(review.usedTextbook) + ' &nbsp; &nbsp;';
        html += '</div>';

        html += '<div class="card-text review-text">';
        html += review.reviewText;
        html += '</div>';

        html += '<div class="card-bottom row mt-auto">';
        html += '<div class="col-lg-6">';
        html += 'Posted on ' + review.createdAt;
        html += '</div>';

        html += '<div class="col-lg-6 text-end">';
        html += 'Helpful?';
        html += '</div>';
        html += '</div>';

        html += '</div>';

        html += '<div class="col-lg-2 text-center">';

        html += '<div class="row">';
        html += '<div class="review-box-title">Overall Rating</div>';
        html += '<div class="review-box mx-auto">' + review.rating.toFixed(1) + '</div>';
        html += '</div>';

        html += '<div class="row mx-auto py-3">';
        html += '<div class="review-box-title">Difficulty</div>';
        html += '<div class="review-box mx-auto">' + review.difficulty.toFixed(1) + '</div>';
        html += '</div>';

        html += '</div>';

        html += '</div>';
        html += '</div>';
    }

    reviewsList.innerHTML = html;
}

function yesNo(value) {
    if (value == true) {
        return "Yes";
    } else {
        return "No";
    }
}

function attendanceText(value) {
    if (value == true) {
        return "Mandatory";
    } else {
        return "Not Mandatory";
    }
}