// ==================== assets/js/api/utils.js =====================

// ======================== AJAX Handling ========================
function ajaxRequest(url, method, data, headers = {}) {
    return $.ajax({
        url,
        method,
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : undefined,
        headers: {
            ...headers,
            'X-Requested-With': 'XMLHttpRequest'
        },
        xhrFields: { withCredentials: true },
        crossDomain: true
    });
}

// ======================== Error Handling ========================
function handleError(xhr, action) {
    const msgs = {
        0:   'Network error – check connection',
        400: 'Bad request – invalid data',
        401: 'Unauthorized – please login',
        403: 'Forbidden – access denied',
        404: 'Resource not found',
        500: 'Server error – try later'
    };
    const m = msgs[xhr.status] || `Unknown error (${xhr.status})`;
    alert(`Failed to ${action}: ${m}`);
}

// ========================= Auth Helpers =========================
function getUser() {
    const raw = sessionStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
}
function isLoggedIn() {
    return Boolean(getUser());
}
function isAdminLoggedIn() {
    const u = getUser();
    return u && u.role === 'ADMIN';
}
function logout() {
    sessionStorage.clear();
    window.location.replace('login.html');
}

// ==================== assets/js/api/utils.js =====================

// ======================== AJAX Handling ========================
function ajaxRequest(url, method, data, headers = {}) {
    return $.ajax({
        url,
        method,
        contentType: 'application/json',
        data: data ? JSON.stringify(data) : undefined,
        headers: {
            ...headers,
            'X-Requested-With': 'XMLHttpRequest'
        },
        xhrFields: { withCredentials: true },
        crossDomain: true
    });
}

// ======================== Error Handling ========================
function handleError(xhr, action) {
    const msgs = {
        0:   'Network error – check connection',
        400: 'Bad request – invalid data',
        401: 'Unauthorized – please login',
        403: 'Forbidden – access denied',
        404: 'Resource not found',
        500: 'Server error – try later'
    };
    const m = msgs[xhr.status] || `Unknown error (${xhr.status})`;
    alert(`Failed to ${action}: ${m}`);
}

// ========================= Auth Helpers =========================
function getUser() {
    const raw = sessionStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
}
function isLoggedIn() {
    return Boolean(getUser());
}
function isAdminLoggedIn() {
    const u = getUser();
    return u && u.role === 'ADMIN';
}
function logout() {
    sessionStorage.clear();
    window.location.replace('login.html');
}

// ====================== Require Login Auto-Check ======================
$(function() {
    const requiresLogin = document.body.dataset.requiresLogin === 'true';
    if (requiresLogin && !isLoggedIn()) {
        window.location.replace('auth/login.html');
    }
});

// ====================== UI Initialization ======================
$(function() {
    // bind logout
    $('#logout-button').on('click', e => {
        e.preventDefault();
        logout();
    });
});