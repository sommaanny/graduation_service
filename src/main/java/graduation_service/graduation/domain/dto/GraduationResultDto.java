package graduation_service.graduation.domain.dto;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import lombok.Data;

import java.util.List;

@Data
public class GraduationResultDto {

    private boolean coursedPassed; //전공필수, 교양필수 다 이수 했는지
    private boolean englishPassed; //졸업 학점 다 채웠는지
    private boolean creditPassed; // 영어 성적 만족 하는지

    private boolean graduated; //졸업 가능한지
    private List<GraduationRequirementsCourses> remainingCourses; //이수하지 못한 과목들

    public GraduationResultDto(boolean graduated, boolean creditPassed, boolean englishPassed, boolean coursedPassed, List<GraduationRequirementsCourses> remainingCourses) {
        this.graduated = graduated;
        this.creditPassed = creditPassed;
        this.englishPassed = englishPassed;
        this.coursedPassed = coursedPassed;
        this.remainingCourses = remainingCourses;
    }
}