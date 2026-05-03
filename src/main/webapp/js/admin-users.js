function loadUsers() {
    fetch('api/admin/users')
        .then(function(res) {
            return res.json();
        })
        .then(function(users) {
            renderUsers(users);
        })
        .catch(function() {
            document.getElementById('users-loading').textContent = 'Could not load users.';
        });
}

function renderUsers(users) {
    var loading = document.getElementById('users-loading');
    var wrapper = document.getElementById('users-table-wrapper');
    var tbody = document.getElementById('users-tbody');

    loading.classList.add('d-none');
    wrapper.classList.remove('d-none');

    tbody.innerHTML = users.map(function(u) {
        return '<tr>' +
            '<td>' + u.id + '</td>' +
            '<td>' + u.fullName + '</td>' +
            '<td>' + u.email + '</td>' +
            '<td>' + u.userType + '</td>' +
            '<td>' +
            '<button class="btn btn-sm btn-action-delete" onclick="deleteUser(\'' + u.id + '\')">Delete</button>' +
            '</td>' +
            '</tr>';
    }).join('');
}

function deleteUser(id) {
    if (!confirm('Delete this user?')) {
        return;
    }

    fetch('api/admin/users?id=' + id, {
        method: 'DELETE'
    })
    .then(function(res) {
        if (!res.ok) {
            throw new Error('Delete failed');
        }

        loadUsers();
    })
    .catch(function() {
        alert('Could not delete user.');
    });
}

loadUsers();