package graduation_service.graduation.domain.pojo;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString
public class Transcript {

    // 성적
    @NotNull(message = "성적 입력은 필수입니다.")
    private float gpa;

    //총 취득학점
    private int totalCredits;

    // 이수한 전공필수 학점
    private int requiredMajorCredits;

    // 이수한 전공선택 학점
    private int electiveMajorCredits;

    // 이수한 기초전공 학점
    private int basicMajorCredits;

    // 이수한 필수교양 학점
    private int requiredGeneralEducationCredits;

    // 이수한 교양선택 학점
    private int electiveGeneralEducationCredits;

    // 편입 전공 인정학점
    private int transferredMajorCredits;

    // 편입 전체 인정학점
    private int totalTransferredCredits;

    // 기타 이수학점
    private int otherEarnedCredits;

    //이수 과목들의 학수번호들
    private Set<String> completedCourseNumbers = new HashSet<>();


    // 전공 총합
    public int getTotalMajorCredits() {
        return requiredMajorCredits + electiveMajorCredits + basicMajorCredits;
    }

    // 교양 총합
    public int getTotalGeneralEducationCredits() {
        return requiredGeneralEducationCredits + electiveGeneralEducationCredits + otherEarnedCredits;
    }


    //유효성 검사
    @AssertTrue(message = "총 이수 학점은 전공 + 교양 학점의 합이어야 합니다.")
    public boolean isTotalCreditsValid() {
        return totalCredits == getTotalMajorCredits() + getTotalGeneralEducationCredits();
    }

    @AssertTrue(message = "전공 총합은 전공필수 + 전공선택 + 기초전공의 합이어야 합니다.")
    public boolean isMajorCreditsValid() {
        return getTotalMajorCredits() == (requiredMajorCredits + electiveMajorCredits + basicMajorCredits);
    }

    @AssertTrue(message = "교양 총합은 교양필수 + 교양선택 + 일반교양의 합이어야 합니다.")
    public boolean isGeneralEducationCreditsValid() {
        return getTotalGeneralEducationCredits() == (requiredGeneralEducationCredits + electiveGeneralEducationCredits + otherEarnedCredits);
    }
}


