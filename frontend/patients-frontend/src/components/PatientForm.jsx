import { useEffect, useState } from "react";

const empty = {
  nombre: "",
  apellidos: "",
  dni: "",
  fechaNacimiento: "",
  telefono: "",
  email: "",
};

export default function PatientForm({ mode, initial, onCancel, onSubmit }) {
  const [form, setForm] = useState(empty);
  const [error, setError] = useState("");

  useEffect(() => {
    setForm(initial ? { ...empty, ...initial } : empty);
    setError("");
  }, [initial, mode]);

  function updateField(e) {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
  }

  function validate() {
    if (!form.nombre.trim()) return "El nombre es obligatorio";
    if (!form.apellidos.trim()) return "Los apellidos son obligatorios";
    if (!form.dni.trim()) return "El DNI es obligatorio";
    if (form.email && !/^\S+@\S+\.\S+$/.test(form.email)) return "Email no válido";
    return "";
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const v = validate();
    if (v) return setError(v);

    setError("");
    await onSubmit({
      nombre: form.nombre.trim(),
      apellidos: form.apellidos.trim(),
      dni: form.dni.trim(),
      fechaNacimiento: form.fechaNacimiento || null,
      telefono: form.telefono.trim() || null,
      email: form.email.trim() || null,
    });
  }

  return (
    <form onSubmit={handleSubmit} style={{ border: "1px solid #ddd", padding: 12, borderRadius: 8, marginTop: 12 }}>
      <h3 style={{ marginTop: 0 }}>{mode === "edit" ? "Editar paciente" : "Nuevo paciente"}</h3>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
        <label>
          Nombre*
          <input name="nombre" value={form.nombre} onChange={updateField} style={{ width: "100%" }} />
        </label>

        <label>
          Apellidos*
          <input name="apellidos" value={form.apellidos} onChange={updateField} style={{ width: "100%" }} />
        </label>

        <label>
          DNI*
          <input name="dni" value={form.dni} onChange={updateField} style={{ width: "100%" }} />
        </label>

        <label>
          Fecha nacimiento
          <input type="date" name="fechaNacimiento" value={form.fechaNacimiento || ""} onChange={updateField} style={{ width: "100%" }} />
        </label>

        <label>
          Teléfono
          <input name="telefono" value={form.telefono} onChange={updateField} style={{ width: "100%" }} />
        </label>

        <label>
          Email
          <input name="email" value={form.email} onChange={updateField} style={{ width: "100%" }} />
        </label>
      </div>

      {error && <p style={{ color: "crimson", marginTop: 10 }}>{error}</p>}

      <div style={{ marginTop: 12 }}>
        <button type="submit" style={{ marginRight: 8 }}>
          {mode === "edit" ? "Guardar cambios" : "Crear"}
        </button>
        <button type="button" onClick={onCancel}>
          Cancelar
        </button>
      </div>
    </form>
  );
}