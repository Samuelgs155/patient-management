import { useEffect, useState } from "react";

const empty = {
  nombre: "",
  apellidos: "",
  dni: "",
  fechaNacimiento: "",
  telefono: "",
  email: "",
};

export default function PatientFormModal({ mode, initial, onCancel, onSubmit }) {
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
    <div className="modalOverlay" onMouseDown={onCancel}>
      <div className="modal" onMouseDown={(e) => e.stopPropagation()}>
        <div className="modalHeader">
          <div>
            <h3>{mode === "edit" ? "Editar paciente" : "Nuevo paciente"}</h3>
            <p>Completa los datos y guarda los cambios.</p>
          </div>
          <button className="btn" onClick={onCancel}>✕</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="modalBody">
            <div className="grid">
              <div className="field">
                <label>Nombre *</label>
                <input name="nombre" value={form.nombre} onChange={updateField} />
              </div>

              <div className="field">
                <label>Apellidos *</label>
                <input name="apellidos" value={form.apellidos} onChange={updateField} />
              </div>

              <div className="field">
                <label>DNI *</label>
                <input name="dni" value={form.dni} onChange={updateField} />
              </div>

              <div className="field">
                <label>Fecha nacimiento</label>
                <input type="date" name="fechaNacimiento" value={form.fechaNacimiento || ""} onChange={updateField} />
              </div>

              <div className="field">
                <label>Teléfono</label>
                <input name="telefono" value={form.telefono} onChange={updateField} />
              </div>

              <div className="field">
                <label>Email</label>
                <input name="email" value={form.email} onChange={updateField} />
              </div>
            </div>

            {error && <div className="notice err" style={{ marginTop: 12 }}>❌ {error}</div>}
          </div>

          <div className="modalFooter">
            <button type="button" className="btn" onClick={onCancel}>
              Cancelar
            </button>
            <button type="submit" className="btn primary">
              {mode === "edit" ? "Guardar cambios" : "Crear"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}