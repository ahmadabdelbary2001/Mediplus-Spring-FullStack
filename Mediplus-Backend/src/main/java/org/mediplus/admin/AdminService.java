package org.mediplus.admin;

import java.util.List;

public interface AdminService {
    Admin createAdmin(Admin admin);
    Admin updateAdmin(Admin admin);
    Admin getAdminById(Long id);
    List<Admin> findAllAdmins();
    void deleteAdmin(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
