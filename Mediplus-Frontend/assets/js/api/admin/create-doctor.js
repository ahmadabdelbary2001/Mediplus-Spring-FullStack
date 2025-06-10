// assets/js/api/admin/create-doctor.js

$(function() {
    const user = getUser();
    if (!user || user.role !== 'ADMIN') {
        alert('Access denied: Admins only.');
        window.location.replace('../auth/login.html');
        return;
    }

    const CREATE_URL = 'http://localhost:8080/api/doctors';

    function validateField($input, minLength = 1) {
        const val = $input.val().trim();
        if (val.length < minLength) {
            $input.addClass('is-invalid');
            return false;
        } else {
            $input.removeClass('is-invalid');
            return true;
        }
    }

    $('#createDoctorForm').on('submit', function(e) {
        e.preventDefault();

        const $username = $('#username');
        const $email = $('#email');
        const $password = $('#password');
        const $specialization = $('#specialization');
        const $license = $('#licenseNumber');
        const $clinic = $('#clinicLocation');
        const $terms = $('#termsAccepted');

        const validUsername = validateField($username, 3);
        const validEmail   = validateField($email, 5); // Assume simple length check; server will validate format
        const validPassword = validateField($password, 6);
        const validSpec     = validateField($specialization, 1);
        const validLicense  = validateField($license, 1);
        const validClinic   = validateField($clinic, 1);
        const termsOK       = $terms.is(':checked');

        if (!termsOK) {
            alert('You must accept the terms and conditions.');
            return;
        }

        if (!(validUsername && validEmail && validPassword && validSpec && validLicense && validClinic)) {
            return; // Some fields invalid
        }

        const payload = {
            username: $username.val().trim(),
            email: $email.val().trim(),
            password: $password.val(),
            specialization: $specialization.val().trim(),
            licenseNumber: $license.val().trim(),
            clinicLocation: $clinic.val().trim(),
            termsAccepted: true
        };

        ajaxRequest(CREATE_URL, 'POST', payload)
            .done(created => {
                alert(`Doctor "${created.username}" was created successfully.`);
                window.location.replace('users-doctors.html');
            })
            .fail(xhr => handleError(xhr, 'create doctor'));
    });
});
