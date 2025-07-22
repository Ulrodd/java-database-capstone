import { openModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from '../components/doctorCard.js';

// Ouvre le modal d'ajout médecin
document.getElementById('addDocBtn').addEventListener('click', () => {
  openModal('addDoctor');
});

// Charge les médecins au chargement
window.addEventListener('DOMContentLoaded', () => {
  loadDoctorCards();
});

// Charge et affiche toutes les cartes des médecins
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';
    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });
  } catch (error) {
    console.error('Erreur lors du chargement des médecins:', error);
  }
}

// Écoute les changements sur recherche et filtres
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim() || null;
  const time = document.getElementById("filterTime").value || null;
  const specialty = document.getElementById("filterSpecialty").value || null;

  try {
    const response = await filterDoctors(name, time, specialty);
    const doctors = response.doctors || [];
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';
    if (doctors.length > 0) {
      doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        contentDiv.appendChild(card);
      });
    } else {
      contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
    }
  } catch (error) {
    alert("Une erreur est survenue lors du filtrage des médecins.");
    console.error(error);
  }
}

// Fonction utilitaire optionnelle
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  contentDiv.innerHTML = '';
  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// Ajout d’un nouveau médecin via modal
async function adminAddDoctor() {
  const name = document.getElementById('doctorName').value.trim();
  const email = document.getElementById('doctorEmail').value.trim();
  const phone = document.getElementById('doctorPhone').value.trim();
  const password = document.getElementById('doctorPassword').value;
  const specialty = document.getElementById('doctorSpecialty').value.trim();
  const availability = [...document.querySelectorAll('.availability-checkbox:checked')].map(cb => cb.value);

  const token = localStorage.getItem('token');
  if (!token) {
    alert("Vous devez être connecté pour ajouter un médecin.");
    return;
  }

  const doctor = { name, email, phone, password, specialty, availability };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert("Médecin ajouté avec succès !");
      openModal('addDoctor', false); // Fermer modal, adapter selon ta fonction
      loadDoctorCards();
    } else {
      alert("Erreur : " + result.message);
    }
  } catch (error) {
    alert("Une erreur est survenue lors de l'ajout du médecin.");
    console.error(error);
  }
}

// Exposer adminAddDoctor si appelé depuis HTML inline
window.adminAddDoctor = adminAddDoctor;
