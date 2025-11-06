package ra.edu.model.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Tên khóa học không được để trống!")
    private String name;

    @NotNull(message = "Thời lượng khóa học không được để trống!")
    @Min(value = 1, message = "Thời lượng khóa học nhỏ nhất là 1")
    private Integer duration;

    @NotBlank(message = "Giảng viên hướng dẫn không được để trống!")
    private String instructor;

    private MultipartFile file;
}
