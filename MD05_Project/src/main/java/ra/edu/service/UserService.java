package ra.edu.service;

import ra.edu.model.entity.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    boolean isExistEmail(String email);
    boolean isExistPhone(String phone);
    Optional<User> login(String email, String password);
    Optional<User> findById(Long id);
}
