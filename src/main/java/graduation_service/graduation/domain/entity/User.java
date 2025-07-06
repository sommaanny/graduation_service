package graduation_service.graduation.domain.entity;

import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    // login_id
    private String loginId;

    // login_pw
    private String loginPw;

    // student_id
    private int studentId;

    //department
    @Enumerated(EnumType.STRING)
    private Department department;

    //graduation_requirements(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graduation_requirements_id")
    private GraduationRequirements graduationRequirement;

    public void setGraduationRequirements(GraduationRequirements requirement) {
        this.graduationRequirement = requirement;
    }

}
