// assets/js/fragments/scripts.js

(function() {
    const scriptSources = [
        './assets/js/vendor/jquery.min.js',
        './assets/js/vendor/jquery-migrate-3.0.0.js',
        './assets/js/vendor/jquery-ui.min.js',
        './assets/js/vendor/easing.js',
        './assets/js/vendor/colors.js',
        './assets/js/vendor/popper.min.js',
        './assets/js/vendor/bootstrap.bundle.min.js',
        './assets/js/vendor/bootstrap-datepicker.js',
        './assets/js/vendor/jquery.nav.js',
        './assets/js/vendor/slicknav.min.js',
        './assets/js/vendor/jquery.scrollUp.min.js',
        './assets/js/vendor/niceselect.js',
        './assets/js/vendor/tilt.jquery.min.js',
        './assets/js/vendor/owl-carousel.js',
        './assets/js/vendor/jquery.counterup.min.js',
        './assets/js/vendor/steller.js',
        './assets/js/vendor/wow.min.js',
        './assets/js/vendor/jquery.magnific-popup.min.js',
        'http://cdnjs.cloudflare.com/ajax/libs/waypoints/2.0.3/waypoints.min.js',
        './assets/js/vendor/bootstrap.min.js',
        './assets/js/vendor/main.js'
    ];

    function loadScript(src, isExternal = false) {
        const s = document.createElement('script');
        s.src = src;
        s.async = false; 
        // s.defer = true;
        document.body.appendChild(s);
    }

    document.addEventListener('DOMContentLoaded', function() {
        scriptSources.forEach(src => {
            loadScript(src);
        });
    });
})();
