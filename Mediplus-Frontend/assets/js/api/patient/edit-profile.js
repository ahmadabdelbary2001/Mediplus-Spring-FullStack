// assets/js/api/patient/edit-profile.js

$(function() {
  function extractDate(isoString) {
    return isoString ? isoString.split('T')[0] : '';
  }

  ajaxRequest('http://localhost:8080/api/patients/me', 'GET')
    .done(patient => {
      $('#input-username').val(patient.username);
      $('#input-email').val(patient.email);
      $('#input-dob').val(extractDate(patient.dateOfBirth));
    })
    .fail(xhr => handleError(xhr, 'load profile'));

  $('#editProfileForm').on('submit', function(e) {
    e.preventDefault();

    const username = $('#input-username').val().trim();
    const email = $('#input-email').val().trim();
    const dobValue = $('#input-dob').val();

    let valid = true;
    if (!username) {
      $('#input-username').addClass('is-invalid');
      valid = false;
    } else {
      $('#input-username').removeClass('is-invalid');
    }

    if (!email) {
      $('#input-email').addClass('is-invalid');
      valid = false;
    } else {
      $('#input-email').removeClass('is-invalid');
    }

    if (!dobValue) {
      $('#input-dob').addClass('is-invalid');
      valid = false;
    } else {
      $('#input-dob').removeClass('is-invalid');
    }

    if (!valid) return;

    ajaxRequest('http://localhost:8080/api/patients/me', 'GET')
      .done(patient => {
        const id = patient.id;

        const payload = {
          username: username,
          email: email,
          dateOfBirth: dobValue
        };

        ajaxRequest(`http://localhost:8080/api/patients/${id}`, 'PUT', payload)
          .done(updated => {
            alert('Profile updated successfully.');
            window.location.replace('profile.html');
          })
          .fail(xhr => handleError(xhr, 'update profile'));
      })
      .fail(xhr => handleError(xhr, 'load profile'));
  });
});
