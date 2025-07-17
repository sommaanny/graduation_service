package graduation_service.graduation.service.graduationComparisonService;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.Transcript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GraduationCheckService {

    private final TranscriptExtractor transcriptExtractor;
    private final CompletedCourseCheckService completedCourseCheckService;
    private final EnglishScoreService englishScoreService;

    public Boolean checkGraduation(MultipartFile file, Department department) throws IOException {
        //이수 못한 과목 없는지 확인, 학점 다 채웠는지 확인

        //영어 성적 만족하는지
        return true;
    }

    //이수 못한 과목 반환
    public List<GraduationRequirementsCourses> checkRemainingCourses(MultipartFile file, Department department) throws IOException {
        Transcript transcript = transcriptExtractor.extract(file); // 성적표 추출
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers(); // 이수과목들 추출

        // 졸업요건과 비교하여 이수 못한 과목들 반환
        return completedCourseCheckService.checkCompletedCourses(completedCourseNumbers, department);
    }
}
