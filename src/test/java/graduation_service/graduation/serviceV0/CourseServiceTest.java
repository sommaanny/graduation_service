package graduation_service.graduation.serviceV0;

import graduation_service.graduation.domain.entity.Course;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional //Test에서 Transaction은 함수 실행 후 롤백되기에 디비에 데이터가 남아있는 것을 방지한다.
@SpringBootTest
class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @Test
    void 과목저장() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);

        //when
        Long saveId = courseService.addCourse(course);

        //then
        Assertions.assertThat(course.getId()).isEqualTo(saveId);
    }

    //중복 예외 발생
    @Test
    void 과목중복저장() {
        //given
        Course course1 = new Course("AIE-12234", "기계학습", 3);
        Course course2 = new Course("AIE-12234", "기계학습2", 3);

        courseService.addCourse(course1);

        //when, then
        Assertions.assertThatThrownBy(() -> courseService.addCourse(course2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 학수번호입니다");
    }


    @Test
    void 과목찾기() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);

        //when
        Long saveId = courseService.addCourse(course);
        Course findCourse = courseService.findCourse(saveId);

        //then
        Assertions.assertThat(findCourse.getId()).isEqualTo(saveId);
    }

    @Test
    void 전체과목조회() {
        //given
        Course course1 = new Course("AIE-12234", "기계학습", 3);
        Course course2 = new Course("AIE-12235", "알고리즘", 3);

        Long saveId1 = courseService.addCourse(course1);
        Long saveId2 = courseService.addCourse(course2);

        //when
        List<Course> allCourse = courseService.findAllCourse();

        //then
        for (Course course : allCourse) {
            log.info("과목명: "+ course.getCourseTitle() + ", 학수번호: " + course.getCourseNumber()
            + ", 학점: " + course.getCredits());
        }

        Assertions.assertThat(allCourse.size()).isEqualTo(2);
        Assertions.assertThat(allCourse).extracting("courseNumber")
                .containsExactlyInAnyOrder("AIE-12234", "AIE-12235");
    }

    @Test
    void 과목이름조회() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);
        courseService.addCourse(course);

        //when
        List<Course> findCourses = courseService.findByCourseTitle("기계학습");

        //then
        for (Course c : findCourses) {
            log.info("과목명: "+ c.getCourseTitle() + ", 학수번호: " + c.getCourseNumber()
                    + ", 학점: " + c.getCredits());
        }

        Assertions.assertThat(findCourses).extracting("courseTitle")
                    .containsOnly("기계학습");
    }

    @Test
    void 학점으로조회() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);
        Course course2 = new Course("AIE-12235", "알고리즘", 3);
        courseService.addCourse(course);
        courseService.addCourse(course2);

        //when
        List<Course> findByCourseCredit = courseService.findByCourseCredit(3);

        //then
        for (Course c : findByCourseCredit) {
            log.info("과목명: "+ c.getCourseTitle() + ", 학수번호: " + c.getCourseNumber()
                    + ", 학점: " + c.getCredits());
        }

        Assertions.assertThat(findByCourseCredit.size()).isEqualTo(2);
        Assertions.assertThat(findByCourseCredit).extracting("credits")
                .containsOnly(3);
    }

    @Test
    void 학수번호조회() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);
        Course course2 = new Course("AIE-12235", "알고리즘", 3);
        Long courseId1 = courseService.addCourse(course);
        Long courseId2 = courseService.addCourse(course2);

        //when
        Optional<Course> findCourse = courseService.findByCourseNumber("AIE-12234");

        //then
        Course courseResult = findCourse.orElseThrow(() -> new IllegalStateException("과목이 존재하지 않습니다"));
        Assertions.assertThat(courseResult.getCourseNumber()).isEqualTo("AIE-12234");
        }

    @Test
    void 과목삭제() {
        //given
        Course course = new Course("AIE-12234", "기계학습", 3);
        Course course2 = new Course("AIE-12235", "알고리즘", 3);
        Long courseId1 = courseService.addCourse(course);
        Long courseId2 = courseService.addCourse(course2);

        //when
        Optional<Course> findCourse = courseService.findByCourseNumber("AIE-12234");
        Course courseResult = findCourse.orElseThrow(() -> new IllegalStateException("과목이 존재하지 않습니다."));

        courseService.deleteCourse(courseResult.getId());

        //then
        Assertions.assertThatThrownBy(() -> courseService.findByCourseNumber("AIE-12234")
                        .orElseThrow(() -> new IllegalStateException("과목이 존재하지 않습니다.")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("과목이 존재하지 않습니다.");

        //알고리즘은 남아 있어야함
        Optional<Course> restCourse = courseService.findByCourseNumber("AIE-12235");
        Course result = restCourse.orElseThrow(() -> new IllegalStateException("과목이 존재하지 않습니다."));
        Assertions.assertThat(result.getCourseNumber()).isEqualTo("AIE-12235");
        Assertions.assertThat(result.getCourseTitle()).isEqualTo("알고리즘");
    }

    // 없는 과목을 삭제하려할 때 예외가 발생해야 함.
    @Test
    void 없는과목삭제예외() {
        Assertions.assertThatThrownBy(() -> courseService.deleteCourse(999999L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("삭제할 과목이 없습니다");
    }

}