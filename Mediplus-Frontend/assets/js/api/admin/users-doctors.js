// assets/js/api/admin/users-doctors.js

$(function() {
    const user = getUser();
    if (!user || user.role !== 'ADMIN') {
        alert('Authorization denied: You must be responsible.');
        window.location.replace('../auth/login.html');
        return;
    }

    const DOCTORS_URL = 'http://localhost:8080/api/doctors';

    function loadDoctors() {
        ajaxRequest(DOCTORS_URL, 'GET')
            .done(doctors => {
                const $tbody = $('#doctorsTable tbody');
                $tbody.empty();
                doctors.forEach(doc => {
                    const row = `
                        <tr data-id="${doc.id}">
                            <td>${doc.id}</td>
                            <td>${doc.username}</td>
                            <td>${doc.email}</td>
                            <td>${doc.specialization}</td>
                            <td>${doc.licenseNumber}</td>
                            <td>${doc.clinicLocation}</td>
                            <td>
                                <a href="edit-doctor.html?id=${doc.id}" class="btn btn-sm btn-warning me-1">Edit</a>
                                <button class="btn btn-sm btn-danger delete-btn">Delete</button>
                            </td>
                        </tr>`;
                    $tbody.append(row);
                });
            })
            .fail(xhr => handleError(xhr, 'load doctors'));
    }

    $('#doctorsTable').on('click', '.delete-btn', function() {
        const $tr = $(this).closest('tr');
        const id = $tr.data('id');
        if (confirm('Are you sure you want to delete this doctor?')) {
            ajaxRequest(`${DOCTORS_URL}/${id}`, 'DELETE')
                .done(() => {
                    alert('The doctor has been successfully deleted.');
                    loadDoctors();
                })
                .fail(xhr => handleError(xhr, 'delete doctor'));
        }
    });

    loadDoctors();
});
