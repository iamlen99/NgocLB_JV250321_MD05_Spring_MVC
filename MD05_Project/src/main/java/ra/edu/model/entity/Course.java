package ra.edu.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String instructor;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createAt;

    private String image;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDate.now();
    }
}
