import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from '../components/patientRows.js';

const patientTableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0]; // yyyy-mm-dd
const token = localStorage.getItem('token');
let patientName = null;

// Recherche par nom patient
document.getElementById('searchBar').addEventListener('input', (e) => {
  patientName = e.target.value.trim() || null;
  loadAppointments();
});

// Bouton Rendez-vous d’aujourd’hui
document.getElementById('todayButton').addEventListener('click', () => {
  selectedDate = new Date().toISOString().split('T')[0];
  document.getElementById('datePicker').value = selectedDate;
  loadAppointments();
});

// Sélecteur de date
document.getElementById('datePicker').addEventListener('change', (e) => {
  selectedDate = e.target.value;
  loadAppointments();
});

// Charge les rendez-vous pour la date et filtre
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);
    patientTableBody.innerHTML = '';

    if (!appointments || appointments.length === 0) {
      const tr = document.createElement('tr');
      tr.innerHTML = `<td colspan="5">Aucun rendez-vous trouvé pour cette date.</td>`;
      patientTableBody.appendChild(tr);
      return;
    }

    appointments.forEach(appointment => {
      const row = createPatientRow(appointment);
      patientTableBody.appendChild(row);
    });
  } catch (error) {
    patientTableBody.innerHTML = `<tr><td colspan="5">Erreur lors du chargement des rendez-vous.</td></tr>`;
    console.error(error);
  }
}

// Initialisation au chargement de la page
window.addEventListener('DOMContentLoaded', () => {
  // renderContent(); // si tu as une fonction spécifique
  loadAppointments();
});
