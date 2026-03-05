const API_URL = import.meta.env.VITE_API_URL;

async function http(url, options = {}) {
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });

  // backend devuelve JSON con error
  if (!res.ok) {
    let payload = null;
    try { payload = await res.json(); } catch (_) {}
    const message =
      payload?.error ||
      (typeof payload === "object" ? JSON.stringify(payload) : null) ||
      `HTTP ${res.status}`;
    throw new Error(message);
  }

  if (res.status === 204) return null;
  return res.json();
}

export async function listPatients() {
  return http(`${API_URL}/patients`);
}

export async function getPatient(id) {
  return http(`${API_URL}/patients/${id}`);
}

export async function createPatient(dto) {
  return http(`${API_URL}/patients`, {
    method: "POST",
    body: JSON.stringify(dto),
  });
}

export async function updatePatient(id, dto) {
  return http(`${API_URL}/patients/${id}`, {
    method: "PUT",
    body: JSON.stringify(dto),
  });
}

export async function deletePatient(id) {
  return http(`${API_URL}/patients/${id}`, { method: "DELETE" });
}