const BASE_URL = 'http://localhost:8080/api';

function getToken() {
    return localStorage.getItem('token');
}

async function request(method, path, body = null) {
    const opts = {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
        }
    };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(BASE_URL + path, opts);
    if (res.status === 403 || res.status === 401) {
        localStorage.clear();
        window.location.href = '/index.html';
        return;
    }
    return res.json();
}

const api = {
    get: (path) => request('GET', path),
    post: (path, body) => request('POST', path, body),
    put: (path, body) => request('PUT', path, body),
    delete: (path) => request('DELETE', path),
};