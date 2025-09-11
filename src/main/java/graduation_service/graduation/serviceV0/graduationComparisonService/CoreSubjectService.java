package graduation_service.graduation.serviceV0.graduationComparisonService;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.repository.CoreSubjectCurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoreSubjectService {

    private final CoreSubjectCurriculumRepository coreSubjectCurriculumRepository;

    //저장
    @Transactional
    public Long addCoreSubjectCurriculum(CoreSubjectCurriculum coreSubjectCurriculum) {
        validateDuplicateCoreSubjectCurriculum(coreSubjectCurriculum); // 중복검사 -> 나중에 공통 로직으로 빼야겠다..(AOP?)
        coreSubjectCurriculumRepository.save(coreSubjectCurriculum);
        return coreSubjectCurriculum.getId();
    }

    //중복 검증
    private void validateDuplicateCoreSubjectCurriculum(CoreSubjectCurriculum coreSubjectCurriculum) {
        Optional<CoreSubjectCurriculum> findCourse =
                coreSubjectCurriculumRepository.findByCourse(coreSubjectCurriculum.getCourse(), coreSubjectCurriculum.getCurriculumYear());


        if (findCourse.isPresent()) {
            throw new IllegalStateException("이미 존재하는 핵심교양입니다: " + coreSubjectCurriculum.getCourse().getCourseTitle());
        }

    }

    public CoreSubjectCurriculum findById(Long id) {
        return coreSubjectCurriculumRepository.findOne(id);
    }

    public Optional<CoreSubjectCurriculum> findByCourse(Course course, int curriculumYear) {
        return coreSubjectCurriculumRepository.findByCourse(course, curriculumYear);
    }

    public List<CoreSubjectCurriculum> findByCoreType(CoreType coreType, int curriculumYear) {
        return coreSubjectCurriculumRepository.findByCoreType(coreType);
    }


    // 핵심교양 이수 체크
    public List<CoreType> checkCoreSubject(Set<String> courseNumberSet, GraduationRequirements graduationRequirements) {
        List<CoreType> requiredCoreTypes = graduationRequirements.getCoreTypes(); // 졸업요건에 명시된 핵심교양 타입 목록
        Set<CoreType> notCompletedTypes = new HashSet<>(requiredCoreTypes); // 결과 반환을 위한 Set (빠른 제거)

        for (CoreType coreType : requiredCoreTypes) {
            List<CoreSubjectCurriculum> subjects = coreSubjectCurriculumRepository.findByCoreType(coreType);

            for (CoreSubjectCurriculum subject : subjects) {
                String courseNumber = subject.getCourse().getCourseNumber();
                if (courseNumberSet.contains(courseNumber)) {
                    notCompletedTypes.remove(coreType); // 하나라도 이수하면 바로 제거
                    break; // 다음 coreType으로 넘어감
                }
            }
        }

        return new ArrayList<>(notCompletedTypes); // List로 반환
    }
}
