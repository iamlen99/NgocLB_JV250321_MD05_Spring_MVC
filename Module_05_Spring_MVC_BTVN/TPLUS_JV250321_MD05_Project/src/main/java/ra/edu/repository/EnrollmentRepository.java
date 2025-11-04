package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.edu.model.dto.CourseStatistics;
import ra.edu.model.dto.StudentCountPerCourse;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentStatus;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Page<Enrollment> findByUserIdAndCourseNameContaining(Long userId, String courName, Pageable pageable);

    Page<Enrollment> findAllByCourseNameContaining(String courseName, Pageable pageable);

    Enrollment findByCourseIdAndUser_Id(Long courseId, Long userId);

    Page<Enrollment> findAllByCourseNameContainingAndStatus(String courseName, EnrollmentStatus status, Pageable pageable);

    @Query(
            "select count (distinct e.user.id) " +
                    "from Enrollment e " +
                    "where e.status = ra.edu.model.entity.EnrollmentStatus.CONFIRMED"
    )
    Long countDistinctUserIdByStatus(EnrollmentStatus enrollmentStatus);

    @Query(
            value = "SELECT new ra.edu.model.dto.CourseStatistics(" +
                    "c.id, " +
                    "c.image, " +
                    "c.name, " +
                    "c.duration, " +
                    "COUNT(e.user.id)) " +
                    "FROM Enrollment e " +
                    "JOIN e.course c " +
                    "WHERE e.status = ra.edu.model.entity.EnrollmentStatus.CONFIRMED " +
                    "GROUP BY c.id, c.image, c.name, c.duration " +
                    "ORDER BY c.name ASC",
            countQuery = "SELECT COUNT(DISTINCT c.id) " +
                    "FROM Enrollment e " +
                    "JOIN e.course c " +
                    "WHERE e.status = ra.edu.model.entity.EnrollmentStatus.CONFIRMED"
    )
    Page<CourseStatistics> getStudentCountPerCourse(Pageable pageable);


    @Query(
            "select new ra.edu.model.dto.StudentCountPerCourse(e.course.image, e.course.name, count(e.user.id)) " +
                    "from Enrollment e " +
                    "where e.status = ra.edu.model.entity.EnrollmentStatus.CONFIRMED " +
                    "group by e.course.name " +
                    "order by count (e.user.id) desc "
    )
    Page<StudentCountPerCourse> getTop5CoursesMostEnrolled(Pageable pageable);

    Page<Enrollment> findByUserIdAndCourseNameContainingAndStatus(Long userId, String courseName, EnrollmentStatus status, Pageable pageable);
}
