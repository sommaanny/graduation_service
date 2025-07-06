package graduation_service.graduation.domain.entity;

import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GraduationRequirements {

    @Id @GeneratedValue
    @Column(name = "graduation_requirements_id")
    private Long id;

    //이수해야 할 총 학점
    private int totalCreditsEarned;

    //이수해야 할 전공 학점
    private int majorCreditsEarned;

    //이수해야 할 교양 학점
    private int generalEducationCreditsEarned;

    //성적
    private float gpa;

    //학과
    @Enumerated(EnumType.STRING)
    private Department department;
}
