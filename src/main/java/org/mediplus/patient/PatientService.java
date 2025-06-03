package org.mediplus.patient;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    Patient updatePatient(Patient patient);
    Patient getPatientById(Long id);
    List<Patient> findAllPatients();
    void deletePatient(Long id);
}