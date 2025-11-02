package graduation_service.graduation.serviceV1.graduationComparisonServiceV1;


import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.dto.CreditStatusDto;
import graduation_service.graduation.dto.GraduationResultDto;
import graduation_service.graduation.dto.RemainingCourseDto;
import graduation_service.graduation.dto.requestDto.graduationCheckDto.GraduationCheckRequest;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import graduation_service.graduation.serviceV0.graduationComparisonService.CompletedCourseCheckService;
import graduation_service.graduation.serviceV1.GraduationRequirementServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraduationCheckServiceV1 {

    private final CompletedCourseCheckService completedCourseCheckService;
    private final GraduationRequirementServiceV1 graduationRequirementService;
    private final CoreSubjectServiceV1 coreSubjectService;

    @Transactional(readOnly = true)
    public GraduationResultDto checkGraduation(GraduationCheckRequest graduationCheckRequest) {

        Transcript transcript = graduationCheckRequest.getTranscript();
        English english = graduationCheckRequest.getEnglish();
        int studentId = extractEntranceYear(graduationCheckRequest.getStudentId());
        Department department = graduationCheckRequest.getDepartment();

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
        GraduationRequirementResponse findGr = graduationRequirementService.findByGRDepartment(department, studentId);

        //학점 충족 상태
        CreditStatusDto creditStatus = checkCredits(findGr, transcript);
        boolean creditsPassed = creditStatus.isCreditPassed(); //학점 충족 여부

        //핵심교양 이수여부 체크
        List<CoreType> remainingCoreTypes = checkRemainingCoreTypes(transcript, findGr);

        //이수 못한 과목 없는지 확인(핵심교양 제외)
        int missingElectiveMajorCredits = creditStatus.getMissingElectiveMajorCredits(); //모자른 전선학점
        List<RemainingCourseDto> remainingCourses = checkRemainingCourses(transcript, findGr.getDepartment(), findGr.getGraduationRequirementsYear(), missingElectiveMajorCredits);
        boolean coursePassed = remainingCourses.isEmpty();

        //영어 성적 만족하는지
        boolean englishPassed = english.isPassed();

        boolean graduated = creditsPassed && coursePassed && englishPassed;

        //졸업요건을 모두 만족했다면 graduated = true
        //졸업요건을 만족하지 못했다면 graduated = false
        return new GraduationResultDto(graduated, creditStatus, englishPassed, coursePassed, remainingCourses, remainingCoreTypes);
    }

    public List<CoreType> checkRemainingCoreTypes(Transcript transcript,  GraduationRequirementResponse gr) {
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        return coreSubjectService.checkCoreSubject(completedCourseNumbers, gr);
    }

    //이수 못한 과목 반환
    public List<RemainingCourseDto> checkRemainingCourses(Transcript transcript, Department department, int year, int missingElectiveMajorCredits) {
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        // 졸업요건과 비교하여 이수 못한 과목들 반환
        return completedCourseCheckService.checkCompletedCourses(completedCourseNumbers, department, year, missingElectiveMajorCredits)
                .stream()
                .map(grc -> new RemainingCourseDto(grc.getCourse().getCourseTitle(),
                                                    grc.getCourse().getCourseNumber(),
                                                    grc.getCourse().getCredits(),
                                                    grc.getCourseType()))
                .collect(Collectors.toList());
    }

    //이수한 학점 체크
    public static CreditStatusDto checkCredits(GraduationRequirementResponse gr, Transcript transcript) {
        int requiredMajorCreditsRequired = gr.getRequiredMajorCredits(); //졸업에  필요한 전공 필수학점
        int electiveMajorCreditsRequired = gr.getMajorCredits() - requiredMajorCreditsRequired; //졸업에 필요한 전공 선택학점
        int requiredGeneralEducationCreditsRequired = gr.getRequiredGeneralEducationCredits(); //졸업에 필요한 교양 필수학점
        int electiveGeneralEducationCreditsRequired = gr.getGeneralEducationCredits() - requiredGeneralEducationCreditsRequired; //졸업에 필요한 교양 선택학점

        //이수한 전공 필수학점
        int requiredMajorCreditsEarned = transcript.getRequiredMajorCredits();

        //이수한 전공 선택학점
        int electiveMajorCreditsEarned = transcript.getElectiveMajorCredits();

        //이수한 교양 필수학점
        int requiredGeneralEducationCreditsEarned = transcript.getRequiredGeneralEducationCredits();

        //이수한 교양 선택학점
        int electiveGeneralEducationCreditsEarned = transcript.getElectiveGeneralEducationCredits();

        //모자란 전필 학점
        int majorRequiredCreditsGap = Math.max(requiredMajorCreditsRequired - requiredMajorCreditsEarned, 0);

        //모자란 전선 학점
        int majorElectiveCreditsGap = Math.max(electiveMajorCreditsRequired - electiveMajorCreditsEarned, 0);

        //모자란 교필
        int generalRequiredCreditsGap = Math.max(requiredGeneralEducationCreditsRequired - requiredGeneralEducationCreditsEarned, 0);

        //모자란 교선 학점
        int generalElectiveCreditsGap = Math.max(electiveGeneralEducationCreditsRequired - electiveGeneralEducationCreditsEarned, 0);

        //모자란 총 학점(위에꺼 더하면 됨)
        int totalCreditsGap = majorRequiredCreditsGap + majorElectiveCreditsGap + generalRequiredCreditsGap + generalElectiveCreditsGap;

        return new CreditStatusDto(
                totalCreditsGap,  //모자란 학점이기에 음수가 나오면 안됨
                majorRequiredCreditsGap, //전공학점이 -10학점 모자랍니다 -> 말이 안됨
                majorElectiveCreditsGap,
                generalRequiredCreditsGap,
                generalElectiveCreditsGap;
    }


    public static int extractEntranceYear(String studentId) {
        if (studentId == null || studentId.length() < 8) {
            throw new IllegalArgumentException("학번 형식이 올바르지 않습니다.");
        }

        String yearPart = studentId.substring(2, 4); // 인덱스 2 ~ 3
        int year = Integer.parseInt(yearPart); // 예: "22" -> 22

        return 2000 + year; // 2022년 입학으로 처리
    }

}
