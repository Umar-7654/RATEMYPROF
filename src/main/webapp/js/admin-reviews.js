function loadReviews() {
    fetch('api/admin/reviews')
        .then(function(res) {
            return res.json();
        })
        .then(function(reviews) {
            renderReviews(reviews);
        })
        .catch(function() {
            document.getElementById('reviews-loading').textContent = 'Could not load reviews.';
        });
}

function renderReviews(reviews) {
    var loading = document.getElementById('reviews-loading');
    var wrapper = document.getElementById('reviews-table-wrapper');
    var tbody = document.getElementById('reviews-tbody');

    loading.classList.add('d-none');
    wrapper.classList.remove('d-none');

    tbody.innerHTML = reviews.map(function(r) {
        return '<tr>' +
            '<td>' + r.id + '</td>' +
            '<td>' + r.professorName + '</td>' +
            '<td>' + r.studentName + '</td>' +
            '<td>' + r.courseCode + '</td>' +
            '<td>' + r.rating + '</td>' +
            '<td>' + r.difficulty + '</td>' +
            '<td>' + r.reviewText + '</td>' +
            '<td>' +
            '<button class="btn btn-sm btn-action-delete" onclick="deleteReview(' + r.id + ')">Delete</button>' +
            '</td>' +
            '</tr>';
    }).join('');
}

function deleteReview(id) {
    if (!confirm('Delete this review?')) {
        return;
    }

    fetch('api/admin/reviews?id=' + id, {
        method: 'DELETE'
    })
    .then(function(res) {
        if (!res.ok) {
            throw new Error('Delete failed');
        }

        loadReviews();
    })
    .catch(function() {
        alert('Could not delete review.');
    });
}

loadReviews();