// doctorCard.js

// Import overlay function for booking appointments (logged-in patient)
import { openBookingOverlay } from "./loggedPatient.js";

// Import API function to delete a doctor (admin role)
import { deleteDoctor } from "./doctorServices.js";

// Import function to fetch patient details
import { getPatientDetails } from "./patientServices.js";

/**
 * Creates and returns a DOM element representing a doctor card
 * @param {Object} doctor - Doctor data object
 * @returns {HTMLElement} doctorCard
 */
export function createDoctorCard(doctor) {
  // Create main card container
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Get current user role
  const role = localStorage.getItem("userRole");

  // ---------------- Doctor Info ----------------
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialization}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  // Available times
  const timesList = document.createElement("ul");
  if (doctor.availableTimes && doctor.availableTimes.length > 0) {
    doctor.availableTimes.forEach(time => {
      const li = document.createElement("li");
      li.textContent = time;
      timesList.appendChild(li);
    });
  } else {
    const li = document.createElement("li");
    li.textContent = "No available times";
    timesList.appendChild(li);
  }

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(timesList);

  // ---------------- Actions ----------------
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  /* ===== ADMIN ROLE ===== */
  if (role === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("adminBtn");

    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Unauthorized action.");
        return;
      }

      try {
        await deleteDoctor(doctor.id, token);
        alert("Doctor deleted successfully.");
        card.remove();
      } catch (error) {
        alert("Failed to delete doctor.");
        console.error(error);
      }
    });

    actionsDiv.appendChild(deleteBtn);
  }

  /* ===== PATIENT (NOT LOGGED-IN) ===== */
  if (role === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";

    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    actionsDiv.appendChild(bookBtn);
  }

  /* ===== LOGGED-IN PATIENT ===== */
  if (role === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";

    bookBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        window.location.href = "/";
        return;
      }

      try {
        const patient = await getPatientDetails(token);
        openBookingOverlay(doctor, patient);
      } catch (error) {
        alert("Unable to fetch patient details.");
        console.error(error);
      }
    });

    actionsDiv.appendChild(bookBtn);
  }

  // ---------------- Final Assembly ----------------
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
