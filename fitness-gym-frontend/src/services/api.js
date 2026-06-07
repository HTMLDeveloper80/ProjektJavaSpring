export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

const AUTH_STORAGE_KEY = 'fitcore-user';
let refreshPromise = null;

export function getStoredAuthSession() {
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    return stored ? JSON.parse(stored) : null;
  } catch {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    return null;
  }
}

export function saveAuthSession(session) {
  if (session) {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(session));
  } else {
    localStorage.removeItem(AUTH_STORAGE_KEY);
  }
}

export async function logoutFromApi() {
  const session = getStoredAuthSession();
  try {
    if (session?.refreshToken) {
      await fetch(`${API_BASE_URL}/auth/logout`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken: session.refreshToken })
      });
    }
  } finally {
    saveAuthSession(null);
  }
}

async function parseResponse(response) {
  const text = await response.text();
  const data = text ? safeJson(text) : null;

  return {
    ok: response.ok,
    status: response.status,
    data,
    text
  };
}

function safeJson(text) {
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

async function refreshAccessToken() {
  if (refreshPromise) {
    return refreshPromise;
  }

  refreshPromise = (async () => {
    const session = getStoredAuthSession();
    if (!session?.refreshToken) {
      return false;
    }

    const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken: session.refreshToken })
    });
    const result = await parseResponse(response);

    if (!result.ok || !result.data?.accessToken || !result.data?.refreshToken) {
      saveAuthSession(null);
      window.dispatchEvent(new Event('fitcore-auth-expired'));
      return false;
    }

    saveAuthSession({ ...session, ...result.data });
    window.dispatchEvent(new CustomEvent('fitcore-auth-refreshed', { detail: result.data }));
    return true;
  })().finally(() => {
    refreshPromise = null;
  });

  return refreshPromise;
}

async function request(path, options = {}, retryAfterRefresh = true) {
  try {
    const session = getStoredAuthSession();
    const headers = new Headers(options.headers ?? {});
    if (options.body && !headers.has('Content-Type')) {
      headers.set('Content-Type', 'application/json');
    }
    if (session?.accessToken) {
      headers.set('Authorization', `Bearer ${session.accessToken}`);
    }

    const response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });
    if (response.status === 401 && retryAfterRefresh && !path.startsWith('/auth/')) {
      const refreshed = await refreshAccessToken();
      if (refreshed) {
        return request(path, options, false);
      }
    }
    return parseResponse(response);
  } catch (error) {
    return {
      ok: false,
      networkError: true,
      message: error.message,
      data: null,
      text: ''
    };
  }
}

export function getFromApi(path) {
  return request(path);
}

export function postToApi(path, payload) {
  return request(path, {
    method: 'POST',
    body: JSON.stringify(payload)
  });
}
