package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.model.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);
}
