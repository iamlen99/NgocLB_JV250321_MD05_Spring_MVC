package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    Enrollment saveEnrollment(Enrollment enrollment);
    Enrollment getEnrollmentByCourseIdAndStudentId(Long courseId, Long studentId);
    Page<Enrollment> getEnrollmentByStudentIdAndSearchValueAndStatus(Long studentId, Integer page, Integer size
            , String searchValue, EnrollmentStatus status);
    Page<Enrollment> getEnrollmentsByCourseName(Integer page, Integer size, String searchValue, EnrollmentStatus status);

    void confirmEnrollment(Long enrollmentId);
    void denyEnrollment(Long enrollmentId);
    void cancelEnrollment(Long enrollmentId);
}
