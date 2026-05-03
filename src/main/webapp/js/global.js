const globalLoginArea = document.getElementById("global-login-area");
const globalNavbar = document.getElementById("global-navbar");

function checkLoggedInUserGlobal() {
    fetch("userInfo")
        .then(getGlobalUserInfoText)
        .then(showGlobalUserInitials);
}

function getGlobalUserInfoText(response) {
    return response.text();
}

function showGlobalUserInitials(data) {
    if (data !== "not_logged_in") {
        globalLoginArea.innerHTML = "";

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

        globalLoginArea.appendChild(initialsButton);
        globalLoginArea.appendChild(dropdown);

        initialsButton.addEventListener("click", toggleGlobalUserDropdown);
    }
}

function toggleGlobalUserDropdown() {
    const dropdown = document.getElementById("user-dropdown");

    if (dropdown.style.display === "none") {
        dropdown.style.display = "block";
    } else {
        dropdown.style.display = "none";
    }
}

function handleNavbarScroll() {
    if (window.scrollY > 120) {
        globalNavbar.classList.add("hide-navbar");
    } else {
        globalNavbar.classList.remove("hide-navbar");
    }
}

window.addEventListener("load", checkLoggedInUserGlobal);
window.addEventListener("scroll", handleNavbarScroll);