package org.mediplus.user;

public interface UserService {
    User getUserByUsername(String username);
    User getUserById(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User authenticate(String username, String rawPassword);
    void deleteUser(Long userId);
}
