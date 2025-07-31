package graduation_service.graduation.dto.requestDto.graduationRequirementDto;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class GraduationRequirementCreateRequest {

    private Department department;
    private int totalCredits;
    private int majorCredits;
    private int requiredMajorCredits; //전공 필수
    private int generalEducationCredits;
    private int requiredGeneralEducationCredits; //교양 필수
    private float gpa;
    private int graduationRequirementsYear;

    public GraduationRequirements toEntity() {
        return GraduationRequirements.builder()
                .department(department)
                .totalCreditsEarned(totalCredits)
                .majorCreditsEarned(majorCredits)
                .requiredMajorCreditsEarned(requiredMajorCredits)
                .electiveMajorCreditsEarned(majorCredits - requiredMajorCredits) //전공 선택
                .generalEducationCreditsEarned(generalEducationCredits)
                .requiredGeneralEducationCreditsEarned(requiredGeneralEducationCredits)
                .electiveGeneralEducationCreditsEarned(generalEducationCredits - requiredGeneralEducationCredits) //교양 선택
                .gpa(gpa)
                .graduationRequirementsYear(graduationRequirementsYear)
                .build();
    }

}
