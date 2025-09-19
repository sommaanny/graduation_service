package graduation_service.graduation.serviceV0.graduationComparisonService;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CompletedCourseCheckService {

    private final GraduationRequirementCoursesRepository grcRepository;

    //이수과목 체크
    @Transactional(readOnly = true)
    public List<GraduationRequirementsCourses> checkCompletedCourses(Set<String> courseNumberSet, Department department, int year, int missingElectiveMajorCredits) {
        //학과 졸업요건 과목 불러오기 (전선을 다 들었을 경우 전공선택은 제외하고 반환)
        List<GraduationRequirementsCourses> allGrc = grcRepository.findAll(department, year);

        if (missingElectiveMajorCredits == 0) {
            return allGrc.stream()
                    .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber()) &&
                            grc.getCourseType() != CourseType.MAJOR_ELECTIVE
                    )
                    .toList();
        }

        return allGrc.stream()
                .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber()))
                .toList();
    }
}


