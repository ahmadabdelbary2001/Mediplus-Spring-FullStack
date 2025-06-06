// assets/js/api/patient/profile.js

$(function() {
  ajaxRequest('http://localhost:8080/api/patients/me', 'GET')
    .done(patient => {
      $('#profile-username').text(patient.username);
      $('#profile-email').text(patient.email);

      if (patient.dateOfBirth) {
        const iso = patient.dateOfBirth;
        const datePart = iso.split('T')[0];
        $('#profile-dob').text(datePart);
      } else {
        $('#profile-dob').text('-');
      }
    })
    .fail(xhr => {
      handleError(xhr, 'load profile');
    });
});
