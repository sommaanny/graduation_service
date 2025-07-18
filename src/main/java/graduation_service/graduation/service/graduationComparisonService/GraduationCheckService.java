package graduation_service.graduation.service.graduationComparisonService;

import graduation_service.graduation.domain.dto.CreditStatusDto;
import graduation_service.graduation.domain.dto.GraduationResultDto;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.service.GraduationRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GraduationCheckService {

    private final TranscriptExtractor transcriptExtractor;
    private final CompletedCourseCheckService completedCourseCheckService;
    private final GraduationRequirementService graduationRequirementService;

    public GraduationResultDto checkGraduation(MultipartFile file, English english, Department department) throws IOException {
        //성적표 추출
        Transcript transcript = transcriptExtractor.extract(file);

        //졸업요건 조회
        Optional<GraduationRequirements> findGr = graduationRequirementService.findByGRDepartment(department);
        GraduationRequirements gr = findGr.orElseThrow(() -> new IllegalStateException("해당 학과의 졸업요건을 찾지 못했습니다."));

        //학점 충족 상태
        CreditStatusDto creditStatus = checkCredits(gr, transcript);
        boolean creditsPassed = creditStatus.isCreditPassed(); //학점 충족 여부

        //이수 못한 과목 없는지 확인
        List<GraduationRequirementsCourses> remainingCourses = checkRemainingCourses(transcript, department);
        boolean coursePassed = remainingCourses.isEmpty();

        //영어 성적 만족하는지
        boolean englishPassed = english.isPassed();

        boolean graduated = creditsPassed && coursePassed && englishPassed;

        //졸업요건을 모두 만족했다면 graduated = true
        //졸업요건을 만족하지 못했다면 graduated = false
        return new GraduationResultDto(graduated, creditStatus, englishPassed, coursePassed, remainingCourses);
    }

    //이수 못한 과목 반환
    public List<GraduationRequirementsCourses> checkRemainingCourses(Transcript transcript, Department department) throws IOException {
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        // 졸업요건과 비교하여 이수 못한 과목들 반환
        return completedCourseCheckService.checkCompletedCourses(completedCourseNumbers, department);
    }

    //이수한 학점 체크
    private static CreditStatusDto checkCredits(GraduationRequirements gr, Transcript transcript) {
        int totalCreditsRequired = gr.getTotalCreditsEarned(); //졸업에 필요한 총 학점
        int majorCreditsRequired = gr.getMajorCreditsEarned(); //졸업에 필요한 총 전공학점
        int generalCreditsRequired = gr.getGeneralEducationCreditsEarned(); //졸업에 필요한 총 교양학점

        //이수한 전공학점
        int majorCreditsEarned = transcript.getRequiredMajorCredits() //이수한 전공필수 +
                + transcript.getBasicMajorCredits() //이수한 기초전공 +
                + transcript.getElectiveMajorCredits() //이수한 전공선택 +
                + transcript.getTransferredMajorCredits(); //이수한 편입전공

        //이수한 교양학점
        int generalCreditsEarned = transcript.getRequiredGeneralEducationCredits() //이수한 교양필수 +
                + transcript.getElectiveGeneralEducationCredits() //이수한 교양선택 +
                + transcript.getOtherEarnedCredits() //이수한 기타교양 +
                + transcript.getTotalTransferredCredits() //이수한 편입 총 학점 -
                - transcript.getTransferredMajorCredits(); // 이수한 편입 전공학점

        int totalCredits = transcript.getTotalCredits();

        return new CreditStatusDto(
                Math.max(totalCreditsRequired - totalCredits, 0),  //모자란 학점이기에 음수가 나오면 안됨
                Math.max(majorCreditsRequired - majorCreditsEarned, 0), //전공학점이 -10학점 모자랍니다 -> 말이 안됨
                Math.max(generalCreditsRequired - generalCreditsEarned, 0));
    }
}
