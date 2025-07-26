package graduation_service.graduation.service.graduationComparisonService;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.repository.CoreSubjectCurriculumRepository;
import graduation_service.graduation.service.CourseService;
import graduation_service.graduation.service.GraduationRequirementService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static graduation_service.graduation.domain.enums.CoreType.CORE_1;
import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class CoreSubjectServiceTest {

    @Autowired
    CoreSubjectService coreSubjectService;

    @Autowired
    CoreSubjectCurriculumRepository coreSubjectCurriculumRepository;

    @Autowired
    CourseService courseService;

    @Autowired
    GraduationRequirementService grService;

    @BeforeEach
    void setUp() {
        //과목추가
        Course course1 = new Course("AIE3001", "기계학습", 3);
        Course course2 = new Course("AIE3002", "알고리즘", 3);
        Course course3 = new Course("MTH1001", "일반수학 1", 3);
        Course course4 = new Course("GED2010", "축제와 인간사회", 3);

        courseService.addCourse(course1);
        courseService.addCourse(course2);
        courseService.addCourse(course3);
        courseService.addCourse(course4);

        //졸업요건 추가
        GraduationRequirements gr = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F, 22);
        Long saveId = grService.addGR(gr, 22);

        //졸업 요건에 과목추가
        grService.addCourseToGraduationRequirement(saveId, 22, course1, CourseType.MAJOR_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course2, CourseType.MAJOR_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course3, CourseType.GENERAL_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course4, CourseType.GENERAL_REQUIRED);
    }

    @Test
    void DB_데이터_확인() {
        List<CoreSubjectCurriculum> all = coreSubjectCurriculumRepository.findAll();
        for (CoreSubjectCurriculum coreSubjectCurriculum : all) {
            log.info("과목 확인: " + coreSubjectCurriculum.getCourse().getCourseTitle());
        }
    }

    @Test
    void save() {
        //given
        Course findCourse = courseService.findByCourseNumber("GED2010").get();

        CoreSubjectCurriculum coreSubjectCurriculum = new CoreSubjectCurriculum();
        coreSubjectCurriculum.setCurriculumYear(22);
        coreSubjectCurriculum.setCoreType(CORE_1);
        coreSubjectCurriculum.assignCourse(findCourse);

        //when
        Long saveId = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum);


        //then
        CoreSubjectCurriculum find = coreSubjectService.findById(saveId);
        assertThat(find.getId()).isEqualTo(saveId);
    }



}