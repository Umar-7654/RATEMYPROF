var allDepartments = [];

function ratingBadge(r) {
    var cls = 'badge-red';
    if (r >= 4) cls = 'badge-green';
    else if (r >= 3) cls = 'badge-yellow';
    return '<span class="rating-badge ' + cls + '">' + r.toFixed(1) + '</span>';
}

function buildSection(dept, profs) {
    var rows = profs.map(function(p, i) {
        return '<tr>' +
            '<td>' + (i + 1) + '</td>' +
            '<td>' + p.name + '</td>' +
            '<td>' + (p.gender === 'M' ? 'Male' : 'Female') + '</td>' +
            '<td>' + ratingBadge(p.rating) + '</td>' +
            '<td>' + p.reviews + '</td>' +
            '<td><a href="teacher.html?id=' + p.id + '" class="btn btn-sm btn-view">View</a></td>' +
            '</tr>';
    }).join('');

    var cards = profs.map(function(p) {
        return '<div class="prof-card mb-3 p-3">' +
            '<h5 class="mb-1">' + p.name + '</h5>' +
            '<p class="mb-1"><strong>Gender:</strong> ' + (p.gender === 'M' ? 'Male' : 'Female') + '</p>' +
            '<p class="mb-1"><strong>Rating:</strong> ' + ratingBadge(p.rating) + '</p>' +
            '<p class="mb-2"><strong>Reviews:</strong> ' + p.reviews + '</p>' +
            '<a href="teacher.html?id=' + p.id + '" class="btn btn-sm btn-view">View</a>' +
            '</div>';
    }).join('');

    return '<div class="dept-section mb-5">' +
        '<h2 id="dept-' + dept.short_id + '" class="mb-3">' + dept.name + ' (' + dept.short_id + ')</h2>' +
        '<div class="table-responsive d-none d-md-block">' +
        '<table class="table table-hover align-middle">' +
        '<thead><tr>' +
        '<th>#</th><th>Professor Name</th><th>Gender</th><th>Average Rating</th><th>Reviews</th><th></th>' +
        '</tr></thead>' +
        '<tbody id="prof-tbody-' + dept.short_id + '">' + rows + '</tbody>' +
        '</table>' +
        '</div>' +
        '<div id="prof-cards-' + dept.short_id + '" class="d-md-none">' + cards + '</div>' +
        '</div>';
}

function applyFilters() {
    var sort = document.getElementById('sort-select').value;
    var gender = document.getElementById('gender-filter').value;
    var container = document.getElementById('departments-container');
    container.innerHTML = allDepartments.map(function(dept) {
        var profs = dept.professors.slice();
        if (gender !== 'all') profs = profs.filter(function(p) { return p.gender === gender; });
        if (sort === 'rating-desc') profs.sort(function(a, b) { return b.rating - a.rating; });
        else if (sort === 'rating-asc') profs.sort(function(a, b) { return a.rating - b.rating; });
        else if (sort === 'reviews-desc') profs.sort(function(a, b) { return b.reviews - a.reviews; });
        else if (sort === 'reviews-asc') profs.sort(function(a, b) { return a.reviews - b.reviews; });
        return buildSection(dept, profs);
    }).join('');
}

fetch('api/departments')
    .then(function(res) {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    })
    .then(function(departments) {
        allDepartments = departments;
        document.getElementById('dept-loading').remove();
        applyFilters();

        var params = new URLSearchParams(window.location.search);
        var target = params.get('dept');
        if (target) {
            var el = document.getElementById('dept-' + target);
            if (el) el.scrollIntoView({ behavior: 'smooth' });
        }
    })
    .catch(function() {
        document.getElementById('dept-loading').textContent = 'Could not load departments. Is the server running?';
    });

document.getElementById('sort-select').addEventListener('change', applyFilters);
document.getElementById('gender-filter').addEventListener('change', applyFilters);
