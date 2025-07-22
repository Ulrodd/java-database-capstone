# User Stories – Smart Clinic Management System

---

## 🛠 Admin User Stories

### Story 1: Admin Login
_As an administrator, I want to log in to the portal using my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter valid credentials to log in  
2. Invalid credentials are rejected with error message  
3. Secure session is established upon login

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Passwords must be encrypted and stored securely  

---

### Story 2: Admin Logout
_As an administrator, I want to log out of the portal, so that I can protect access to the system._

**Acceptance Criteria:**
1. Admin can click "Logout" to end session  
2. Session data is cleared  
3. Admin is redirected to login page  

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Auto-logout after inactivity should also be implemented  

---

### Story 3: Add Doctor Profile
_As an administrator, I want to add doctors to the system, so that they can be assigned to appointments._

**Acceptance Criteria:**
1. Admin can fill out doctor details and submit  
2. Doctor is saved in the database  
3. Confirmation message is shown  

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Doctor ID must be unique  

---

### Story 4: Remove Doctor Profile
_As an administrator, I want to remove doctors from the system, so that outdated profiles are deleted._

**Acceptance Criteria:**
1. Admin selects doctor and clicks delete  
2. System confirms deletion  
3. Doctor is removed from MySQL  

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Check for active appointments before deleting  

---

### Story 5: Monthly Appointment Stats
_As an administrator, I want to execute a stored procedure to get appointment counts per month, so that I can track usage trends._

**Acceptance Criteria:**
1. Admin runs stored procedure from interface or CLI  
2. Results show number of appointments by month  
3. Data is exportable as CSV  

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Requires MySQL integration  

---

## 👤 Patient User Stories

### Story 1: View Doctors Without Login
_As a patient, I want to view a list of doctors without logging in, so that I can explore my options before signing up._

**Acceptance Criteria:**
1. Doctor list displays name, specialization, availability  
2. No login is required  
3. List can be filtered  

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Doctors marked inactive should not appear  

---

### Story 2: Patient Registration
_As a patient, I want to sign up with email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Form validates required fields  
2. Password is hashed and stored securely  
3. Confirmation email is sent  

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Avoid duplicate email registrations  

---

### Story 3: Patient Login
_As a patient, I want to log in, so that I can manage my appointments._

**Acceptance Criteria:**
1. Login with valid credentials  
2. Wrong credentials are rejected  
3. Session is created securely  

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Implement “remember me” feature if possible  

---

### Story 4: Book Appointment
_As a patient, I want to book a one-hour appointment with a doctor, so that I can receive medical consultation._

**Acceptance Criteria:**
1. Select doctor, date, and time  
2. System checks doctor availability  
3. Booking confirmation is displayed  

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Time slots must be managed to avoid overlaps  

---

### Story 5: View Upcoming Appointments
_As a patient, I want to view my upcoming appointments, so that I can prepare ahead._

**Acceptance Criteria:**
1. List shows date, time, and doctor  
2. Only future appointments appear  
3. Option to cancel appointment  

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Appointments within 24h may not be cancellable  

---

## 👨‍⚕️ Doctor User Stories

### Story 1: Doctor Login
_As a doctor, I want to log in to the system, so that I can access my appointment schedule._

**Acceptance Criteria:**
1. Doctor can enter credentials  
2. Redirected to dashboard  
3. Invalid attempts are tracked  

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Password reset feature is recommended  

---

### Story 2: Doctor Logout
_As a doctor, I want to log out, so that my session is protected from unauthorized access._

**Acceptance Criteria:**
1. Logout button ends session  
2. Token or session data is invalidated  
3. Redirected to login page  

**Priority:** Medium  
**Story Points:** 1  
**Notes:**  
- Implement automatic timeout after inactivity  

---

### Story 3: View Appointment Calendar
_As a doctor, I want to see my appointment calendar, so that I can manage my time effectively._

**Acceptance Criteria:**
1. Calendar displays daily/weekly view  
2. Shows booked slots with patient names  
3. View updates in real-time  

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Calendar should be mobile-responsive  

---

### Story 4: Mark Unavailability
_As a doctor, I want to mark unavailable time slots, so that patients only see my true availability._

**Acceptance Criteria:**
1. Doctor selects date/time to block  
2. Time slot becomes unavailable to patients  
3. Blocked time can be edited or deleted  

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Conflicts with existing bookings must be prevented  

---

### Story 5: Update Profile
_As a doctor, I want to update my profile with my specialization and contact details, so that patients have accurate information._

**Acceptance Criteria:**
1. Fields for specialization, email, phone, location  
2. Updates are saved and visible to patients  
3. Profile update triggers success message  

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Profile should support image upload  

---

