const sortSelect = document.getElementById("sort-select");
const genderFilter = document.getElementById("gender-filter");
const container = document.getElementById("dept-sections-container");

let allDepartments = [];

function loadDepartments() {
    fetch("api/departments")
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Server returned " + response.status);
            }
            return response.json();
        })
        .then(function(data) {
            allDepartments = data;
            render();
            scrollToRequestedDept();
        })
        .catch(function(err) {
            container.innerHTML = "<p class='error-message'>Could not load departments. Is the server running?</p>";
        });
}

function ratingBadge(r) {
    var cls = "badge-red";

    if (r >= 4) {
        cls = "badge-green";
    } else if (r >= 3) {
        cls = "badge-yellow";
    }

    return '<span class="rating-badge ' + cls + '">' + Number(r).toFixed(1) + '</span>';
}

function professorCard(prof) {
    var html = "";

    html += '<div class="prof-card-row">';
    html += '<img src="img/default-prof.png" alt="' + prof.name + '" class="prof-avatar">';
    html += '<div class="prof-info">';
    html += '<h5 class="prof-name">' + prof.name + '</h5>';
    html += '<div class="prof-rating-row">';
    html += ratingBadge(prof.rating);
    html += '<a href="professor.html?id=' + prof.id + '" class="btn btn-sm btn-view">View</a>';
    html += '</div>';
    html += '</div>';
    html += '</div>';

    return html;
}
function applySortAndFilter(profs) {
    let data = profs.slice();

    const gender = genderFilter.value;
    if (gender !== "all") data = data.filter(p => p.gender === gender);

    const sort = sortSelect.value;
    if (sort === "rating-desc") data.sort((a, b) => b.rating - a.rating);
    else if (sort === "rating-asc") data.sort((a, b) => a.rating - b.rating);
    else if (sort === "reviews-desc") data.sort((a, b) => b.reviews - a.reviews);
    else if (sort === "reviews-asc") data.sort((a, b) => a.reviews - b.reviews);

    return data;
}

function render() {
    if (allDepartments.length === 0) {
        container.innerHTML = "<p>No departments found.</p>";
        return;
    }

    container.innerHTML = allDepartments.map(dept => {
        const profs = applySortAndFilter(dept.professors || []);
        const profsHtml = profs.length === 0
            ? "<p class='no-profs'>No professors match the current filter.</p>"
            : profs.map(professorCard).join("");

			return '' +
			    '<section class="dept-section" id="dept-' + dept.short_id + '">' +
			        '<div class="dept-banner">' +
			            '<img src="' + dept.image_path + '" alt="' + dept.name + '">' +
			        '</div>' +
			        '<h3 class="dept-section-title">' + dept.name + ' (' + dept.short_id + ')</h3>' +
			        '<div class="prof-list">' +
			            profsHtml +
			        '</div>' +
			    '</section>';
    }).join("");
}

function scrollToRequestedDept() {
    const params = new URLSearchParams(window.location.search);
    const requested = params.get("dept");
    if (!requested) return;

    const target = document.getElementById("dept-" + requested);
    if (target) {
        setTimeout(() => target.scrollIntoView({ behavior: "smooth", block: "start" }), 100);
    }
}

sortSelect.addEventListener("change", render);
genderFilter.addEventListener("change", render);

loadDepartments();