// Importar base URL
import { API_BASE_URL } from '../config/config.js';

// Endpoint base de doctor
const DOCTOR_API = API_BASE_URL + '/doctor';

// =======================
// GET ALL DOCTORS
// =======================
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);

    const data = await response.json();
    return data.doctors || [];

  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
}

// =======================
// DELETE DOCTOR
// =======================
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(
      `${DOCTOR_API}/delete/${doctorId}/${token}`,
      {
        method: 'DELETE'
      }
    );

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message
    };

  } catch (error) {
    console.error('Error deleting doctor:', error);
    return {
      success: false,
      message: 'Error deleting doctor'
    };
  }
}

// =======================
// SAVE DOCTOR
// =======================
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(
      `${DOCTOR_API}/save/${token}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(doctor)
      }
    );

    const data = await response.json();

    return {
      success: response.ok,
      message: data.message
    };

  } catch (error) {
    console.error('Error saving doctor:', error);
    return {
      success: false,
      message: 'Error saving doctor'
    };
  }
}

// =======================
// FILTER DOCTORS
// =======================
export async function filterDoctors(name, time, specialty) {
  try {
    const response = await fetch(
      `${DOCTOR_API}/filter/${name}/${time}/${specialty}`
    );

    if (!response.ok) {
      console.error('Filter request failed');
      return { doctors: [] };
    }

    return await response.json();

  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('Error filtering doctors');
    return { doctors: [] };
  }
}
