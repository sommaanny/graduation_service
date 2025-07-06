package graduation_service.graduation.domain.entity;

import graduation_service.graduation.domain.enums.CourseType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GraduationRequirementsCourses {

    @Id @GeneratedValue
    @Column(name = "graduation_requirements_courses_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graduation_requirements_id")
    private GraduationRequirements graduationRequirements;

    // Course 엔티티에서 삭제된 course_type을 대신해 추가해주었다.
    private CourseType courseType;
}
