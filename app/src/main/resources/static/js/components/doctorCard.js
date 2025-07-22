import { deleteDoctor } from './services/doctorServices.js';
import { getPatientData } from './services/patientServices.js';

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  const role = localStorage.getItem("userRole");

  // Section infos médecin
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Spécialité: ${doctor.specialty}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Disponibilité: ${doctor.availability.join(", ")}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  // Section actions
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Supprimer";
    removeBtn.addEventListener("click", async () => {
      const confirmed = confirm(`Confirmez-vous la suppression du médecin ${doctor.name} ?`);
      if (!confirmed) return;

      const token = localStorage.getItem("token");
      try {
        await deleteDoctor(doctor.id, token);
        card.remove();
        alert("Médecin supprimé avec succès.");
      } catch (error) {
        alert("Erreur lors de la suppression du médecin.");
        console.error(error);
      }
    });
    actionsDiv.appendChild(removeBtn);
  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Prendre rendez-vous";
    bookNow.addEventListener("click", () => {
      alert("Veuillez vous connecter pour prendre rendez-vous.");
    });
    actionsDiv.appendChild(bookNow);
  } else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Prendre rendez-vous";
    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      try {
        const patientData = await getPatientData(token);
        // showBookingOverlay est supposé être une fonction existante dans l'app
        showBookingOverlay(e, doctor, patientData);
      } catch (error) {
        alert("Erreur lors de la récupération des données patient.");
        console.error(error);
      }
    });
    actionsDiv.appendChild(bookNow);
  }

  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";

export function createDoctorCard(doctor) {
  const role = localStorage.getItem("userRole");

  const card = document.createElement("div");
  card.classList.add("doctor-card");

  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Spécialité : ${doctor.specialty}`;

  const email = document.createElement("p");
  email.textContent = `Email : ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Disponibilités : ${doctor.availability.join(", ")}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Supprimer";
    removeBtn.addEventListener("click", async () => {
      if (confirm(`Supprimer le médecin ${doctor.name} ?`)) {
        const token = localStorage.getItem("token");
        try {
          await deleteDoctor(doctor.id, token);
          card.remove();
          alert("Médecin supprimé.");
        } catch (error) {
          alert("Erreur lors de la suppression.");
        }
      }
    });
    actionsDiv.appendChild(removeBtn);

  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Prendre rendez-vous";
    bookNow.addEventListener("click", () => {
      alert("Veuillez vous connecter pour prendre rendez-vous.");
    });
    actionsDiv.appendChild(bookNow);

  } else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Prendre rendez-vous";
    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      const patientData = await getPatientData(token);
      showBookingOverlay(e, doctor, patientData);
    });
    actionsDiv.appendChild(bookNow);
  }

  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
