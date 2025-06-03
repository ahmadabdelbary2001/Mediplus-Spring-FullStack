package org.mediplus.user;

public interface UserService {
    User getUserByUsername(String username);
    User getUserById(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean authenticate(User user);
    void deleteUser(Long userId);
}
