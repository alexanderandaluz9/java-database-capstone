import { getAllAppointments } from '../services/appointmentService.js';
import { createPatientRow } from '../components/patientRow.js';

// =======================
// VARIABLES INICIALES
// =======================

const tableBody = document.getElementById('appointmentsTableBody');

// Fecha de hoy en formato YYYY-MM-DD
let selectedDate = new Date().toISOString().split('T')[0];

// Token guardado (auth)
const token = localStorage.getItem('token');

// Filtro por nombre de paciente
let patientName = null;

// =======================
// EVENTOS DE FILTRO
// =======================

// Buscador por nombre
const searchInput = document.getElementById('searchPatient');
if (searchInput) {
  searchInput.addEventListener('input', () => {
    const value = searchInput.value.trim();
    patientName = value !== '' ? value : null;
    loadAppointments();
  });
}

// Botón "Today"
const todayBtn = document.getElementById('todayBtn');
const datePicker = document.getElementById('datePicker');

if (todayBtn) {
  todayBtn.addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    datePicker.value = selectedDate;
    loadAppointments();
  });
}

// Selector de fecha
if (datePicker) {
  datePicker.value = selectedDate;
  datePicker.addEventListener('change', () => {
    selectedDate = datePicker.value;
    loadAppointments();
  });
}

// =======================
// CARGAR APPOINTMENTS
// =======================
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(
      selectedDate,
      patientName,
      token
    );

    tableBody.innerHTML = '';

    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="4">No Appointments found for today.</td>
        </tr>
      `;
      return;
    }

    appointments.forEach(app => {
      const patient = {
        id: app.patient.id,
        name: app.patient.name,
        phone: app.patient.phone,
        email: app.patient.email
      };

      const row = createPatientRow(patient, app);
      tableBody.appendChild(row);
    });

  } catch (error) {
    console.error(error);
    tableBody.innerHTML = `
      <tr>
        <td colspan="4">Error loading appointments. Try again later.</td>
      </tr>
    `;
  }
}

// =======================
// INIT
// =======================
document.addEventListener('DOMContentLoaded', () => {
  if (typeof renderContent === 'function') {
    renderContent();
  }
  loadAppointments();
});
