package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static graduation_service.graduation.domain.enums.CourseType.*;
import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class GraduationRequirementCoursesServiceTest {

    @Autowired GraduationRequirementCoursesService grcService;
    @Autowired CourseService courseService;
    @Autowired GraduationRequirementService grService;

    @Test
    void 졸업요건과목_저장_확인() {
        //given
        Course course = new Course("AIE-12235", "알고리즘", 3);
        GraduationRequirements graduationRequirements
                = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F);

        Long courseId = courseService.addCourse(course);
        Long grId = grService.addGR(graduationRequirements);

        grService.addCourseToGraduationRequirement(grId, course, MAJOR_REQUIRED);

        //when
        List<GraduationRequirementsCourses> allGrc = grcService.findAllGrc(AI_ENGINEERING);
        List<GraduationRequirementsCourses> grcByCourseType = grcService.findGrcByCourseType(AI_ENGINEERING, MAJOR_REQUIRED);

        //then

        for (GraduationRequirementsCourses grc : allGrc) {
            log.info("과목 확인: " + grc.getCourse().getCourseNumber());
            log.info("졸업요건 확인: " + grc.getGraduationRequirements().getDepartment());
        }

        for (GraduationRequirementsCourses grc : grcByCourseType) {
            log.info("과목 확인: " + grc.getCourse().getCourseNumber());
            log.info("졸업요건 확인: " + grc.getGraduationRequirements().getDepartment());
        }

        //assert 검증
        assertThat(allGrc.get(0).getCourse().getCourseNumber()).isEqualTo("AIE-12235");
        assertThat(allGrc.get(0).getGraduationRequirements().getDepartment()).isEqualTo(AI_ENGINEERING);

        assertThat(grcByCourseType.get(0).getCourse().getCourseNumber()).isEqualTo("AIE-12235");
        assertThat(grcByCourseType.get(0).getGraduationRequirements().getDepartment()).isEqualTo(AI_ENGINEERING);
    }


}