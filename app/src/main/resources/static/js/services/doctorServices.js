// app/src/main/resources/static/js/services/doctorServices.js

import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + '/doctor';

/**
 * Récupère la liste de tous les médecins
 * @returns {Array} Liste des médecins ou tableau vide en cas d'erreur
 */
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    const data = await response.json();
    // Supposons que la réponse contient un tableau 'doctors'
    return data.doctors || [];
  } catch (error) {
    console.error("getDoctors error:", error);
    return [];
  }
}

/**
 * Supprime un médecin par ID avec authentification token
 * @param {string} id - ID du médecin
 * @param {string} token - Token d'authentification
 * @returns {Object} { success: boolean, message: string }
 */
export async function deleteDoctor(id, token) {
  try {
    const url = `${DOCTOR_API}/delete/${id}/${token}`;
    const response = await fetch(url, {
      method: 'DELETE',
    });
    const data = await response.json();
    return { success: response.ok, message: data.message || '' };
  } catch (error) {
    console.error("deleteDoctor error:", error);
    return { success: false, message: "Erreur lors de la suppression du médecin" };
  }
}

/**
 * Sauvegarde un nouveau médecin (POST)
 * @param {Object} doctor - Détails du médecin
 * @param {string} token - Token d'authentification admin
 * @returns {Object} { success: boolean, message: string }
 */
export async function saveDoctor(doctor, token) {
  try {
    const url = `${DOCTOR_API}/save/${token}`;
    const response = await fetch(url, {
      method: 'POST',
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor)
    });
    const data = await response.json();
    return { success: response.ok, message: data.message || '' };
  } catch (error) {
    console.error("saveDoctor error:", error);
    return { success: false, message: "Erreur lors de l'ajout du médecin" };
  }
}

/**
 * Filtre les médecins selon les critères donnés
 * @param {string} name 
 * @param {string} time 
 * @param {string} specialty 
 * @returns {Array} Liste filtrée de médecins ou tableau vide
 */
export async function filterDoctors(name, time, specialty) {
  try {
    const url = `${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`;
    const response = await fetch(url);
    if (response.ok) {
      const data = await response.json();
      return data.doctors || [];
    } else {
      console.error("filterDoctors fetch failed:", response.statusText);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("filterDoctors error:", error);
    alert("Une erreur est survenue lors du filtrage des médecins.");
    return { doctors: [] };
  }
}
