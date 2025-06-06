// assets/js/api/doctor/edit-profile.js

$(function() {
    let doctorId = null;
    let originalDoctor = null;

    function loadCurrentDoctor() {
        ajaxRequest('http://localhost:8080/api/users/me', 'GET')
            .done(user => {
                doctorId = user.id;
                return ajaxRequest(`http://localhost:8080/api/doctors/${doctorId}`, 'GET');
            })
            .done(doc => {
                originalDoctor = doc;
                $('#input-username').val(doc.username);
                $('#input-email').val(doc.email);
            })
            .fail(xhr => {
                if (xhr.status === 401) {
                    alert('Please log in first.');
                    window.location.replace('../auth/login.html');
                } else if (xhr.status === 404) {
                    alert('Doctor not found.');
                    window.location.replace('profile.html');
                } else {
                    handleError(xhr, 'load doctor details');
                }
            });
    }

    $('#editDoctorForm').on('submit', function(e) {
        e.preventDefault();

        const username = $('#input-username').val().trim();
        const email = $('#input-email').val().trim();

        let valid = true;
        if (!username) {
            $('#input-username').addClass('is-invalid');
            valid = false;
        } else {
            $('#input-username').removeClass('is-invalid');
        }
        if (!email) {
            $('#input-email').addClass('is-invalid');
            valid = false;
        } else {
            $('#input-email').removeClass('is-invalid');
        }
        if (!valid) return;

        const payload = {
            id: originalDoctor.id,
            username: username,
            email: email,
            specialization: originalDoctor.specialization,
            licenseNumber: originalDoctor.licenseNumber,
            clinicLocation: originalDoctor.clinicLocation,
            password: originalDoctor.password  
        };

        ajaxRequest(`http://localhost:8080/api/doctors/${doctorId}`, 'PUT', payload)
            .done(updated => {
                alert('Profile updated successfully.');
                window.location.replace('profile.html');
            })
            .fail(xhr => {
                if (xhr.status === 401) {
                    alert('You must be logged in.');
                    window.location.replace('../auth/login.html');
                } else if (xhr.status === 404) {
                    alert('Doctor not found.');
                    window.location.replace('profile.html');
                } else {
                    handleError(xhr, 'update profile');
                }
            });
    });

    loadCurrentDoctor();
});
