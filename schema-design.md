## MySQL Database Design

### Table: patients

* id: INT, Primary Key, AUTO_INCREMENT
* first_name: VARCHAR(100), NOT NULL
* last_name: VARCHAR(100), NOT NULL
* email: VARCHAR(150), UNIQUE, NOT NULL
* phone: VARCHAR(20)
* date_of_birth: DATE, NOT NULL
* created_at: DATETIME, NOT NULL

### Table: doctors

* id: INT, Primary Key, AUTO_INCREMENT
* first_name: VARCHAR(100), NOT NULL
* last_name: VARCHAR(100), NOT NULL
* email: VARCHAR(150), UNIQUE, NOT NULL
* specialty: VARCHAR(100), NOT NULL
* created_at: DATETIME, NOT NULL

### Table: admin

* id: INT, Primary Key, AUTO_INCREMENT
* username: VARCHAR(100), UNIQUE, NOT NULL
* password_hash: VARCHAR(255), NOT NULL
* created_at: DATETIME, NOT NULL

### Table: appointments

* id: INT, Primary Key, AUTO_INCREMENT
* doctor_id: INT, Foreign Key → doctors(id)
* patient_id: INT, Foreign Key → patients(id)
* appointment_time: DATETIME, NOT NULL
* status: INT, NOT NULL (0 = Scheduled, 1 = Completed, 2 = Cancelled)

---

## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 12,
  "doctorId": 3,
  "appointmentId": 45,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours"
    }
  ],
  "doctorNotes": "Take after meals",
  "createdAt": "2025-01-10T10:30:00Z"
}
```
