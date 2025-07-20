package graduation_service.graduation.dto;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import lombok.Data;

import java.util.List;

@Data
public class GraduationResultDto {

    private CreditStatusDto creditStatus; //학점 충족 상태
    private boolean coursedPassed; //전공필수, 교양필수 다 이수 했는지
    private boolean englishPassed; // 영어 성적 만족 하는지

    private boolean graduated; //졸업 가능한지
    private List<GraduationRequirementsCourses> remainingCourses; //이수하지 못한 과목들

    public GraduationResultDto(boolean graduated,CreditStatusDto creditStatus, boolean englishPassed, boolean coursedPassed, List<GraduationRequirementsCourses> remainingCourses) {
        this.graduated = graduated;
        this.creditStatus = creditStatus;
        this.englishPassed = englishPassed;
        this.coursedPassed = coursedPassed;
        this.remainingCourses = remainingCourses;
    }
}