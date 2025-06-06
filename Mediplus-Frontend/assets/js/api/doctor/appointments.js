// assets/js/api/doctor/appointments.js
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
                const filtered = appts.filter(a => a.doctorUsername === username);
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
                    <td colspan="4" class="text-center text-muted">No appointment requests at the moment.</td>
                </tr>
            `);
            return;
        }

        appointments.forEach(appt => {
            const id = appt.id;
            const patient = appt.patientUsername;
            const dt = new Date(appt.dateTime);
            const formatted = dt.toLocaleDateString('en-GB', {
                day: '2-digit', month: '2-digit', year: 'numeric',
                hour: '2-digit', minute: '2-digit'
            }).replace(',', '');
            const status = appt.status;
            const badge = badgeClass(status);

            let actions = '';
            // الطبيب يتعامل مع الطلبات ذات الحالة SCHEDULED
            if (status === 'SCHEDULED') {
                actions = `
                    <button data-id="${id}" class="btn btn-sm btn-success accept-btn me-2">
                        <i class="icofont-check"></i> Accept
                    </button>
                    <button data-id="${id}" class="btn btn-sm btn-danger decline-btn">
                        <i class="icofont-close"></i> Decline
                    </button>
                `;
            }

            tbody.append(`
                <tr>
                    <td>${patient}</td>
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

        $('.accept-btn').on('click', function() {
            const apptId = $(this).data('id');
            if (confirm('Confirm this appointment?')) {
                ajaxRequest(`${APPTS_API_URL}/${apptId}/status?status=CONFIRMED`, 'PATCH')
                    .done(() => loadAppointments())
                    .fail(xhr => handleError(xhr, 'confirm appointment'));
            }
        });

        $('.decline-btn').on('click', function() {
            const apptId = $(this).data('id');
            if (confirm('Decline this appointment?')) {
                ajaxRequest(`${APPTS_API_URL}/${apptId}/status?status=CANCELLED`, 'PATCH')
                    .done(() => loadAppointments())
                    .fail(xhr => handleError(xhr, 'decline appointment'));
            }
        });
    }

    loadAppointments();
});