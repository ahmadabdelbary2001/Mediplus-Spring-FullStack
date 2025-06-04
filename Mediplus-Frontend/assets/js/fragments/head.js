// 1. <app-head>
class AppHead extends HTMLElement {
    connectedCallback() {
        const title = this.getAttribute('data-page-title') || '';
        this.innerHTML = `
<!-- Meta Tags -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="keywords" content="Site keywords here">
<meta name="description" content="">
<meta name='copyright' content=''>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Title -->
<title>MEDIPLUS | ${title}</title>

<!-- Favicon -->
<link rel="icon" href="../assets/img/favicon.png">

<!-- Google Fonts -->
<link href="https://fonts.googleapis.com/css?family=Poppins:200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&display=swap" rel="stylesheet">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="../assets/css/bootstrap.min.css">
<!-- Nice Select CSS -->
<link rel="stylesheet" href="../assets/css/nice-select.css">
<!-- Font Awesome CSS -->
<link rel="stylesheet" href="../assets/css/font-awesome.min.css">
<!-- icofont CSS -->
<link rel="stylesheet" href="../assets/css/icofont.css">
<!-- Slicknav -->
<link rel="stylesheet" href="../assets/css/slicknav.min.css">
<!-- Owl Carousel CSS -->
<link rel="stylesheet" href="../assets/css/owl-carousel.css">
<!-- Datepicker CSS -->
<link rel="stylesheet" href="../assets/css/datepicker.css">
<!-- Animate CSS -->
<link rel="stylesheet" href="../assets/css/animate.min.css">
<!-- Magnific Popup CSS -->
<link rel="stylesheet" href="../assets/css/magnific-popup.css">

<!-- Medipro CSS -->
<link rel="stylesheet" href="../assets/css/normalize.css">
<link rel="stylesheet" href="../assets/css/style.css">
<link rel="stylesheet" href="../assets/css/responsive.css">
        `;
    }
}
customElements.define('app-head', AppHead);