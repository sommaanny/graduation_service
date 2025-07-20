package graduation_service.graduation.dto;

import lombok.Data;

@Data
public class GraduationRequirementUpdateDto {

    //이수해야 할 총 학점
    private int totalCreditsEarned;

    //이수해야 할 전공 학점
    private int majorCreditsEarned;

    //이수해야 할 교양 학점
    private int generalEducationCreditsEarned;

    //성적
    private float gpa;

    public GraduationRequirementUpdateDto(int totalCreditsEarned, int majorCreditsEarned, int generalEducationCreditsEarned, float gpa) {
        this.totalCreditsEarned = totalCreditsEarned;
        this.majorCreditsEarned = majorCreditsEarned;
        this.generalEducationCreditsEarned = generalEducationCreditsEarned;
        this.gpa = gpa;
    }
}
