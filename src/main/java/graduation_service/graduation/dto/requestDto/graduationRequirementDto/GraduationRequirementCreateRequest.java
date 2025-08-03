package graduation_service.graduation.dto.requestDto.graduationRequirementDto;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class GraduationRequirementCreateRequest {

    @NotNull(message = "학과 입력은 필수입니다.")
    private Department department;

    @Min(value = 1, message = "총 학점은 1 이상이어야 합니다.")
    private int totalCredits;

    @Min(value = 1, message = "전공 학점은 1 이상이어야 합니다.")
    private int majorCredits;

    @Min(value = 1, message = "전공필수 학점은 1 이상이어야 합니다.")
    private int requiredMajorCredits; //전공 필수

    @Min(value = 1, message = "교양 학점은 1 이상이어야 합니다.")
    private int generalEducationCredits;

    @Min(value = 1, message = "교양필수 학점은 1 이상이어야 합니다.")
    private int requiredGeneralEducationCredits; //교양 필수

    @DecimalMin(value = "0.0", message = "GPA는 0.0 이상이어야 합니다.")
    @DecimalMax(value = "4.5", message = "GPA는 4.5 이하여야 합니다.")
    private float gpa;

    @Min(value = 2000, message = "졸업 요건 연도는 2000년 이후여야 합니다.")
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
