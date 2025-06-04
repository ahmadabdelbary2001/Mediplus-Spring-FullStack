// ======================== assets/js/api/index.js ========================

$(function() {
    $('#getStarted').on('click', function() {
        const raw = sessionStorage.getItem("user");
        if (!raw) {
            window.location.href = "auth/login.html";
            return;
        }

        let u;
        try {
            u = JSON.parse(raw);
        } catch (e) {
            window.location.href = "auth/login.html";
            return;
        }

        switch (u.role) {
            case "DOCTOR":
                window.location.href = "doctor/dashboard.html";
                break;
            case "PATIENT":
                window.location.href = "patient/dashboard.html";
                break;
            case "ADMIN":
                window.location.href = "admin/dashboard.html";
                break;
            default:
                window.location.href = "auth/login.html";
        }
    });
});