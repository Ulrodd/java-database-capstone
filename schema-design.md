# Smart Clinic Database Schema Design

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(20), NOT NULL
- date_of_birth: DATE, NOT NULL
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

*Commentaires :*  
L'email est unique pour éviter les doublons. Le téléphone est une chaîne pour inclure des codes pays. La date de naissance est importante pour le suivi médical.

---

### Table: doctors
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- specialty: VARCHAR(100), NOT NULL
- phone: VARCHAR(20), NOT NULL
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

*Commentaires :*  
Le champ specialty permet de différencier les médecins selon leur domaine.

---

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), NOT NULL
- patient_id: INT, Foreign Key → patients(id), NOT NULL
- appointment_time: DATETIME, NOT NULL
- status: ENUM('Scheduled', 'Completed', 'Cancelled'), DEFAULT 'Scheduled'
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

*Commentaires :*  
La suppression d'un patient ou médecin pourrait entraîner la suppression en cascade des rendez-vous, ou bien la mise à jour pour garder un historique.

---

### Table: admins
- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), UNIQUE, NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

*Commentaires :*  
Les administrateurs gèrent la plateforme, les rôles et la sécurité.

---

### Table: clinic_locations (optionnelle)
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), NOT NULL
- address: VARCHAR(255), NOT NULL
- phone: VARCHAR(20)

*Commentaires :*  
Si la clinique a plusieurs sites, cette table stocke leurs infos.

---

## MongoDB Collection Design

### Collection: feedback

```json
{
  "feedback": {
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
}

### Collection: prescriptions

```json
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
