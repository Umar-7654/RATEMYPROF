// Tag limit — max 3 tags
let tagCheckboxes = document.querySelectorAll(".tag-checkbox");

tagCheckboxes.forEach(function(checkbox) {
    checkbox.addEventListener("change", function() {
        let checkedTags = document.querySelectorAll(".tag-checkbox:checked");
        if (checkedTags.length > 3) {
            this.checked = false;
        }
    });
});

// Get professor ID from URL
function getProfessorId() {
    let params = new URLSearchParams(window.location.search);
    return params.get("professorId");
}

// Submit button
document.getElementById("submit-btn").addEventListener("click", function() {

    // Get professor ID from URL
    let professorId = getProfessorId();
    if (!professorId) {
        alert("No professor selected. Please go back and select a professor.");
        return;
    }

    // Get values
    let courseCode = document.getElementById("course-code").value;
    let ratingInput = document.querySelector('input[name="rating"]:checked');
    let difficultyInput = document.getElementById("difficulty").value;
    let takeAgainInput = document.querySelector('input[name="take-again"]:checked');
    let reviewText = document.getElementById("review-text").value;
    let gradeReceived = document.getElementById("grade").value;

    // Validation
    if (!courseCode) { alert("Please select a course code."); return; }
    if (!ratingInput) { alert("Please rate your professor."); return; }
    if (!difficultyInput) { alert("Please select a difficulty."); return; }
    if (!takeAgainInput) { alert("Please answer 'Would you take this professor again?'"); return; }
    if (!reviewText.trim()) { alert("Please write a review."); return; }

    // Get selected tags
    let checkedTags = document.querySelectorAll(".tag-checkbox:checked");
    let tag1 = checkedTags[0] ? checkedTags[0].nextElementSibling.innerText : "";
    let tag2 = checkedTags[1] ? checkedTags[1].nextElementSibling.innerText : "";
    let tag3 = checkedTags[2] ? checkedTags[2].nextElementSibling.innerText : "";

    // Build form and submit
    let form = document.createElement("form");
    form.method = "POST";
    form.action = "submitReview";

    let fields = {
        professorId: professorId,
        courseCode: courseCode,
        rating: ratingInput.value,
        difficulty: difficultyInput.charAt(0),
        wouldTakeAgain: takeAgainInput.value,
        forCredit: document.getElementById("for-credit").checked ? "on" : "",
        usedTextbook: document.getElementById("textbook").checked ? "on" : "",
        attendanceMandatory: document.getElementById("attendance").checked ? "on" : "",
        gradeReceived: gradeReceived,
        tag1: tag1,
        tag2: tag2,
        tag3: tag3,
        reviewText: reviewText
    };

    for (let key in fields) {
        let input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = fields[key];
        form.appendChild(input);
    }

    document.body.appendChild(form);
    form.submit();
});