package graduation_service.graduation.domain.dto;

import lombok.Data;

@Data
public class CreditStatusDto {

    private boolean creditPassed; //학점 충족 여부
    private int missingMajorCredits; //모자란 전공학점
    private int missingGeneralCredits; //모자란 교양학점
    private int missingTotalCredits; //모자란 총 학점

    public CreditStatusDto(int missingTotalCredits, int missingMajorCredits, int missingGeneralCredits) {
        this.creditPassed = (missingMajorCredits <= 0 && missingGeneralCredits <= 0 && missingTotalCredits <= 0);
        this.missingTotalCredits = missingTotalCredits;
        this.missingMajorCredits = missingMajorCredits;
        this.missingGeneralCredits = missingGeneralCredits;
    }
}
