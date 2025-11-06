package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.entity.Course;
import java.util.Optional;

public interface CourseService {
    Page<Course> getAllCoursesByName(Integer page, Integer size, String courseName,  String sortBy);
    Course save(Course course);
    Optional<Course> findCourseById(Long id);
    void deleteCourseById(Long id);
    boolean existsByName(String name);
}
