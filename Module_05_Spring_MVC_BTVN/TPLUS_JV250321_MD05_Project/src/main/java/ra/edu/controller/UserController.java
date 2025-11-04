package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.model.dto.UserLogin;
import ra.edu.model.dto.UserRegister;
import ra.edu.model.entity.Role;
import ra.edu.model.entity.User;
import ra.edu.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping(value = {"/","/users"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegister", new UserRegister());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userRegister") UserRegister userRegister,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.isExistEmail(userRegister.getEmail())) {
            result.rejectValue("email", "err.email", "Email đã tồn tại");
            return "auth/register";
        }

        if (userService.isExistPhone(userRegister.getPhone())) {
            result.rejectValue("phone", "err.phone", "Số điện thoại đã tồn tại");
            return "auth/register";
        }

        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "err.confirmPassword", "Mật khẩu xác nhận không trùng khớp");
            return "auth/register";
        }

        try {
            User savedUser = userService.save(userRegister.toEntity());
            redirectAttributes.addFlashAttribute("successMsg", "Đăng ký tài khoản " + savedUser.getEmail() + " thành công");
            return "redirect:/users/login";
        } catch (RuntimeException e) {
            model.addAttribute("errMsg", "Có lỗi trong quá trình đăng kí, xin thử lại");
            return "auth/register";
        }
    }

    @GetMapping(value = {"/","/login"})
    public String showLoginForm(Model model) {
        model.addAttribute("userLogin", new UserLogin());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("userLogin") UserLogin userLogin,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            HttpSession session
    ) {
        if (result.hasErrors()) {
            return "auth/login";
        }

        Optional<User> userOptional = userService.login(userLogin.getEmail(), userLogin.getPassword());

        if (userOptional.isPresent()) {
            User loggedUser = userOptional.get();
            session.setAttribute("loggedUser", loggedUser);
            redirectAttributes.addFlashAttribute("successMsg", "Đăng nhập thành công");
            if (Role.ADMIN.equals(loggedUser.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/student/courses";
        }
        model.addAttribute("errMsg", "Tài khoản hoặc mật khẩu không chính xác");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "auth/login";
    }
}
