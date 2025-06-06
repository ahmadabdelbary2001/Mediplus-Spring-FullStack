// assets/js/api/patient/edit-appointment.js

$(function() {
  const BASE_URL = 'http://localhost:8080/api';
  const APPT_URL = `${BASE_URL}/appointments`;
  const DOCTORS_URL = `${BASE_URL}/doctors`;
  const appointmentId = new URLSearchParams(window.location.search).get('id');

  if (!appointmentId) {
    alert('Missing appointment ID');
    return window.location.href = 'appointments.html';
  }

  let apptData = null;

  const toDateTimeLocal = iso => {
    if (!iso) return '';
    const d = new Date(iso);
    const offset = d.getTimezoneOffset();
    return new Date(d.getTime() - offset * 60 * 1000).toISOString().slice(0, 16);
  };

  function loadAppointment() {
    ajaxRequest(`${APPT_URL}/${appointmentId}`, 'GET')
      .done(appt => {
        apptData = appt;
        $('#dateTime').val(toDateTimeLocal(appt.dateTime));
        $('#id').val(appt.id);
        $('#patientUsername').val(appt.patientUsername);
        $('#status').val(appt.status);
        loadDoctors(appt.doctorUsername);
      })
      .fail(xhr => {
        if (xhr.status === 401) {
          alert('Please log in first.');
          return window.location.href = 'auth/login.html';
        }
        handleError(xhr, 'load appointment');
      });
  }

  function loadDoctors(selected) {
    ajaxRequest(DOCTORS_URL, 'GET')
      .done(docs => {
        const $sel = $('#doctor').empty().append('<option value="">Select a Doctor</option>');
        docs.forEach(doc => {
          const sel = doc.username === selected ? 'selected' : '';
          $sel.append(`<option value="${doc.username}" ${sel}>${doc.username} - ${doc.specialization}</option>`);
        });
      })
      .fail(xhr => handleError(xhr, 'load doctors'));
  }

  $('#editAppointmentForm').on('submit', function(e) {
    e.preventDefault();

    if (!apptData) {
      alert('Appointment data not loaded.');
      return;
    }

    const dtLocal = $('#dateTime').val();
    const docUser = $('#doctor').val();
    const chosen = dtLocal && new Date(dtLocal);
    const now = new Date();

    if (!dtLocal) {
      $('#dateTime').addClass('is-invalid');
      return;
    }
    if (chosen < now) {
      alert('Cannot set an appointment in the past.');
      return;
    }
    $('#dateTime').removeClass('is-invalid');

    if (!docUser) {
      $('#doctor').addClass('is-invalid');
      return;
    }
    $('#doctor').removeClass('is-invalid');

    const payload = {
      id: apptData.id,
      dateTime: chosen.toISOString(),
      status: apptData.status,
      patientUsername: apptData.patientUsername,
      doctorUsername: docUser
    };

    ajaxRequest(`${APPT_URL}/${appointmentId}`, 'PUT', payload)
      .done(() => {
        alert('Appointment updated successfully.');
        window.location.href = 'appointments.html';
      })
      .fail(xhr => {
        if (xhr.status === 401) {
          alert('Unauthorized. Please log in.');
          return window.location.href = 'auth/login.html';
        }
        if (xhr.status === 404) {
          alert('Appointment not found.');
          return window.location.href = 'appointments.html';
        }
        handleError(xhr, 'update appointment');
      });
  });

  loadAppointment();
});
