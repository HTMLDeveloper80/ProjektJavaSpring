export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

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

export async function getFromApi(path) {
  try {
    const response = await fetch(`${API_BASE_URL}${path}`);
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

export async function postToApi(path, payload) {
  try {
    const response = await fetch(`${API_BASE_URL}${path}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
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
