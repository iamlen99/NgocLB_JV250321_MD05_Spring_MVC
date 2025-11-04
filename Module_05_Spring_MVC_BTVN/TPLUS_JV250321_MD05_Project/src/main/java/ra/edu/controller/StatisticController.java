package ra.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.model.dto.CourseStatistics;
import ra.edu.model.dto.StudentCountPerCourse;
import ra.edu.service.StatisticService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;


    @GetMapping("/dashboard")
    public String showStatistics(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            Model model
    ) {
        Long allCoursesCount = statisticService.countAllCourses();
        Long allStudentsCount = statisticService.countAllStudents();
        Long enrolledStudentsCount = statisticService.countEnrolledStudents();
        Page<CourseStatistics>  courseStatistics= statisticService.getStudentCountPerCourse(page, size);
        Page<StudentCountPerCourse> top5Courses = statisticService.getTop5CoursesMostEnrolled();

        model.addAttribute("allCoursesCount", allCoursesCount);
        model.addAttribute("allStudentsCount", allStudentsCount);
        model.addAttribute("enrolledStudentsCount", enrolledStudentsCount);
        model.addAttribute("courseStatistics", courseStatistics);
        model.addAttribute("top5Courses", top5Courses);
        return "admin/admin-index";
    }
}
