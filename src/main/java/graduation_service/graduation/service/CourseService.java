package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    //저장
    @Transactional
    public Long addCourse(Course course) {
        validateDuplicateCourse(course); // 중복 검사
        courseRepository.save(course);
        return course.getId();
    }

    // 과목 중복 검증
    private void validateDuplicateCourse(Course course) {
        Optional<Course> findCourse = courseRepository.findByCourseNumber(course.getCourseNumber());
        if (findCourse.isPresent()) {
            throw new IllegalStateException("이미 존재하는 학수번호입니다: " + course.getCourseNumber());
        }
    }

    //조회
    public Course findCourse(Long id) {
        return courseRepository.findOne(id);
    }

    //전체 조회
    public List<Course> findAllCourse() {
        return courseRepository.findAll();
    }

    //강의 이름으로 조회
    public List<Course> findByCourseTitle(String title) {
        return courseRepository.findByCourseTitle(title);
    }

    //학점(2학점, 3학점 ..) 으로 조회
    public List<Course> findByCourseCredit(int credit) {
        return courseRepository.findByCredits(credit);
    }

    //학수 번호로 조회
    public Optional<Course> findByCourseNumber(String courseNumber) {
        return courseRepository.findByCourseNumber(courseNumber);
    }

    //삭제
    @Transactional
    public void deleteCourse(Long id) {
        Course findCourse = findCourse(id);
        if (findCourse == null) {
            throw new IllegalStateException("삭제할 과목이 없습니다");
        }
        courseRepository.delete(findCourse);
    }

}
