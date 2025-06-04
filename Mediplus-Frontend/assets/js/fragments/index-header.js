// 2. <app-header>
class AppHeader extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `

<!-- Header Area -->
<header class="header" >
    <!-- Header Inner -->
    <div class="header-inner">
        <div class="container">
            <div class="inner">
                <div class="row">
                    <div class="col-lg-3 col-md-3 col-12">
                        <!-- Start Logo -->
                        <div class="logo">
                            <a href="#"><img src="./assets/img/logo.png" alt="#"></a>
                        </div>
                        <!-- End Logo -->
                    </div>
                    <div class="col-lg-7 col-md-9 col-12">
                        <!-- Main Menu -->
                        <div class="main-menu">
                            <nav class="navigation">
                                <ul class="nav menu">
                                    <li class="active"><a href="#">Home <i class="icofont-rounded-down"></i></a>
                                        <ul class="dropdown">
                                            <li><a href="#">Home Page 1</a></li>
                                        </ul>
                                    </li>
                                    <li><a href="#">Doctos </a></li>
                                    <li><a href="#">Services </a></li>
                                    <li><a href="#">Contact Us</a></li>
                                </ul>
                            </nav>
                        </div>
                        <!--/ End Main Menu -->
                    </div>
                    <div class="col-lg-2 col-12">
                        <div class="get-quote">
                            <a id="getStarted" class="btn">Get Started</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--/ End Header Inner -->
</header>
<!-- End Header Area -->

        `;
    }
}
customElements.define('app-header', AppHeader);