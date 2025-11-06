package ra.edu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatistics {
    private Long courseId;
    private String image;
    private String courseName;
    private Integer duration;
    private Long studentCount;
}
