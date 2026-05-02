const searchBox = document.getElementById("search-name-box");
const resultsBox = document.getElementById("search-results");
const loginArea = document.getElementById("login-area");

function searchProfessor() {
    const typedText = searchBox.value.toLowerCase();

    resultsBox.innerHTML = "";

    if (typedText === "") {
        resultsBox.style.display = "none";
        return;
    }

    fetch("searchProfessor?query=" + encodeURIComponent(typedText))
        .then(getTextFromResponse)
        .then(displayProfessors);
}

function getTextFromResponse(response) {
    return response.text();
}

function displayProfessors(data) {
    resultsBox.innerHTML = "";

    const professors = data.split("\n");

    let resultsss = 0;

    for (let i=0; i<professors.length; i=i+1) {
        const professorLine = professors[i].trim();

        if (professorLine !== "" && resultsss < 3) {
            const professorParts = professorLine.split("|");

            const professorId = professorParts[0];
            const professorName = professorParts[1];

            addProfessorToDropdown(professorId, professorName);

            resultsss = resultsss + 1;
        }
    }

    if (resultsss===0) {
        resultsBox.style.display = "none";
    } else {
        resultsBox.style.display = "block";
    }
}

function addProfessorToDropdown(professorId, professorName) {
    const resultItem = document.createElement("div");

    resultItem.className = "professor-result";
    resultItem.textContent = professorName;

    resultItem.addEventListener("click", function() {
        window.location.href = "professor.html?id=" + encodeURIComponent(professorId);
    });

    resultsBox.appendChild(resultItem);
}

function checkLoggedInUser() {
    fetch("userInfo")
        .then(getUserInfoText)
        .then(showUserInitials);
}

function getUserInfoText(response) {
    return response.text();
}

function showUserInitials(data) {
    console.log("User info servlet returned:", data);

    if (data!=="not_logged_in") {
        loginArea.innerHTML = "";

        const initialsButton = document.createElement("button");
        initialsButton.id = "initials-button";
        initialsButton.textContent = data;

        const dropdown = document.createElement("div");
        dropdown.id = "user-dropdown";
        dropdown.style.display = "none";

        const signOutLink = document.createElement("a");
        signOutLink.href = "logout";
        signOutLink.textContent = "Sign out";

        dropdown.appendChild(signOutLink);

        loginArea.appendChild(initialsButton);
        loginArea.appendChild(dropdown);

        initialsButton.addEventListener("click", toggleUserDropdown);
    }
}

function toggleUserDropdown() {
    const dropdown = document.getElementById("user-dropdown");

    if (dropdown.style.display === "none") {
        dropdown.style.display = "block";
    } else {
        dropdown.style.display = "none";
    }
}

searchBox.addEventListener("input", searchProfessor);
window.addEventListener("load", checkLoggedInUser);