package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    Page<User> findAllByRoleAndNameContaining(Role role, String name, Pageable pageable);

    Optional<User> findByIdAndRole(Long id, Role role);

    Long countByRole(Role role);
}
