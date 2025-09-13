package graduation_service.graduation.serviceV1;

import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class CourseServiceV1Test {

    @Autowired
    CourseServiceV1 courseService;

    @Autowired
    EntityManager em;

    private CourseCreateRequest createRequest(String number, String title, int credits) {
        CourseCreateRequest request = new CourseCreateRequest();
        request.setCourseNumber(number);
        request.setCourseTitle(title);
        request.setCredits(credits);
        return request;
    }

    @Test
    void 과목저장() {
        CourseCreateRequest request = createRequest("AIE-12234", "기계학습", 3);

        CourseResponse response = courseService.addCourse(request);

        Assertions.assertThat(response.getCourseNumber()).isEqualTo("AIE-12234");
        Assertions.assertThat(response.getCourseTitle()).isEqualTo("기계학습");
    }

    @Test
    void 과목중복저장() {
        CourseCreateRequest request1 = createRequest("AIE-12234", "기계학습", 3);
        CourseCreateRequest request2 = createRequest("AIE-12234", "기계학습2", 3);

        courseService.addCourse(request1);

        Assertions.assertThatThrownBy(() -> courseService.addCourse(request2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 학수번호입니다");
    }

    @Test
    void 과목찾기() {
        CourseCreateRequest request = createRequest("AIE-12234", "기계학습", 3);
        CourseResponse saved = courseService.addCourse(request);

        em.flush();
        em.clear();

        CourseResponse find = courseService.findCourse(saved.getId());
        Assertions.assertThat(find.getCourseNumber()).isEqualTo("AIE-12234");
    }

    @Test
    void 전체과목조회() {
        courseService.addCourse(createRequest("AIE-12234", "기계학습", 3));
        courseService.addCourse(createRequest("AIE-12235", "알고리즘", 3));

        em.flush();
        em.clear();

        List<CourseResponse> result = courseService.findAllCourse();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).extracting("courseNumber")
                .containsExactlyInAnyOrder("AIE-12234", "AIE-12235");
    }

    @Test
    void 과목이름조회() {
        courseService.addCourse(createRequest("AIE-12234", "기계학습", 3));

        em.flush();
        em.clear();

        List<CourseResponse> result = courseService.findByCourseTitle("기계학습");

        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getCourseTitle()).isEqualTo("기계학습");
    }

    @Test
    void 학점으로조회() {
        courseService.addCourse(createRequest("AIE-12234", "기계학습", 3));
        courseService.addCourse(createRequest("AIE-12235", "알고리즘", 3));

        em.flush();
        em.clear();

        List<CourseResponse> result = courseService.findByCourseCredit(3);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).extracting("credits").containsOnly(3);
    }

    @Test
    void 학수번호조회() {
        courseService.addCourse(createRequest("AIE-12234", "기계학습", 3));

        em.flush();
        em.clear();

        CourseResponse result = courseService.findByCourseNumber("AIE-12234");

        Assertions.assertThat(result.getCourseNumber()).isEqualTo("AIE-12234");
        Assertions.assertThat(result.getCourseTitle()).isEqualTo("기계학습");
    }

    @Test
    void 과목삭제() {
        CourseResponse saved1 = courseService.addCourse(createRequest("AIE-12234", "기계학습", 3));
        courseService.addCourse(createRequest("AIE-12235", "알고리즘", 3));

        courseService.deleteCourse(saved1.getId());

        Assertions.assertThatThrownBy(() -> courseService.findByCourseNumber("AIE-12234"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 학수번호의 과목이 존재하지 않습니다.");

        CourseResponse remain = courseService.findByCourseNumber("AIE-12235");
        Assertions.assertThat(remain.getCourseTitle()).isEqualTo("알고리즘");
    }

    @Test
    void 없는과목삭제예외() {
        Assertions.assertThatThrownBy(() -> courseService.deleteCourse(999999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("삭제할 과목이 없습니다");
    }
}