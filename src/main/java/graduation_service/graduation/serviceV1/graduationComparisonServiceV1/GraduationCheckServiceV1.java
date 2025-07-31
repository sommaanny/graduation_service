package graduation_service.graduation.serviceV1.graduationComparisonServiceV1;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.dto.CreditStatusDto;
import graduation_service.graduation.dto.GraduationResultDto;
import graduation_service.graduation.serviceV0.GraduationRequirementService;
import graduation_service.graduation.serviceV0.graduationComparisonService.CompletedCourseCheckService;
import graduation_service.graduation.serviceV0.graduationComparisonService.CoreSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GraduationCheckServiceV1 {

    private final CompletedCourseCheckService completedCourseCheckService;
    private final GraduationRequirementService graduationRequirementService;
    private final CoreSubjectService coreSubjectService;

    @Transactional(readOnly = true)
    public GraduationResultDto checkGraduation(Transcript transcript, English english, int studentId, Department department) throws IOException {

        //편입생일 경우 편입학점을 기존 학점에 합산(추후 편입생 학점 이수인정표 pdf를 추가로 받아서 처리하는 기능을 만들 예정)
        int transferredMajorCredits = transcript.getTransferredMajorCredits(); //편입 전공
        int transferredGeneralCredits = transcript.getTotalTransferredCredits() - transferredMajorCredits; //편입 교양
        if (transcript.getTotalTransferredCredits() != 0) {
            transcript.setElectiveMajorCredits(transcript.getElectiveMajorCredits() + transferredMajorCredits); //편입 전공 기존 전공선택 학점에 합산
            transcript.setElectiveGeneralEducationCredits(transcript.getElectiveGeneralEducationCredits() + transferredGeneralCredits); //편입 전공 기존 교양선택 학점에 합산
            transcript.setTransferredMajorCredits(0);
            transcript.setTotalTransferredCredits(0);
        }

        //졸업요건 조회
        Optional<GraduationRequirements> findGr = graduationRequirementService.findByGRDepartment(department, studentId);
        GraduationRequirements gr = findGr.orElseThrow(() -> new IllegalStateException("해당 학과의 졸업요건을 찾지 못했습니다."));

        //학점 충족 상태
        CreditStatusDto creditStatus = checkCredits(gr, transcript);
        boolean creditsPassed = creditStatus.isCreditPassed(); //학점 충족 여부

        //핵심교양 이수여부 체크
        List<CoreType> remainingCoreTypes = checkRemainingCoreTypes(transcript, gr);

        //이수 못한 과목 없는지 확인(핵심교양 제외)
        int missingElectiveMajorCredits = creditStatus.getMissingElectiveMajorCredits(); //모자른 전선학점
        List<GraduationRequirementsCourses> remainingCourses = checkRemainingCourses(transcript, gr, missingElectiveMajorCredits);
        boolean coursePassed = remainingCourses.isEmpty();

        //영어 성적 만족하는지
        boolean englishPassed = english.isPassed();

        boolean graduated = creditsPassed && coursePassed && englishPassed;

        //졸업요건을 모두 만족했다면 graduated = true
        //졸업요건을 만족하지 못했다면 graduated = false
        return new GraduationResultDto(graduated, creditStatus, englishPassed, coursePassed, remainingCourses, remainingCoreTypes);
    }

    public List<CoreType> checkRemainingCoreTypes(Transcript transcript, GraduationRequirements gr) {
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        return coreSubjectService.checkCoreSubject(completedCourseNumbers, gr.getGraduationRequirementsYear(), gr);
    }

    //이수 못한 과목 반환
    public List<GraduationRequirementsCourses> checkRemainingCourses(Transcript transcript, GraduationRequirements gr, int missingElectiveMajorCredits) throws IOException {
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        // 졸업요건과 비교하여 이수 못한 과목들 반환
        return completedCourseCheckService.checkCompletedCourses(completedCourseNumbers, gr, missingElectiveMajorCredits);
    }

    //이수한 학점 체크
    public static CreditStatusDto checkCredits(GraduationRequirements gr, Transcript transcript) {
        int totalCreditsRequired = gr.getTotalCreditsEarned(); //졸업에 필요한 총 학점
        int requiredMajorCreditsRequired = gr.getRequiredMajorCreditsEarned();//졸업에  필요한 전공 필수학점
        int electiveMajorCreditsRequired = gr.getElectiveMajorCreditsEarned(); //졸업에 필요한 전공 선택학점
        int requiredGeneralEducationCreditsRequired = gr.getRequiredGeneralEducationCreditsEarned(); //졸업에 필요한 교양 필수학점
        int electiveGeneralEducationCreditsRequired = gr.getElectiveGeneralEducationCreditsEarned(); //졸업에 필요한 교양 선택학점

        //이수한 전공 필수학점
        int requiredMajorCreditsEarned = transcript.getRequiredMajorCredits();

        //이수한 전공 선택학점
        int electiveMajorCreditsEarned = transcript.getElectiveMajorCredits();

        //이수한 교양 필수학점
        int requiredGeneralEducationCreditsEarned = transcript.getRequiredGeneralEducationCredits();

        //이수한 교양 선택학점
        int electiveGeneralEducationCreditsEarned = transcript.getElectiveGeneralEducationCredits();

        //이수한 총 학점
        int totalCredits = transcript.getTotalCredits();

        return new CreditStatusDto(
                Math.max(totalCreditsRequired - totalCredits, 0),  //모자란 학점이기에 음수가 나오면 안됨
                Math.max(requiredMajorCreditsRequired - requiredMajorCreditsEarned, 0), //전공학점이 -10학점 모자랍니다 -> 말이 안됨
                Math.max(electiveMajorCreditsRequired - electiveMajorCreditsEarned, 0),
                Math.max(requiredGeneralEducationCreditsRequired - requiredGeneralEducationCreditsEarned, 0),
                Math.max(electiveGeneralEducationCreditsRequired - electiveGeneralEducationCreditsEarned, 0));
    }
}
