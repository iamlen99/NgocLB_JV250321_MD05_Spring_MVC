package ra.edu.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.format.annotation.DateTimeFormat;
import ra.edu.model.entity.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    @NotBlank(message = "Họ tên không được để trống!")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    @NotBlank(message = "Email không được để trống!")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Định dạng email không hợp lệ!"
    )
    private String email;

    @NotNull(message = "Giới tính không được để trống!")
    private Boolean sex;

    @NotBlank(message = "Số điện thoại không được để trống!")
    private String phone;
}
