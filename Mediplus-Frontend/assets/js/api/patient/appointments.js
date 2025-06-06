// assets/js/api/patient/appointments.js
$(function() {
    const APPTS_API_URL = 'http://localhost:8080/api/appointments';

    function getCurrentUsername() {
        const raw = sessionStorage.getItem('user');
        if (!raw) return null;
        try {
            const u = JSON.parse(raw);
            return u.username || null;
        } catch {
            return null;
        }
    }

    function badgeClass(status) {
        if (status === 'PENDING' || status === 'SCHEDULED') return 'bg-warning text-dark';
        if (status === 'CONFIRMED') return 'bg-success';
        return 'bg-danger';
    }

    function loadAppointments() {
        const username = getCurrentUsername();
        if (!username) {
            alert('Please log in first.');
            window.location.href = '../auth/login.html';
            return;
        }

        ajaxRequest(APPTS_API_URL, 'GET')
            .done(appts => {
                const filtered = appts.filter(a => a.patientUsername === username);
                renderTable(filtered);
            })
            .fail(xhr => handleError(xhr, 'load appointments'));
    }

    function renderTable(appointments) {
        const tbody = $('#apptBody');
        tbody.empty();

        if (appointments.length === 0) {
            tbody.append(`
                <tr>
                    <td colspan="4" class="text-center text-muted">No appointments found.</td>
                </tr>
            `);
            return;
        }

        appointments.forEach(appt => {
            const id = appt.id;
            const doctor = appt.doctorUsername;
            const dt = new Date(appt.dateTime);
            const formatted = dt.toLocaleDateString('en-GB', {
                day: '2-digit', month: '2-digit', year: 'numeric',
                hour: '2-digit', minute: '2-digit'
            }).replace(',', '');
            const status = appt.status;
            const badge = badgeClass(status);

            let actions = '';
            // Patient can edit/cancel only when status is SCHEDULED
            if (status === 'SCHEDULED') {
                actions = `
                    <a href="edit-appointment.html?id=${id}"
                       class="btn btn-sm btn-outline-primary me-2">
                        <i class="icofont-edit"></i> Edit
                    </a>
                    <button data-id="${id}" class="btn btn-sm btn-outline-danger cancel-btn">
                        <i class="icofont-close"></i> Cancel
                    </button>
                `;
            }

            tbody.append(`
                <tr>
                    <td>${doctor}</td>
                    <td>${formatted}</td>
                    <td>
                      <span class="badge ${badge}">${status}</span>
                    </td>
                    <td class="text-end">
                      ${actions}
                    </td>
                </tr>
            `);
        });

        $('.cancel-btn').on('click', function() {
            const apptId = $(this).data('id');
            if (confirm('Are you sure you want to cancel this appointment?')) {
                ajaxRequest(`${APPTS_API_URL}/${apptId}`, 'DELETE')
                    .done(() => loadAppointments())
                    .fail(xhr => handleError(xhr, 'cancel appointment'));
            }
        });
    }

    loadAppointments();
});
