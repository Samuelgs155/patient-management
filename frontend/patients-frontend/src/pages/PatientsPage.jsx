import { useEffect, useMemo, useState } from "react";
import PatientFormModal from "../components/PatientFormModal";
import PatientTable from "../components/PatientTable";
import { createPatient, deletePatient, listPatients, updatePatient } from "../api/patientsApi";

export default function PatientsPage() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const [search, setSearch] = useState("");
  const [mode, setMode] = useState("none"); // none | create | edit
  const [selected, setSelected] = useState(null);

  async function refresh() {
    setLoading(true);
    setError("");
    try {
      const data = await listPatients();
      setPatients(data);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    refresh();
  }, []);

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return patients;
    return patients.filter((p) =>
      [p.nombre, p.apellidos, p.dni, p.email, p.telefono].filter(Boolean).some((v) => v.toLowerCase().includes(q))
    );
  }, [patients, search]);

  function openCreate() {
    setSelected(null);
    setMode("create");
    setMsg("");
    setError("");
  }

  function openEdit(p) {
    setSelected(p);
    setMode("edit");
    setMsg("");
    setError("");
  }

  function closeForm() {
    setMode("none");
    setSelected(null);
  }

  async function submitForm(dto) {
    setError("");
    setMsg("");
    try {
      if (mode === "create") {
        await createPatient(dto);
        setMsg("✅ Paciente creado.");
      } else if (mode === "edit" && selected?.id) {
        await updatePatient(selected.id, dto);
        setMsg("✅ Paciente actualizado.");
      }
      closeForm();
      await refresh();
    } catch (e) {
      setError(e.message);
    }
  }

  async function handleDelete(p) {
    const ok = confirm(`¿Borrar paciente ${p.nombre} ${p.apellidos} (DNI ${p.dni})?`);
    if (!ok) return;

    setError("");
    setMsg("");
    try {
      await deletePatient(p.id);
      setMsg("🗑️ Paciente eliminado.");
      await refresh();
    } catch (e) {
      setError(e.message);
    }
  }

  return (
    <div className="container">
      <div className="header">
        <div className="title">
          <h1>Gestión de pacientes</h1>
          <p>CRUD con React + Spring Boot. Busca, crea y actualiza pacientes en segundos.</p>
        </div>

        <button className="btn primary" onClick={openCreate}>
          + Nuevo paciente
        </button>
      </div>

      <div className="card toolbar">
        <div className="row">
          <div className="input">
            <span className="icon">🔎</span>
            <input
              placeholder="Buscar por nombre, apellidos, DNI, email o teléfono..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <button className="btn" onClick={refresh} disabled={loading}>
            {loading ? "Cargando..." : "Recargar"}
          </button>

          <span className="badge">
            <span className="dot" />
            {filtered.length} pacientes
          </span>
        </div>

        {msg && (
          <div className="notice ok">
            {msg} <small>({new Date().toLocaleTimeString()})</small>
          </div>
        )}
        {error && (
          <div className="notice err">
            ❌ {error} <small>(revisa backend/CORS)</small>
          </div>
        )}
      </div>

      <div className="tableWrap">
        <PatientTable patients={filtered} onEdit={openEdit} onDelete={handleDelete} />
      </div>

      {mode !== "none" && (
        <PatientFormModal mode={mode} initial={selected} onCancel={closeForm} onSubmit={submitForm} />
      )}
    </div>
  );
}