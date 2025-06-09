package org.mediplus.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        log.info("Initializing Admin Service");
    }

    @Override
    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        Optional<Admin> existingOpt = adminRepository.findById(admin.getId());
        if (existingOpt.isEmpty()) {
            return null;
        }
        Admin existing = existingOpt.get();

        existing.setUsername(admin.getUsername());
        existing.setEmail(admin.getEmail());
        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        return adminRepository.save(existing);
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}
