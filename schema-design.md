-- ========================================
-- Smart Clinic Database Schema Design
-- ========================================

-- ========================================
-- MySQL Database Design
-- ========================================

-- Table: patients
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: doctors
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    specialty VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: appointments
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Table: admins
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: clinic_locations (optionnelle)
CREATE TABLE clinic_locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
);

-- ========================================
-- MongoDB Collections Design (exemples JSON)
-- ========================================

/*
Collection: feedback

{
  "userId": "12345",
  "prescriptionId": "67890",
  "comments": "Le médicament a été efficace, mais j'ai ressenti quelques effets secondaires.",
  "rating": 4,
  "tags": ["efficacité", "effets secondaires", "satisfaction"],
  "metadata": {
    "submittedAt": "2023-10-01T12:34:56Z",
    "updatedAt": "2023-10-02T09:00:00Z"
  },
  "responses": [
    {
      "responseId": "abc123",
      "adminId": "admin001",
      "reply": "Merci pour votre retour. Nous sommes heureux d'apprendre que le médicament a été efficace.",
      "timestamp": "2023-10-01T15:00:00Z"
    },
    {
      "responseId": "def456",
      "adminId": "admin002",
      "reply": "Nous allons examiner les effets secondaires que vous avez mentionnés.",
      "timestamp": "2023-10-02T10:00:00Z"
    }
  ]
}

Collection: prescriptions

{
  "_id": "ObjectId('64abc123456')",
  "patient_id": 101,
  "appointment_id": 250,
  "medication": "Paracétamol",
  "dosage": "500mg",
  "frequency": "Toutes les 6 heures",
  "duration_days": 5,
  "doctor_notes": "Ne pas dépasser 3 comprimés par jour.",
  "refill_count": 2,
  "pharmacy": {
    "name": "Pharmacie Centrale",
    "location": "123 Rue Principale"
  },
  "metadata": {
    "created_at": "2025-07-20T14:30:00Z",
    "updated_at": "2025-07-21T10:00:00Z"
  }
}
*/

-- ========================================
-- Procédures Stockées MySQL
-- ========================================

DELIMITER $$

-- 1. Rapport Quotidien des Rendez-vous par Médecin
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE
)
BEGIN
    SELECT 
        CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
        a.appointment_time,
        a.status,
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
        p.phone AS patient_phone
    FROM 
        appointments a
    JOIN 
        doctors d ON a.doctor_id = d.id
    JOIN 
        patients p ON a.patient_id = p.id
    WHERE 
        DATE(a.appointment_time) = report_date
    ORDER BY 
        d.last_name, d.first_name, a.appointment_time;
END$$

-- 2. Médecin avec le Plus de Patients Par Mois
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN input_month INT, 
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(DISTINCT patient_id) AS patients_seen
    FROM
        appointments
    WHERE
        MONTH(appointment_time) = input_month 
        AND YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$

-- 3. Médecin avec le Plus de Patients par Année
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id, 
        COUNT(DISTINCT patient_id) AS patients_seen
    FROM
        appointments
    WHERE
        YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$

DELIMITER ;

-- ========================================
-- Exemples d'appels des procédures stockées
-- ========================================

/*
CALL GetDailyAppointmentReportByDoctor('2025-07-22');
CALL GetDoctorWithMostPatientsByMonth(7, 2025);
CALL GetDoctorWithMostPatientsByYear(2025);
*/
