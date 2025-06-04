// ======================== assets/js/api/auth/register.js ========================

const REGISTER_API_URL = "http://localhost:8080/api/patients/register";

$(function() {
    $('#registerForm').on('submit', function(e) {
        e.preventDefault();

        const username        = $('#username').val().trim();
        const email           = $('#regEmail').val().trim();
        const password        = $('#password').val();
        const confirmPassword = $('#confirm-password').val();
        const dateOfBirth     = $('#dateOfBirth').val();
        const termsAccepted   = $('#terms').is(':checked');

        if (password !== confirmPassword) {
            return alert("Passwords do not match");
        }
        if (!termsAccepted) {
            return alert("You must accept the terms and conditions");
        }

        const payload = {
            username,
            email,
            password,
            dateOfBirth,
            termsAccepted
        };

        ajaxRequest(REGISTER_API_URL, 'POST', payload)
            .done(() => {
                alert("Registration successful");
                window.location.replace("login.html");
            })
            .fail(xhr => handleError(xhr, 'register'));
    });
});
