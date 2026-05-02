var profModal = new bootstrap.Modal(document.getElementById('prof-modal'));
var allDepts = [];
var currentDeptId = '';

function loadDepartments() {
    fetch('api/departments')
        .then(function(res) {
            if (!res.ok) throw new Error('Server error');
            return res.json();
        })
        .then(function(depts) {
            allDepts = depts;
            var picker = document.getElementById('dept-picker');
            picker.innerHTML = '<option value="">— select a department —</option>' +
                depts.map(function(d) {
                    return '<option value="' + d.short_id + '">' + d.short_id + ' — ' + d.name + '</option>';
                }).join('');
        })
        .catch(function() {
            alert('Could not load departments. Is the server running?');
        });
}

function renderProfessors(deptId) {
    var dept = allDepts.find(function(d) { return d.short_id === deptId; });
    var wrapper = document.getElementById('prof-table-wrapper');
    var tbody = document.getElementById('prof-tbody');

    if (!dept) {
        wrapper.classList.add('d-none');
        return;
    }

    wrapper.classList.remove('d-none');
    tbody.innerHTML = dept.professors.map(function(p) {
        return '<tr>' +
            '<td>' + p.id + '</td>' +
            '<td>' + p.name + '</td>' +
            '<td>' + (p.gender === 'M' ? 'Male' : 'Female') + '</td>' +
            '<td>' + p.rating.toFixed(1) + '</td>' +
            '<td>' + p.reviews + '</td>' +
            '<td>' +
            '<button class="btn btn-sm btn-action-edit me-1" onclick="openEditProf(\'' + p.id + '\')">Edit</button>' +
            '<button class="btn btn-sm btn-action-delete" onclick="deleteProf(\'' + p.id + '\')">Delete</button>' +
            '</td>' +
            '</tr>';
    }).join('');
}

function refreshCurrentDept() {
    fetch('api/departments')
        .then(function(res) { return res.json(); })
        .then(function(depts) {
            allDepts = depts;
            renderProfessors(currentDeptId);
        });
}

function openAddProf() {
    if (!currentDeptId) {
        alert('Please select a department first.');
        return;
    }
    document.getElementById('prof-edit-mode').value = 'add';
    document.getElementById('prof-modal-label').textContent = 'Add Professor';
    document.getElementById('prof-id-input').value = '';
    document.getElementById('prof-id-input').disabled = false;
    document.getElementById('prof-name-input').value = '';
    document.getElementById('prof-gender-input').value = 'M';
    document.getElementById('prof-rating-input').value = '';
    document.getElementById('prof-reviews-input').value = '';
    profModal.show();
}

function openEditProf(profId) {
    var dept = allDepts.find(function(d) { return d.short_id === currentDeptId; });
    if (!dept) return;
    var prof = dept.professors.find(function(p) { return p.id === profId; });
    if (!prof) return;
    document.getElementById('prof-edit-mode').value = 'edit';
    document.getElementById('prof-modal-label').textContent = 'Edit Professor';
    document.getElementById('prof-id-input').value = prof.id;
    document.getElementById('prof-id-input').disabled = true;
    document.getElementById('prof-name-input').value = prof.name;
    document.getElementById('prof-gender-input').value = prof.gender;
    document.getElementById('prof-rating-input').value = prof.rating;
    document.getElementById('prof-reviews-input').value = prof.reviews;
    profModal.show();
}

function deleteProf(profId) {
    if (!confirm('Delete professor "' + profId + '"? This cannot be undone.')) return;
    fetch('api/professors?dept=' + encodeURIComponent(currentDeptId) + '&id=' + encodeURIComponent(profId), { method: 'DELETE' })
        .then(function(res) {
            if (res.status === 204 || res.ok) refreshCurrentDept();
            else alert('Failed to delete professor.');
        })
        .catch(function() { alert('Network error.'); });
}

document.getElementById('add-prof-btn').addEventListener('click', openAddProf);

document.getElementById('dept-picker').addEventListener('change', function() {
    currentDeptId = this.value;
    renderProfessors(currentDeptId);
});

document.getElementById('prof-save-btn').addEventListener('click', function() {
    var mode = document.getElementById('prof-edit-mode').value;
    var id = document.getElementById('prof-id-input').value.trim();
    var name = document.getElementById('prof-name-input').value.trim();
    var gender = document.getElementById('prof-gender-input').value;
    var rating = parseFloat(document.getElementById('prof-rating-input').value);
    var reviews = parseInt(document.getElementById('prof-reviews-input').value, 10);

    if (!id || !name || isNaN(rating) || isNaN(reviews)) {
        alert('All fields are required.');
        return;
    }

    var body = {
        dept_short_id: currentDeptId,
        professor: { id: id, name: name, gender: gender, dept_short_id: currentDeptId, rating: rating, reviews: reviews }
    };

    var method = mode === 'add' ? 'POST' : 'PUT';
    fetch('api/professors', {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    })
        .then(function(res) {
            if (res.ok || res.status === 201) {
                profModal.hide();
                refreshCurrentDept();
            } else {
                alert('Failed to save professor.');
            }
        })
        .catch(function() { alert('Network error.'); });
});

loadDepartments();
