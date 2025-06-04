// 2. <app-header>
class AppHeader extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `

<header class="header">
    <div class="header-inner">
        <div class="container d-flex justify-content-between align-items-center pb-3">
            <div class="logo">
                <a href="../index.html"><img src="../assets/img/logo.png" alt="MEDIPLUS"></a>
            </div>
            <div class="get-quote">
                <a href="/contact-us" class="btn">CONTACT US</a>
            </div>
        </div>
    </div>
</header>

        `;
    }
}
customElements.define('app-header', AppHeader);