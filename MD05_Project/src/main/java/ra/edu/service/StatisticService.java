package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.dto.CourseStatistics;
import ra.edu.model.dto.StudentCountPerCourse;

import java.util.List;

public interface StatisticService {
    Long countAllCourses();
    Long countAllStudents();
    Long countEnrolledStudents();
    Page<CourseStatistics> getStudentCountPerCourse(Integer page, Integer size);
    Page<StudentCountPerCourse> getTop5CoursesMostEnrolled();
}
