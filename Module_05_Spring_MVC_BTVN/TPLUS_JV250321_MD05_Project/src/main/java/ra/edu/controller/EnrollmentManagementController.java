package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.model.entity.Enrollment;
import ra.edu.model.entity.EnrollmentStatus;
import ra.edu.model.entity.User;
import ra.edu.service.EnrollmentService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class EnrollmentManagementController {
    private final EnrollmentService enrollmentService;

    @GetMapping("/enrollments")
    public String enrollmentManagement(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "8") Integer size,
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

        Page<Enrollment> enrollments
                = enrollmentService.getEnrollmentsByCourseName(page, size, searchValue, EnrollmentStatus.fromString(status));
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        return "admin/enrollment/enrollment-list";
    }

    @GetMapping("/confirm-enrollment/{id}")
    public String confirmEnrollment(
            @PathVariable("id") Long id,
            @RequestParam(value = "status", required = false) String status,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        try {
            enrollmentService.confirmEnrollment(id);
            redirectAttributes.addFlashAttribute("successMsg", "Duyệt đăng ký khóa học thành công");
            redirectAttributes.addAttribute("status", status);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errMsg", e.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi trong quá trình duyệt đăng ký");
        }
        return "redirect:/admin/enrollments";
    }

    @GetMapping("/deny-enrollment/{id}")
    public String denyEnrollment(
            @PathVariable("id") Long id,
            @RequestParam(value = "status", required = false) String status,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("errMsg", "Phiên đăng nhập đã hết, xin mời đăng nhập lại");
            return "redirect:/users/login";
        }

        try {
            enrollmentService.denyEnrollment(id);
            redirectAttributes.addFlashAttribute("successMsg", "Từ chối đăng ký thành công");
            redirectAttributes.addAttribute("status", status);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errMsg", "Có lỗi trong quá trình từ chối đăng ký");
        }
        return "redirect:/admin/enrollments";
    }
}
