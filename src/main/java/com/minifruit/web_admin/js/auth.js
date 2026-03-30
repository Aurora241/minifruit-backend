function saveAuth(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('username', data.username);
    localStorage.setItem('role', data.role);
    localStorage.setItem('branchId', data.branchId);
}

function getUser() {
    return {
        username: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        branchId: localStorage.getItem('branchId'),
    };
}

function requireAuth() {
    if (!getToken()) {
        window.location.href = '/index.html';
    }
}

function logout() {
    localStorage.clear();
    window.location.href = '/index.html';
}