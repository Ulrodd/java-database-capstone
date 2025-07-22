// app/src/main/resources/static/js/services/patientServices.js

import { API_BASE_URL } from "../config/config.js";
const PATIENT_API = API_BASE_URL + '/patient';

/**
 * Inscription d'un patient
 * @param {Object} data - Données d'inscription
 * @returns {Object} { success: boolean, message: string }
 */
export async function patientSignup(data) {
  try {
    const response = await fetch(PATIENT_API, {
      method: "POST",
      headers: { "Content-type": "application/json" },
      body: JSON.stringify(data)
    });
    const result = await response.json();
    if (!response.ok) throw new Error(result.message);
    return { success: true, message: result.message };
  } catch (error) {
    console.error("patientSignup error:", error);
    return { success: false, message: error.message };
  }
}

/**
 * Connexion patient
 * @param {Object} data - Identifiants de connexion
 * @returns {Response} Réponse fetch brute (gestion côté frontend)
 */
export async function patientLogin(data) {
  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
}

/**
 * Récupérer les données du patient connecté avec token
 * @param {string} token 
 * @returns {Object|null} Objet patient ou null
 */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    if (response.ok) return data.patient;
    return null;
  } catch (error) {
    console.error("getPatientData error:", error);
    return null;
  }
}

/**
 * Récupérer les rendez-vous selon id, token et user (patient/doctor)
 * @param {string} id 
 * @param {string} token 
 * @param {string} user 
 * @returns {Array|null} Liste de rendez-vous ou null
 */
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();
    if (response.ok) return data.appointments;
    return null;
  } catch (error) {
    console.error("getPatientAppointments error:", error);
    return null;
  }
}

/**
 * Filtrer les rendez-vous selon condition et nom
 * @param {string} condition 
 * @param {string} name 
 * @param {string} token 
 * @returns {Array} Liste filtrée ou tableau vide
 */
export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(`${PATIENT_API}/filter/${condition}/${name}/${token}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" }
    });
    if (response.ok) {
      const data = await response.json();
      return data.appointments || [];
    } else {
      console.error("filterAppointments failed:", response.statusText);
      return [];
    }
  } catch (error) {
    console.error("filterAppointments error:", error);
    alert("Une erreur est survenue lors du filtrage des rendez-vous.");
    return [];
  }
}
