package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;
import ra.edu.repository.CourseRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.CourseService;
import ra.edu.service.StudentService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;

    private Pageable buildPageable(Integer page, Integer size, String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return PageRequest.of(page, size);
        }

        Sort sortOrder = switch (sortBy) {
            case "nameASC" -> Sort.by(Sort.Direction.ASC, "name");
            case "nameDESC" -> Sort.by(Sort.Direction.DESC, "name");
            case "idASC" -> Sort.by(Sort.Direction.ASC, "id");
            case "idDESC" -> Sort.by(Sort.Direction.DESC, "id");
            default -> Sort.unsorted();
        };

        return PageRequest.of(page, size, sortOrder);
    }

    @Override
    public Page<User> getAllStudentsByName(Integer page, Integer size, String searchValue, String sortBy) {
        Pageable pageable = buildPageable(page, size, sortBy);
        return userRepository.findAllByRoleAndNameContaining(Role.STUDENT, searchValue, pageable);
    }

    @Override
    public Optional<User> getStudentById(Long id) {
        return userRepository.findByIdAndRole(id, Role.STUDENT);
    }

    @Override
    public boolean toggleStudentStatus(Long id) {
        Optional<User> userOptional = getStudentById(id);
        if (userOptional.isEmpty()) return false;
        User student = userOptional.get();
        student.setStatus(!student.getStatus());
        userRepository.save(student);
        return student.getStatus();
    }
}
