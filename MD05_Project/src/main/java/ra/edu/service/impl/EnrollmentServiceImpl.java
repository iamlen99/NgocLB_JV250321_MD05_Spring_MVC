package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentStatus;
import ra.edu.repository.EnrollmentRepository;
import ra.edu.service.EnrollmentService;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    private Enrollment getWaitingEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy đăng ký có id = " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.WAITING) {
            throw new IllegalStateException("Chỉ được thao tác khi trạng thái đăng ký là WAITING");
        }
        return enrollment;
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment getEnrollmentByCourseIdAndStudentId(Long courseId, Long studentId) {
        return enrollmentRepository.findByCourseIdAndUser_Id(courseId, studentId);
    }

    @Override
    public Page<Enrollment> getEnrollmentByStudentIdAndSearchValueAndStatus(Long studentId, Integer page, Integer size,
                                                                            String searchValue, EnrollmentStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        if (status == null) {
            return enrollmentRepository.findByUserIdAndCourseNameContaining(studentId, searchValue, pageable);
        }
        return enrollmentRepository.findByUserIdAndCourseNameContainingAndStatus(studentId, searchValue, status, pageable);
    }

    @Override
    public Page<Enrollment> getEnrollmentsByCourseName(Integer page, Integer size, String searchValue, EnrollmentStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        if (status == null) {
            return enrollmentRepository.findAllByCourseNameContaining(searchValue, pageable);
        }
        return enrollmentRepository.findAllByCourseNameContainingAndStatus(searchValue, status, pageable);
    }

    @Override
    public void confirmEnrollment(Long enrollmentId) {
        Enrollment enrollment = getWaitingEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.CONFIRMED);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void denyEnrollment(Long enrollmentId) {
        Enrollment enrollment = getWaitingEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.DENIED);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy bản đăng ký có id = " + enrollmentId));
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
    }
}
