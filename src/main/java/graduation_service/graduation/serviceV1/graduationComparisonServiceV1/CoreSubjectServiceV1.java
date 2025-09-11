package graduation_service.graduation.serviceV1.graduationComparisonServiceV1;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.requestDto.coreSubjectDto.CoreSubjectCreateRequest;
import graduation_service.graduation.dto.responseDto.coreResponse.CoreSubjectResponse;
import graduation_service.graduation.repository.CoreSubjectCurriculumRepository;
import graduation_service.graduation.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoreSubjectServiceV1 {

    private final CoreSubjectCurriculumRepository coreSubjectCurriculumRepository;
    private final CourseRepository courseRepository;

    //저장
    @Transactional
    public CoreSubjectResponse addCoreSubjectCurriculum(CoreSubjectCreateRequest coreSubjectCreateRequest) {
        Long courseId = coreSubjectCreateRequest.getCourseId();
        Course course = courseRepository.findOne(courseId);

        CoreSubjectCurriculum coreSubjectCurriculum = coreSubjectCreateRequest.toEntity(course);

        validateDuplicateCoreSubjectCurriculum(coreSubjectCurriculum); // 중복검사 -> 나중에 공통 로직으로 빼야겠다..(AOP?)
        coreSubjectCurriculumRepository.save(coreSubjectCurriculum);

        return new CoreSubjectResponse(coreSubjectCurriculum.getId(), courseId, coreSubjectCreateRequest.getCurriculumYear(), coreSubjectCurriculum.getCoreType());
    }

    //중복 검증
    private void validateDuplicateCoreSubjectCurriculum(CoreSubjectCurriculum coreSubjectCurriculum) {
        Optional<CoreSubjectCurriculum> findCourse =
                coreSubjectCurriculumRepository.findByCourse(coreSubjectCurriculum.getCourse(), coreSubjectCurriculum.getCurriculumYear());

        if (findCourse.isPresent()) {
            throw new IllegalStateException("이미 존재하는 핵심교양입니다: " + coreSubjectCurriculum.getCourse().getCourseTitle());
        }

    }

    public CoreSubjectResponse findById(Long id) {
        CoreSubjectCurriculum findOne = coreSubjectCurriculumRepository.findOne(id);
        return new CoreSubjectResponse(findOne.getId(), findOne.getCourse().getId(), findOne.getCurriculumYear(), findOne.getCoreType());
    }

    public CoreSubjectResponse findByCourse(Long courseId, int curriculumYear) {
        Course course = courseRepository.findOne(courseId);
        CoreSubjectCurriculum findOne = coreSubjectCurriculumRepository.findByCourse(course, curriculumYear)
                .orElseThrow(() -> new NoSuchElementException("해당 과목은 핵심교양이 아닙니다."));

        return new CoreSubjectResponse(findOne.getId(), findOne.getCourse().getId(), findOne.getCurriculumYear(), findOne.getCoreType());

    }

    public List<CoreSubjectResponse> findByCoreType(CoreType coreType) {
        return coreSubjectCurriculumRepository.findByCoreType(coreType).stream()
                .map(CoreSubjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //전체 핵심교양 리스트 조회
    public List<CoreSubjectResponse> findAll() {
        return coreSubjectCurriculumRepository.findAll().stream()
                .map(CoreSubjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //특정 년도 핵심교양 리스트 조회
    public List<CoreSubjectResponse> findByYear(int curriculumYear) {
        return coreSubjectCurriculumRepository.findByYear(curriculumYear)
                .stream().map(CoreSubjectResponse::fromEntity)
                .collect(Collectors.toList());
    }


    // 핵심교양 이수 체크
    public List<CoreType> checkCoreSubject(Set<String> courseNumberSet, int curriculumYear, GraduationRequirements graduationRequirements) {
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
