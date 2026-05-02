var deptModal = new bootstrap.Modal(document.getElementById('dept-modal'));
var allDepts = [];

function loadDepartments() {
    fetch('api/departments')
        .then(function(res) {
            if (!res.ok) throw new Error('Server error');
            return res.json();
        })
        .then(function(depts) {
            allDepts = depts;
            renderTable(depts);
        })
        .catch(function() {
            document.getElementById('dept-loading').textContent = 'Could not load departments. Is the server running?';
        });
}

function renderTable(depts) {
    var loading = document.getElementById('dept-loading');
    var wrapper = document.getElementById('dept-table-wrapper');
    var tbody = document.getElementById('dept-tbody');

    loading.classList.add('d-none');
    wrapper.classList.remove('d-none');

    tbody.innerHTML = depts.map(function(d) {
        return '<tr>' +
            '<td>' + d.short_id + '</td>' +
            '<td>' + d.name + '</td>' +
            '<td>' + d.image_path + '</td>' +
            '<td>' + d.professors.length + '</td>' +
            '<td>' +
            '<button class="btn btn-sm btn-action-edit me-1" onclick="openEdit(\'' + d.short_id + '\')">Edit</button>' +
            '<button class="btn btn-sm btn-action-delete" onclick="deleteDept(\'' + d.short_id + '\')">Delete</button>' +
            '</td>' +
            '</tr>';
    }).join('');
}

function openAdd() {
    document.getElementById('dept-edit-mode').value = 'add';
    document.getElementById('dept-modal-label').textContent = 'Add Department';
    document.getElementById('dept-short-id').value = '';
    document.getElementById('dept-short-id').disabled = false;
    document.getElementById('dept-name-input').value = '';
    document.getElementById('dept-image-path').value = '';
    deptModal.show();
}

function openEdit(shortId) {
    var dept = allDepts.find(function(d) { return d.short_id === shortId; });
    if (!dept) return;
    document.getElementById('dept-edit-mode').value = 'edit';
    document.getElementById('dept-modal-label').textContent = 'Edit Department';
    document.getElementById('dept-short-id').value = dept.short_id;
    document.getElementById('dept-short-id').disabled = true;
    document.getElementById('dept-name-input').value = dept.name;
    document.getElementById('dept-image-path').value = dept.image_path;
    deptModal.show();
}

function deleteDept(shortId) {
    if (!confirm('Delete department "' + shortId + '"? This cannot be undone.')) return;
    fetch('api/departments?id=' + encodeURIComponent(shortId), { method: 'DELETE' })
        .then(function(res) {
            if (res.status === 204 || res.ok) loadDepartments();
            else alert('Failed to delete department.');
        })
        .catch(function() { alert('Network error.'); });
}

document.getElementById('add-dept-btn').addEventListener('click', openAdd);

document.getElementById('dept-save-btn').addEventListener('click', function() {
    var mode = document.getElementById('dept-edit-mode').value;
    var shortId = document.getElementById('dept-short-id').value.trim();
    var name = document.getElementById('dept-name-input').value.trim();
    var imagePath = document.getElementById('dept-image-path').value.trim();

    if (!shortId || !name || !imagePath) {
        alert('All fields are required.');
        return;
    }

    var body = { short_id: shortId, name: name, image_path: imagePath };

    if (mode === 'add') {
        fetch('api/departments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(function(res) {
                if (res.status === 201 || res.ok) {
                    deptModal.hide();
                    loadDepartments();
                } else {
                    alert('Failed to add department.');
                }
            })
            .catch(function() { alert('Network error.'); });
    } else {
        fetch('api/departments', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(function(res) {
                if (res.ok) {
                    deptModal.hide();
                    loadDepartments();
                } else {
                    alert('Failed to update department.');
                }
            })
            .catch(function() { alert('Network error.'); });
    }
});

loadDepartments();
