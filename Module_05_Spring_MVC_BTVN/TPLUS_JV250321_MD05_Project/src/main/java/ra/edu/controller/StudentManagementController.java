package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.model.entity.User;
import ra.edu.service.StudentService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StudentManagementController {
    private final StudentService studentService;

    @GetMapping("/students")
    public String showStudents(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
            @RequestParam(name = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        Page<User> students = studentService.getAllStudentsByName(page - 1, size, searchValue, sortBy);
        model.addAttribute("students", students);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("sortBy", sortBy);
        return "admin/student/student-list";
    }

    @GetMapping("/change-student-status/{id}")
    public String changeStatus(
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
            Boolean studentStatus = studentService.toggleStudentStatus(id);
            redirectAttributes.addFlashAttribute("successMsg",
                    studentStatus ? "Mở khóa sinh viên thành công" : "Khóa sinh viên thành công");
        } catch (RuntimeException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi trong quá trình cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
}
