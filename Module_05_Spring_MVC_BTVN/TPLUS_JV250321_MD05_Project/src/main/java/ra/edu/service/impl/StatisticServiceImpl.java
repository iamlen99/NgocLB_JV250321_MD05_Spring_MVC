package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.CourseStatistics;
import ra.edu.model.dto.StudentCountPerCourse;
import ra.edu.model.entity.*;
import ra.edu.repository.CourseRepository;
import ra.edu.repository.EnrollmentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.StatisticService;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public Long countAllCourses() {
        return courseRepository.count();
    }

    @Override
    public Long countAllStudents() {
        return userRepository.countByRole(Role.STUDENT);
    }

    @Override
    public Long countEnrolledStudents() {
        return enrollmentRepository.countDistinctUserIdByStatus(EnrollmentStatus.CONFIRMED);
    }

    @Override
    public Page<StudentCountPerCourse> getTop5CoursesMostEnrolled() {
        return enrollmentRepository.getTop5CoursesMostEnrolled(PageRequest.of(0, 5));
    }

    @Override
    public Page<CourseStatistics> getStudentCountPerCourse(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return enrollmentRepository.getStudentCountPerCourse(pageable);
    }
}
