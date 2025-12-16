# Admin Story

**Title:**
_As an Administrator, I want to manage the entire clinic system, so that I can ensure smooth operations and quickly resolve failures._

**Acceptance Criteria:**
1. The administrator can create, update, and deactivate doctors, patients, and clinic staff.
2. The administrator can view and manage appointments, including rescheduling or canceling them.
3. The administrator can monitor system status and access logs or error information to identify and fix failures.

**Priority:** High
**Story Points:** 8
**Notes:**
- The administrator has full access permissions across all system modules.
- Error handling and validation must prevent invalid data changes.
- Only authenticated administrators can access these features.


- # Patient Story

**Title:**
_As a Patient, I want to view and manage my appointments, so that I can receive medical care at convenient times._

**Acceptance Criteria:**
1. The patient can view a list of upcoming and past appointments.
2. The patient can book, reschedule, or cancel an appointment through the system.
3. he patient can view basic information about the assigned doctor and appointment details.

**Priority:** High
**Story Points:** 5
**Notes:**
- Patients must be authenticated to access their appointments.
- Appointment availability depends on doctor schedules.
- The system should prevent double-booking of appointments.

- # Doctor Story
  
**Title:**
_As a Doctor, I want to view my schedule, so that I can manage my appointments efficiently._

**Acceptance Criteria:**
1. The doctor can view a daily and weekly schedule of assigned appointments.
2. The schedule displays patient name, appointment time, and appointment type.
3. The doctor can see updates to the schedule in real time when appointments are created, updated, or canceled.

**Priority:** High
**Story Points:** 5
**Notes:**
- Only authenticated doctors can access their own schedules.
- Past appointments should be displayed as read-only.

- # Admin Appointment Story
  
**Title:**
_As an Administrator, I want to manage clinic appointments, so that scheduling conflicts are avoided and operations run smoothly._

**Acceptance Criteria:**
1. The administrator can view all appointments in the system.
2. The administrator can reschedule or cancel appointments when necessary.
3. The system prevents overlapping appointments for the same doctor.The doctor can see updates to the schedule in real time when appointments are created, updated, or canceled.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Appointment changes should follow clinic scheduling rules.
- Only authenticated administrators can perform these actions.


- # Doctor Prescription Story
  
**Title:**
_As a Doctor, I want to create and manage patient prescriptions, so that treatments are properly recorded and accessible._

**Acceptance Criteria:**
1. The doctor can create a prescription linked to a patient and an appointment.
2. Prescriptions are stored and retrieved successfully by the system.
3. The doctor can view previous prescriptions for a patient.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Only doctors are allowed to create or update prescriptions.
- Prescription data is stored in a document-based database (MongoDB).
