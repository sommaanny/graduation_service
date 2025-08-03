package graduation_service.graduation.serviceV1;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * V1 - 도메인 엔티티를 직접 사용하는 것이 아닌 DTO를 사용
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceV1 {

    private final CourseRepository courseRepository;

    //저장
    @Transactional
    public CourseResponse addCourse(CourseCreateRequest courseCreateRequest) {
        Course course = courseCreateRequest.toEntity();
        validateDuplicateCourse(course); // 중복 검사
        courseRepository.save(course);

        return CourseResponse.fromEntity(course);
    }

    // 과목 중복 검증
    private void validateDuplicateCourse(Course course) {
        Optional<Course> findCourse = courseRepository.findByCourseNumber(course.getCourseNumber());
        if (findCourse.isPresent()) {
            throw new IllegalStateException("이미 존재하는 학수번호입니다: " + course.getCourseNumber());
        }
    }

    //조회
    public CourseResponse findCourse(Long id) {
        Course findCourse = courseRepository.findOne(id);
        return CourseResponse.fromEntity(findCourse);
    }

    //전체 조회
    public List<CourseResponse> findAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //강의 이름으로 조회
    public List<CourseResponse> findByCourseTitle(String title) {
        return courseRepository.findByCourseTitle(title)
                .stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //학점(2학점, 3학점 ..) 으로 조회
    public List<CourseResponse> findByCourseCredit(int credit) {
        return courseRepository.findByCredits(credit)
                .stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //학수 번호로 조회
    public CourseResponse findByCourseNumber(String courseNumber) {
        Course course = courseRepository.findByCourseNumber(courseNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 학수번호의 과목이 존재하지 않습니다."));

        return CourseResponse.fromEntity(course);
    }

    //삭제
    @Transactional
    public void deleteCourse(Long id) {
        Course findCourse = courseRepository.findOne(id);
        if (findCourse == null) {
            throw new IllegalStateException("삭제할 과목이 없습니다");
        }
        courseRepository.delete(findCourse);
    }

}
