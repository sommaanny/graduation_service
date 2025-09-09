package graduation_service.graduation.dto;

import lombok.Data;

@Data
public class CreditStatusDto {

    private boolean creditPassed; //학점 충족 여부
    private int missingRequiredMajorCredits; //모자란 전공 필수학점
    private int missingElectiveMajorCredits; //모자란 전공 선택학점
    private int missingRequiredGeneralCredits; //모자란 교양 필수학점
    private int missingElectiveGeneralCredits; //모자란 교양 선택 학점
    private int missingTotalCredits; //모자란 총 학점

    public CreditStatusDto(int missingTotalCredits, int missingRequiredMajorCredits, int missingElectiveMajorCredits, int missingRequiredGeneralCredits, int missingElectiveGeneralCredits) {
        this.creditPassed = (missingRequiredMajorCredits <= 0 && missingElectiveMajorCredits <= 0 && missingTotalCredits <= 0
                                && missingRequiredGeneralCredits <= 0 && missingElectiveGeneralCredits <= 0);

        this.missingTotalCredits = missingTotalCredits;
        this.missingRequiredMajorCredits = missingRequiredMajorCredits;
        this.missingElectiveMajorCredits = missingElectiveMajorCredits;
        this.missingRequiredGeneralCredits = missingRequiredGeneralCredits;
        this.missingElectiveGeneralCredits = missingElectiveGeneralCredits;
    }
}
