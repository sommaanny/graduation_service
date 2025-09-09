package graduation_service.graduation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraduationRequirementUpdateDto {

    //이수해야 할 총 학점
    private int totalCreditsEarned;

    //이수해야 할 전공 학점
    private int majorCreditsEarned;

    //이수해야 할 전공 필수 학점
    private int requiredMajorCredits;

    //이수해야 할 교양 학점
    private int generalEducationCreditsEarned;

    //이수해야 할 교양 필수 학점
    private int requiredGeneralEducationCredits;

    //성적
    private float gpa;
}
