import { openModal, closeModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from '../services/doctorService.js';
import { createDoctorCard } from '../components/doctorCard.js';

// =======================
// EVENTOS INICIALES
// =======================

// Botón "Add Doctor"
const addDoctorBtn = document.getElementById('addDoctorBtn');
if (addDoctorBtn) {
  addDoctorBtn.addEventListener('click', () => {
    openModal('addDoctor');
  });
}

// Cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
  loadDoctorCards();
});

// =======================
// CARGAR DOCTORES
// =======================
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error('Error loading doctor cards:', error);
  }
}

// =======================
// FILTROS
// =======================
const searchInput = document.getElementById('searchDoctor');
const timeFilter = document.getElementById('filterTime');
const specialtyFilter = document.getElementById('filterSpecialty');

if (searchInput) searchInput.addEventListener('input', filterDoctorsOnChange);
if (timeFilter) timeFilter.addEventListener('change', filterDoctorsOnChange);
if (specialtyFilter) specialtyFilter.addEventListener('change', filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  try {
    const name = searchInput.value.trim() || null;
    const time = timeFilter.value || null;
    const specialty = specialtyFilter.value || null;

    const result = await filterDoctors(name, time, specialty);
    const doctors = result.doctors || [];

    if (doctors.length === 0) {
      const content = document.getElementById('content');
      content.innerHTML = '<p>No doctors found with the given filters.</p>';
      return;
    }

    renderDoctorCards(doctors);

  } catch (error) {
    console.error(error);
    alert('Error filtering doctors');
  }
}

// =======================
// RENDER DOCTORES
// =======================
function renderDoctorCards(doctors) {
  const content = document.getElementById('content');
  content.innerHTML = '';

  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    content.appendChild(card);
  });
}

// =======================
// ADD DOCTOR (ADMIN)
// =======================
window.adminAddDoctor = async function () {
  try {
    const name = document.getElementById('doctorName').value;
    const email = document.getElementById('doctorEmail').value;
    const phone = document.getElementById('doctorPhone').value;
    const password = document.getElementById('doctorPassword').value;
    const specialty = document.getElementById('doctorSpecialty').value;
    const availableTimes = document
      .getElementById('doctorTimes')
      .value.split(',')
      .map(t => t.trim());

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Unauthorized. Please log in again.');
      return;
    }

    const doctor = {
      name,
      email,
      phone,
      password,
      specialty,
      availableTimes
    };

    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert('Doctor added successfully');
      closeModal('addDoctor');
      location.reload();
    } else {
      alert(result.message || 'Error adding doctor');
    }

  } catch (error) {
    console.error(error);
    alert('Unexpected error while adding doctor');
  }
};
