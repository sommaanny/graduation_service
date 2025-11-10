package graduation_service.graduation.serviceV0.graduationComparisonService;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.CreditStatusDto;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CompletedCourseCheckService {

    private final GraduationRequirementCoursesRepository grcRepository;

    //이수과목 체크
    @Transactional(readOnly = true)
    public List<GraduationRequirementsCourses> checkCompletedCourses(Set<String> courseNumberSet, Department department, int year, CreditStatusDto creditStatus) {
        //학과 졸업요건 과목 불러오기 (전선을 다 들었을 경우 전공선택은 제외하고 반환)
        List<GraduationRequirementsCourses> allGrc = grcRepository.findAll(department, year);

        // 제외할 과목 타입을 동적으로 결정
        Set<CourseType> excludedTypes = EnumSet.noneOf(CourseType.class);
        // 전공선택 남은 학점이 없다면 학수번호 비교 x
        if (creditStatus.getMissingElectiveMajorCredits() == 0) {
            excludedTypes.add(CourseType.MAJOR_ELECTIVE);
        }
        // 교양필수도 마찬가지
        if (creditStatus.getMissingRequiredGeneralCredits() == 0) {
            excludedTypes.add(CourseType.GENERAL_REQUIRED);
        }

        //필터링
        return allGrc.stream()
                .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber())) // 수강 안 한 과목만
                .filter(grc -> !excludedTypes.contains(grc.getCourseType()))                 // 제외 대상 아닌 것만
                .toList();
    }
}


