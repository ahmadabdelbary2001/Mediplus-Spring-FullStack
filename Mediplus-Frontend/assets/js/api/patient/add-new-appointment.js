// assets/js/api/patient/add-new-appointment.js

$(function() {
    const DOCTORS_API_URL = 'http://localhost:8080/api/doctors';
    const CREATE_APPT_URL = 'http://localhost:8080/api/appointments';

    function loadDoctors() {
        ajaxRequest(DOCTORS_API_URL, 'GET')
        .done(doctors => {
            const $select = $('#doctor').empty().append('<option value="">Select a Doctor</option>');
            doctors.forEach(doc => {
                $select.append(`<option value="${doc.username}">${doc.username} - ${doc.specialization}</option>`);
            });
        })
        .fail(xhr => handleError(xhr, 'load doctors'));
    }

    $('#addAppointmentForm').on('submit', function(e) {
        e.preventDefault();

        const dateTimeLocal = $('#dateTime').val();
        const doctorUsername = $('#doctor').val();
        const chosenDate = dateTimeLocal && new Date(dateTimeLocal);
        const now = new Date();

        if (!dateTimeLocal) {
            $('#dateTime').addClass('is-invalid');
            return;
        }
        if (chosenDate < now) {
            alert('Cannot book an appointment in the past.');
            return;
        }
        $('#dateTime').removeClass('is-invalid');

        if (!doctorUsername) {
            $('#doctor').addClass('is-invalid');
            return;
        }
        $('#doctor').removeClass('is-invalid');

        const user = getUser();
        if (!user) {
            alert('Please log in first.');
            window.location.href = 'auth/login.html';
            return;
        }

        const payload = {
            dateTime: chosenDate.toISOString(),
            status: 'SCHEDULED',
            patientUsername: user.username,
            doctorUsername
        };

        ajaxRequest(CREATE_APPT_URL, 'POST', payload)
        .done(() => {
            alert('Appointment created successfully.');
            window.location.href = 'appointments.html';
        })
        .fail(xhr => handleError(xhr, 'create appointment'));
    });

    loadDoctors();
});
