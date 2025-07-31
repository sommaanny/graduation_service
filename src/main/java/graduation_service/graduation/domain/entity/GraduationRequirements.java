package graduation_service.graduation.domain.entity;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.domain.enums.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduationRequirements {

    @Id @GeneratedValue
    @Column(name = "graduation_requirements_id")
    private Long id;

    //리팩토리전 사용하던 생성자
    public GraduationRequirements(Department department, int totalCreditsEarned, int majorCreditsEarned, int generalEducationCreditsEarned, float gpa, int graduationRequirementsYear) {
        this.department = department;
        this.totalCreditsEarned = totalCreditsEarned;
        this.majorCreditsEarned = majorCreditsEarned;
        this.generalEducationCreditsEarned = generalEducationCreditsEarned;
        this.gpa = gpa;
        this.graduationRequirementsYear = graduationRequirementsYear;
    }

    //이수해야 할 총 학점
    private int totalCreditsEarned;

    //이수해야 할 전공 학점
    private int majorCreditsEarned;

    //이수해야 할 전공 필수 학점
    private int requiredMajorCreditsEarned;

    //이수해야 할 전공 선택 학점
    private int electiveMajorCreditsEarned;

    //이수해야 할 교양 학점
    private int generalEducationCreditsEarned;

    //이수해야 할 교양 필수 학점
    private int requiredGeneralEducationCreditsEarned;

    //이수해야 할 교양 선택 학점
    private int electiveGeneralEducationCreditsEarned;

    //성적
    private float gpa;

    //년도
    private int graduationRequirementsYear;

    //학과
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Department department;

    //이수해야 할 핵심교양 타입
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "required_core_types",
            joinColumns = @JoinColumn(name = "graduation_requirements_id")
    )
    @Enumerated(EnumType.STRING)
    private List<CoreType> coreTypes = new ArrayList<>();

    //mappedBy -> 연관관계 주인 지정(주인이 아닌 엔티티는 readOnly)
    //cascade는 연관된 엔티티를 함께 저장, 수정, 삭제할 때 사용
    //여기서는 CascadeType.ALL
    // orphanRemoval = true 부모 엔티티와의 연관이 끊어졌을 때 자식 엔티티 자동 삭제
    @Builder.Default
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
        requiredMajorCreditsEarned = updateDto.getRequiredMajorCredits(); //이수해야 할 전공 필수 학점
        electiveMajorCreditsEarned = majorCreditsEarned - requiredMajorCreditsEarned;  //이수해야 할 전공 선택 학점
        generalEducationCreditsEarned = updateDto.getGeneralEducationCreditsEarned(); //이수해야 할 교양 학점
        requiredGeneralEducationCreditsEarned = updateDto.getRequiredGeneralEducationCredits(); //이수해야 할 교양 필수학점
        electiveGeneralEducationCreditsEarned = generalEducationCreditsEarned - requiredGeneralEducationCreditsEarned; //이수해야 할 교양 선택학점
        gpa = updateDto.getGpa();
    }

    //졸업요건을 봤을 때 아래 항목들은 직접적으로 나타나있지 않고 관리자가 직접 계산해서 입력해야 하기에
    //우선 졸업요건을 등록시에는 직접적으로 나타나있는 정보들만 생성자를 통해 등록하게끔 만들고 추 후 아래 항목을 셋팅한다.
    //전공 필수, 전공 선택, 교양 필수, 교양 선택학점 셋팅

    public void setRequiredMajorCreditsEarned(int requiredMajorCreditsEarned) {
        this.requiredMajorCreditsEarned = requiredMajorCreditsEarned;
    }

    public void setElectiveMajorCreditsEarned(int electiveMajorCreditsEarned) {
        this.electiveMajorCreditsEarned = electiveMajorCreditsEarned;
    }

    public void setRequiredGeneralEducationCreditsEarned(int requiredGeneralEducationCreditsEarned) {
        this.requiredGeneralEducationCreditsEarned = requiredGeneralEducationCreditsEarned;
    }

    public void setElectiveGeneralEducationCreditsEarned(int electiveGeneralEducationCreditsEarned) {
        this.electiveGeneralEducationCreditsEarned = electiveGeneralEducationCreditsEarned;
    }

    //핵심교양 추가
    public void addCoreType(CoreType coreType) {
        coreTypes.add(coreType);
    }

    //제약조건
    public void validateCreditsConsistency() {
        if (totalCreditsEarned != majorCreditsEarned + generalEducationCreditsEarned) {
            throw new IllegalStateException("총 학점이 전공 + 교양 학점과 일치하지 않습니다.");
        }

        if (majorCreditsEarned != requiredMajorCreditsEarned + electiveMajorCreditsEarned) {
            throw new IllegalStateException("전공 학점이 전공 필수 + 전공 선택 학점과 일치하지 않습니다.");
        }

        if (generalEducationCreditsEarned != requiredGeneralEducationCreditsEarned + electiveGeneralEducationCreditsEarned) {
            throw new IllegalStateException("교양 학점이 교양 필수 + 교양 선택 학점과 일치하지 않습니다.");
        }
    }

}
