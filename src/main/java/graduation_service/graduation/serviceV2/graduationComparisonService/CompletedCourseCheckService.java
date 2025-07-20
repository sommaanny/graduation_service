package graduation_service.graduation.serviceV2.graduationComparisonService;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.serviceV2.GraduationRequirementCoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CompletedCourseCheckService {

    private final GraduationRequirementCoursesService grcService;

    //이수과목 체크
    @Transactional(readOnly = true)
    public List<GraduationRequirementsCourses> checkCompletedCourses(Set<String> courseNumberSet, Department department) {
        //학과 졸업요건 과목 불러오기
        List<GraduationRequirementsCourses> allGrc = grcService.findAllGrc(department);

        return allGrc.stream()
                .filter(grc -> !courseNumberSet.contains(grc.getCourse().getCourseNumber()))
                .toList();
    }
}


