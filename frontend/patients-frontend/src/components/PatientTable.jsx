export default function PatientTable({ patients, onEdit, onDelete }) {
  if (!patients.length) return <div className="empty">No hay pacientes.</div>;

  return (
    <table>
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Apellidos</th>
          <th>DNI</th>
          <th>Nacimiento</th>
          <th>Teléfono</th>
          <th>Email</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {patients.map((p) => (
          <tr key={p.id}>
            <td><strong>{p.nombre}</strong></td>
            <td>{p.apellidos}</td>
            <td className="mono">{p.dni}</td>
            <td>{p.fechaNacimiento || "—"}</td>
            <td>{p.telefono || "—"}</td>
            <td>{p.email || "—"}</td>
            <td>
              <div className="actions">
                <button className="btn" onClick={() => onEdit(p)}>Editar</button>
                <button className="btn danger" onClick={() => onDelete(p)}>Borrar</button>
              </div>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}