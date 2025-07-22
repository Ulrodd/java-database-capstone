function renderHeader() {
  const headerDiv = document.getElementById("header");

  // Si la page est la racine (homepage), on efface la session et on affiche un header minimal
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>`;

  // Gestion session expirée ou invalide
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expirée ou connexion invalide. Veuillez vous reconnecter.");
    window.location.href = "/";
    return;
  }

  // Ajout contenu selon rôle
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Ajouter un médecin</button>
      <a href="#" id="logoutLink">Déconnexion</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="window.location.href='/pages/doctorDashboard.html'">Accueil</button>
      <a href="#" id="logoutLink">Déconnexion</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Connexion</button>
      <button id="patientSignup" class="adminBtn">S’inscrire</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Accueil</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Rendez-vous</button>
      <a href="#" id="logoutPatientLink">Déconnexion</a>`;
  }

  headerContent += `</nav></header>`;

  headerDiv.innerHTML = headerContent;

  attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal('addDoctor'));
  }

  const patientLoginBtn = document.getElementById("patientLogin");
  if (patientLoginBtn) {
    patientLoginBtn.addEventListener("click", () => openModal('patientLogin'));
  }

  const patientSignupBtn = document.getElementById("patientSignup");
  if (patientSignupBtn) {
    patientSignupBtn.addEventListener("click", () => openModal('patientSignup'));
  }

  const logoutLink = document.getElementById("logoutLink");
  if (logoutLink) {
    logoutLink.addEventListener("click", (e) => {
      e.preventDefault();
      logout();
    });
  }

  const logoutPatientLink = document.getElementById("logoutPatientLink");
  if (logoutPatientLink) {
    logoutPatientLink.addEventListener("click", (e) => {
      e.preventDefault();
      logoutPatient();
    });
  }
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}

// Appel initial pour afficher le header dès que le script est chargé
renderHeader();
