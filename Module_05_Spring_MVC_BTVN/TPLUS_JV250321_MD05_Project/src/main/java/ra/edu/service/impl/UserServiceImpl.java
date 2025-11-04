package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.model.entity.User;
import ra.edu.repository.UserRepository;
import ra.edu.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isExistPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Optional<User> login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            return Optional.empty();
        }

        boolean passwordMatch = BCrypt.checkpw(password, user.getPassword());
        if(passwordMatch && Boolean.TRUE.equals(user.getStatus())){
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
