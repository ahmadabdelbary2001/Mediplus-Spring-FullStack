# MediPlus Clinic Management System

MediPlus is a full-stack clinic management system built with Spring Boot (Java) for the backend and a static front-end using HTML, CSS, JavaScript, and jQuery. The application provides functionalities for three primary user roles:

1. **Patients**
   - Self-registration, login, and profile management.
   - Booking, viewing, editing, and canceling appointments with doctors.
   - Viewing medical records and prescriptions.
   - Receiving notifications when a doctor confirms or rejects an appointment.

2. **Doctors**
   - Login and profile management.
   - Viewing a list of patient appointments.
   - Confirming or rejecting appointment requests.
   - Managing personal schedule.
   - Receiving notifications when a patient requests or modifies an appointment.

3. **Administrators**
   - Managing all user accounts (doctors, patients, pharmacists, receptionists).
   - Configuring medical departments and specializations.
   - Generating statistical reports (e.g., number of patients, appointments, revenue).
   - Viewing system logs and monitoring unauthorized access attempts.

Under the hood, the application uses Spring Security for authentication/authorization, JPA/Hibernate for data persistence, and a RESTful design for API endpoints.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Database Setup](#database-setup)
  - [Environment Configuration](#environment-configuration)
  - [Building & Running](#building--running)
- [API Reference](#api-reference)
- [Frontend Static Pages](#frontend-static-pages)
- [Security & Roles](#security--roles)
- [Testing](#testing)
- [Future Improvements](#future-improvements)
- [License](#license)
