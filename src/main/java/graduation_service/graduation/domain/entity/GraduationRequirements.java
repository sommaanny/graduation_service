package graduation_service.graduation.domain.entity;

import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class GraduationRequirements {

    @Id @GeneratedValue
    @Column(name = "graduation_requirements_id")
    private Long id;

    public GraduationRequirements() {
    }

    public GraduationRequirements(Department department, int totalCreditsEarned, int majorCreditsEarned, int generalEducationCreditsEarned, float gpa) {
        this.department = department;
        this.totalCreditsEarned = totalCreditsEarned;
        this.majorCreditsEarned = majorCreditsEarned;
        this.generalEducationCreditsEarned = generalEducationCreditsEarned;
        this.gpa = gpa;
    }

    //이수해야 할 총 학점
    private int totalCreditsEarned;

    //이수해야 할 전공 학점
    private int majorCreditsEarned;

    //이수해야 할 교양 학점
    private int generalEducationCreditsEarned;

    //성적
    private float gpa;

    //학과
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Department department;

    //mappedBy -> 연관관계 주인 지정(주인이 아닌 엔티티는 readOnly)
    //cascade는 연관된 엔티티를 함께 저장, 수정, 삭제할 때 사용
    //여기서는 CascadeType.ALL
    // orphanRemoval = true 부모 엔티티와의 연관이 끊어졌을 때 자식 엔티티 자동 삭제
    @OneToMany(mappedBy = "graduationRequirements", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GraduationRequirementsCourses> graduationRequirementsCourses = new ArrayList<>();

    //연관관계 편의 메서드
    public void addGraduationRequirementsCourses(GraduationRequirementsCourses grc) {
        graduationRequirementsCourses.add(grc);
        grc.setGraduationRequirements(this);
    }

    //업데이트 메서드
    public void updateGraduationRequirement(GraduationRequirementUpdateDto updateDto) {
        totalCreditsEarned = updateDto.getTotalCreditsEarned(); //이수해야 할 총 학점
        majorCreditsEarned = updateDto.getMajorCreditsEarned(); //이수해야 할 전공 학점
        generalEducationCreditsEarned = updateDto.getGeneralEducationCreditsEarned(); //이수해야 할 교양 학점
        gpa = updateDto.getGpa();
    }
}
