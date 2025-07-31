package graduation_service.graduation.dto.responseDto;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraduationRequirementResponse {

    private Long id;
    private Department department;
    private int totalCredits;
    private int majorCredits;
    private int requiredMajorCredits; //전공 필수
    private int generalEducationCredits;
    private int requiredGeneralEducationCredits; //교양 필수
    private float gpa;
    private int graduationRequirementsYear;

    public static GraduationRequirementResponse fromEntity(GraduationRequirements graduationRequirements) {
        return GraduationRequirementResponse.builder()
                .id(graduationRequirements.getId())
                .department(graduationRequirements.getDepartment())
                .totalCredits(graduationRequirements.getTotalCreditsEarned())
                .majorCredits(graduationRequirements.getMajorCreditsEarned())
                .requiredMajorCredits(graduationRequirements.getRequiredMajorCreditsEarned())
                .generalEducationCredits(graduationRequirements.getGeneralEducationCreditsEarned())
                .requiredGeneralEducationCredits(graduationRequirements.getRequiredGeneralEducationCreditsEarned())
                .gpa(graduationRequirements.getGpa())
                .graduationRequirementsYear(graduationRequirements.getGraduationRequirementsYear())
                .build();
    }

}
