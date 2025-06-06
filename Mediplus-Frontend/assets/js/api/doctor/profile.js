// assets/js/api/doctor/profile.js

$(function() {
  ajaxRequest('http://localhost:8080/api/users/me', 'GET')
    .done(doctor => {
      $('#profile-username').text(doctor.username);
      $('#profile-email').text(doctor.email);
    })
    .fail(xhr => {
      handleError(xhr, 'load profile');
    });
});
