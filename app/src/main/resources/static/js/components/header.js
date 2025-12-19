// header.js
// This file handles dynamic rendering of the header based on user role and session state

function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  // 1. Check if we are on the root page
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");

    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>
    `;
    return;
  }

  // 2. Get role and token from localStorage
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // 3. Validate session (invalid or expired login)
  if (
    (role === "loggedPatient" || role === "admin" || role === "doctor") &&
    !token
  ) {
    localStorage.removeItem("userRole");
    alert("Sesión expirada o inicio de sesión inválido. Por favor, inicie sesión nuevamente.");
    window.location.href = "/";
    return;
  }

  // 4. Base header HTML
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>
  `;

  // 5. Role-based header buttons
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">
        Agregar Doctor
      </button>
      <a href="#" onclick="logout()">Cerrar sesión</a>
    `;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Cerrar sesión</a>
    `;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>
    `;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button class="adminBtn"
        onclick="window.location.href='/pages/loggedPatientDashboard.html'">
        Home
      </button>
      <button class="adminBtn"
        onclick="window.location.href='/pages/patientAppointments.html'">
        Appointments
      </button>
      <a href="#" onclick="logoutPatient()">Cerrar sesión</a>
    `;
  }

  // 6. Close header
  headerContent += `
      </nav>
    </header>
  `;

  // 7. Inject header into DOM
  headerDiv.innerHTML = headerContent;

  // 8. Attach listeners (if needed)
  attachHeaderButtonListeners();
}

/* ---------------- Helper Functions ---------------- */

function attachHeaderButtonListeners() {
  const loginBtn = document.getElementById("patientLogin");
  const signupBtn = document.getElementById("patientSignup");

  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }

  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }
}

function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

// 9. Initialize header rendering
renderHeader();

