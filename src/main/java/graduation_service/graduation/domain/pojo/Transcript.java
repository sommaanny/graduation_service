package graduation_service.graduation.domain.pojo;

import lombok.Getter;

@Getter
public class Transcript {

    private Long id;

    // 성적
    private float gpa;

    // 이수한 전공필수 학점
    private int requiredMajorCredits;

    // 이수한 전공선택 학점
    private int electiveMajorCredits;

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

}
