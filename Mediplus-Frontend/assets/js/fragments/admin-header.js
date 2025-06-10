// assets/js/fragments/admin-header.js

class AdminHeader extends HTMLElement {
  connectedCallback() {
    let username = '[Guest]';
    const raw = sessionStorage.getItem('user');
    if (raw) {
      try {
        const u = JSON.parse(raw);
        username = u.username || '[Guest]';
      } catch {
        username = '[Guest]';
      }
    }

    this.innerHTML = `
      <!-- Admin Header -->
      <header class="header bg-light shadow-sm">
        <div class="header-inner">
          <div class="container">
            <div class="inner">
              <div class="row align-items-center">

                <!-- Logo -->
                <div class="col-lg-3 col-md-3 col-6">
                  <div class="logo">
                    <a href="../index.html">
                      <img src="../assets/img/logo.png" alt="MediPlus">
                    </a>
                  </div>
                  <div class="mobile-nav"></div>
                </div>

                <!-- القوائم الرئيسية لمسؤول النظام -->
                <div class="col-lg-6 col-md-6 d-none d-md-block">
                  <nav class="navigation">
                    <ul class="nav menu">
                      <li class="${this._isActive('dashboard.html')}">
                        <a href="../admin/dashboard.html">Dashboard</a>
                      </li>
                      <li class="dropdown ${this._isActive('users.html')}">
                        <a href="#">User Management <i class="icofont-rounded-down"></i></a>
                        <ul class="dropdown">
                          <li><a href="../admin/users-doctors.html">Doctors</a></li>
                          <li><a href="../admin/users-pharmacists.html">Pharmacists</a></li>
                          <li><a href="../admin/users-receptionists.html">Receptionist</a></li>
                        </ul>
                      </li>
                      
                      <li class="${this._isActive('specializations.html')}">
                        <a href="../admin/specializations.html">Specializations</a>
                      </li>
                      <li class="${this._isActive('reports.html')}">
                        <a href="../admin/reports.html">Reports</a>
                      </li>
                    </ul>
                  </nav>
                </div>

                <!-- Icons: Alerts / Messages / User -->
                <div class="col-lg-3 col-md-3 col-6">
                  <ul class="d-flex justify-content-end align-items-center list-unstyled mb-0">
                    <!-- Alerts -->
                    <li class="nav-item dropdown no-arrow mx-2">
                      <a class="nav-link d-flex align-items-center p-2 hover-primary" href="#" id="alertsDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="icofont-alarm icofont-md text-dark"></i>
                        <span class="badge badge-danger badge-pill position-absolute" style="top: -5px; right: -5px">3</span>
                      </a>
                      <div class="dropdown-menu dropdown-menu-end shadow animated--grow-in"
                           aria-labelledby="alertsDropdown">
                        <h6 class="dropdown-header text-center bg-light">New Notifications</h6>
                        <div class="dropdown-item d-flex align-items-center">
                          <i class="icofont-prescription icofont-2x text-primary me-3"></i>
                          <div>
                            <p class="mb-0 small">New prescription request</p>
                            <span class="text-muted">2 mins ago</span>
                          </div>
                        </div>
                        <div class="dropdown-divider"></div>
                        <a href="#" class="dropdown-item text-center small hover-primary">View All</a>
                      </div>
                    </li>

                    <!-- Messages -->
                    <li class="nav-item dropdown no-arrow mx-2">
                      <a class="nav-link d-flex align-items-center p-2 hover-primary" href="#"
                                data-toggle="dropdown">
                        <i class="icofont-envelope icofont-md text-dark"></i>
                        <span class="badge badge-success badge-pill position-absolute" style="top: -5px; right: -5px">2</span>
                      </a>
                      <div class="dropdown-menu dropdown-menu-end shadow animated--grow-in"
                           aria-labelledby="messagesDropdown">
                        <h6 class="dropdown-header text-center bg-light">Recent Messages</h6>
                        <div class="dropdown-item d-flex align-items-center">
                          <img src="../assets/img/doctor-thumb.jpg" class="rounded-circle me-3" width="40" alt="doctor">
                          <div>
                            <p class="mb-0 small">Need urgent consultation</p>
                            <span class="text-muted">Dr. Ahmed</span>
                          </div>
                        </div>
                        <div class="dropdown-divider"></div>
                        <a href="#" class="dropdown-item text-center small hover-primary">View All</a>
                      </div>
                    </li>

                    <!-- User Info -->
                    <li class="nav-item dropdown no-arrow mx-2">
                      <a class="nav-link d-flex align-items-center hover-primary" href="#" id="userDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span class="d-none d-md-inline text-dark">${username}</span>
                        <i class="icofont-user icofont-md text-dark"></i>
                      </a>
                      <div class="dropdown-menu dropdown-menu-end shadow animated--grow-in">
                        <a class="dropdown-item" href="../doctor/profile.html">
                          <i class="icofont-user-alt-3 me-2 text-primary"></i> Profile
                        </a>
                        <a class="dropdown-item" href="#">
                          <i class="icofont-settings me-2 text-primary"></i> Settings
                        </a>
                        <div class="dropdown-divider"></div>
                        <a id="adminLogoutBtn" class="dropdown-item" href="#">
                          <i class="icofont-logout me-2 text-danger"></i> Logout
                        </a>
                      </div>
                    </li>

                  </ul>
                </div>

              </div>
            </div>
          </div>
        </div>
      </header>
    `;

    this._highlightActive();

    const logoutBtn = this.querySelector('#adminLogoutBtn');
    if (logoutBtn) {
      logoutBtn.addEventListener('click', e => {
        e.preventDefault();
        sessionStorage.clear();
        window.location.href = '../auth/login.html';
      });
    }

    document.addEventListener('click', e => {
      if (!this.contains(e.target)) {
        this.querySelectorAll('.dropdown-menu.show').forEach(menu => {
          menu.classList.remove('show');
        });
      }
    });

    $(this).find('[data-toggle="dropdown"]').on('click', function(e) {
      e.preventDefault();
      const $menu = $(this).next('.dropdown-menu');
      $('.dropdown-menu').not($menu).removeClass('show');
      $menu.toggleClass('show');
      return false;
    });
  }

  _highlightActive() {
    const currentPage = window.location.pathname.split('/').pop();
    this.querySelectorAll('nav.navigation .nav.menu li').forEach(li => {
      li.classList.remove('active');
      const anchor = li.querySelector('a');
      if (!anchor) return;
      const href = anchor.getAttribute('href')?.split('/').pop();
      if (href && href === currentPage) {
        li.classList.add('active');
      }
    });
  }

  _isActive(pageName) {
    const current = window.location.pathname.split('/').pop();
    return current === pageName ? 'active' : '';
  }
}

customElements.define('app-header', AdminHeader);
