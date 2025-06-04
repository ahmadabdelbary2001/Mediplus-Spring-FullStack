// ======================== assets/js/api/auth/login.js ========================

$(function() {
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();

        const username = $('#username').val().trim();
        const password = $('#password').val().trim();
        console.log("[login.js] Attempting login for:", username);

        $.ajax({
            url: 'http://localhost:8080/authenticate',
            method: 'POST',
            data: { username, password },
            xhrFields: { withCredentials: true },
            success: () => {
                console.log("[login.js] Login successful, checking document.cookie:");
                console.log("           document.cookie =", document.cookie);

                console.log("[login.js] Now requesting /api/users/me ...");
                ajaxRequest('http://localhost:8080/api/users/me', 'GET')
                    .done(user => {
                        console.log("[login.js] /api/users/me response:", user);
                        sessionStorage.setItem('user', JSON.stringify(user));
                        console.log("[login.js] Stored user in sessionStorage:", getUser());

                        if (user.role === "DOCTOR") {
                            window.location.href = "../doctor/dashboard.html";
                        } else if (user.role === "PATIENT") {
                            window.location.href = "../patient/dashboard.html";
                        } else if (user.role === "ADMIN") {
                            window.location.href = "../admin/dashboard.html";
                        } else {
                            window.location.href = "index.html";
                        }
                    })
                    .fail(xhr => {
                        console.error("[login.js] GET /api/users/me failed:", xhr.status, xhr.responseText);
                        handleError(xhr, 'fetch user profile');
                    });
            },
            error: xhr => {
                console.error("[login.js] POST /authenticate failed:", xhr.status, xhr.responseText);
                handleError(xhr, 'login');
            }
        });
    });
});
