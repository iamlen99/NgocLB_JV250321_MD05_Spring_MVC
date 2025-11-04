package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.model.dto.ChangePasswordDTO;
import ra.edu.model.dto.UserUpdate;
import ra.edu.model.entity.Course;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentStatus;
import ra.edu.model.entity.User;
import ra.edu.service.CourseService;
import ra.edu.service.EnrollmentService;
import ra.edu.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/courses")
    public String showCourseList(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        Page<Course> courses = courseService.getAllCoursesByName(page, size, searchValue, null);
        model.addAttribute("courses", courses);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("page", page);
        return "student/course/course-list";
    }

    @GetMapping("/register-course/{id}")
    public String registerCourse(
            @PathVariable("id") Long courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if (courseOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi khi lấy id khóa học, xin thử lại");
            return "redirect:/student/register-course";
        }

        Enrollment enrollmentCheck = enrollmentService.getEnrollmentByCourseIdAndStudentId(courseId, loggedUser.getId());
        if (enrollmentCheck != null) {
            redirectAttributes.addFlashAttribute("errMsg", "Bạn đã đăng ký khóa học này rồi");
            return "redirect:/student/courses";
        }

        Enrollment enrollment = new Enrollment();
        Course course = courseOptional.get();
        enrollment.setCourse(course);
        enrollment.setUser(loggedUser);

        try {
            Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
            redirectAttributes.addFlashAttribute("successMsg", "Đăng ký khóa học " +
                    savedEnrollment.getCourse().getName() + " thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi khi đăng ký khóa học, xin thử lại");
        }
        return "redirect:/student/courses";
    }

    @GetMapping("/enrollments")
    public String showEnrollmentHistoryList(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "status", required = false) String status,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        Page<Enrollment> enrollments = enrollmentService.getEnrollmentByStudentIdAndSearchValueAndStatus(loggedUser.getId(), page, size, searchValue, EnrollmentStatus.fromString(status));
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        return "student/enrollment/enrollment-list";
    }

    @GetMapping("/cancel-enrollment/{id}")
    public String cancelEnrollment(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        try {
            enrollmentService.cancelEnrollment(id);
            redirectAttributes.addFlashAttribute("successMsg", "Hủy đăng ký khóa học thành công");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi trong quá trình hủy đăng ký khóa học");
        }
        return "redirect:/student/enrollments";
    }

    @GetMapping("/edit-student")
    public String showEditStudentPage(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        UserUpdate studentUpdate = new UserUpdate();
        studentUpdate.setName(loggedUser.getName());
        studentUpdate.setDob(loggedUser.getDob());
        studentUpdate.setEmail(loggedUser.getEmail());
        studentUpdate.setPhone(loggedUser.getPhone());
        studentUpdate.setSex(loggedUser.getSex());

        if (!model.containsAttribute("studentUpdate")) {
            model.addAttribute("studentUpdate", studentUpdate);
        }
        if (!model.containsAttribute("changePasswordDTO")) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        }
        return "student/edit-student";
    }

    @PostMapping("/update-student")
    public String updateStudent(
            @Valid @ModelAttribute("studentUpdate") UserUpdate studentUpdate,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model
    ) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "student/edit-student";
        }

        if (!loggedUser.getEmail().equals(studentUpdate.getEmail()) &&
                userService.isExistEmail(studentUpdate.getEmail())
        ) {
            bindingResult.rejectValue("email", "err.email", "Email đã tồn tại");
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "student/edit-student";
        }

        if (!loggedUser.getPhone().equals(studentUpdate.getPhone()) &&
                userService.isExistPhone(studentUpdate.getPhone())
        ) {
            model.addAttribute("studentUpdate", studentUpdate);
            bindingResult.rejectValue("phone", "err.phone", "Số điện thoại đã tồn tại");
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "student/edit-student";
        }

        Optional<User> userOpt = userService.findById(loggedUser.getId());
        if (userOpt.isEmpty()) {
            model.addAttribute("errMsg", "Có lỗi trong quá trình lấy id tài khoản, xin thử lại");
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            return "student/edit-student";
        }

        User userUpdate = userOpt.get();

        userUpdate.setName(studentUpdate.getName());
        userUpdate.setDob(studentUpdate.getDob());
        userUpdate.setEmail(studentUpdate.getEmail());
        userUpdate.setSex(studentUpdate.getSex());
        userUpdate.setPhone(studentUpdate.getPhone());

        try {
            userService.save(userUpdate);
            session.setAttribute("loggedUser", userUpdate);
            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật thông tin tài khoản thành công");
            return "redirect:/student/courses";
        } catch (RuntimeException e) {
            e.printStackTrace();
            model.addAttribute("errMsg", "Có lỗi trong quá trình cập nhật: " + e.getMessage());
            return "student/edit-student";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO changePasswordDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDTO", bindingResult);
            redirectAttributes.addFlashAttribute("changePasswordDTO", changePasswordDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/student/edit-student";
        }

        if (!BCrypt.checkpw(changePasswordDTO.getCurrentPassword(), loggedUser.getPassword())) {
            bindingResult.rejectValue("currentPassword", "err.currentPassword", "Mật khẩu hiện tại không đúng");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDTO", bindingResult);
            redirectAttributes.addFlashAttribute("changePasswordDTO", changePasswordDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/student/edit-student";
        }
        ;

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "err.confirmNewPassword", "Mật khẩu xác nhận không trùng khớp");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDTO", bindingResult);
            redirectAttributes.addFlashAttribute("changePasswordDTO", changePasswordDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/student/edit-student";
        }
        ;

        Optional<User> userOpt = userService.findById(loggedUser.getId());
        if (userOpt.isEmpty()) {
            model.addAttribute("errMsg", "Có lỗi trong quá trình lấy id tài khoản, xin thử lại");
            return "student/edit-student";
        }

        User userUpdate = userOpt.get();

        String hashedPassword = BCrypt.hashpw(changePasswordDTO.getNewPassword(), BCrypt.gensalt(12));
        userUpdate.setPassword(hashedPassword);
        loggedUser.setPassword(hashedPassword);

        try {
            userService.save(userUpdate);
            session.setAttribute("loggedUser", userUpdate);
            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật mật khẩu thành công");
        } catch (RuntimeException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi trong quá trình cập nhật: " + e.getMessage());
        }
        return "redirect:/student/edit-student";
    }
}
