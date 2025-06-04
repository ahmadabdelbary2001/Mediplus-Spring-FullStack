// 2. <app-preloader>
class AppPreloader extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        
<!-- Preloader -->
<div class="preloader">
    <div class="loader">
        <div class="loader-outter"></div>
        <div class="loader-inner"></div>

        <div class="indicator">
            <svg width="16px" height="12px">
                <polyline id="back" points="1 6 4 6 6 11 10 1 12 6 15 6"></polyline>
                <polyline id="front" points="1 6 4 6 6 11 10 1 12 6 15 6"></polyline>
            </svg>
        </div>
    </div>
</div>
<!-- End Preloader -->

        `;
        // استمع لانتهاء تحميل الصفحة (جميع الصور والسكريبتات)
        window.addEventListener('load', () => {
            // الطريقة الأولى: إزالة العنصر نهائيًا
            this.remove();

            // أو الطريقة الثانية: إخفاؤه عبر CSS
            // this.style.display = 'none';
        });
    }
}
customElements.define('app-preloader', AppPreloader);