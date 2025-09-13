package graduation_service.graduation.serviceV0.graduationComparisonService;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.repository.CoreSubjectCurriculumRepository;
import graduation_service.graduation.serviceV0.CourseService;
import graduation_service.graduation.serviceV0.GraduationRequirementService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static graduation_service.graduation.domain.enums.CoreType.*;
import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@ActiveProfiles("test")
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

    @Autowired
    TranscriptExtractService transcriptExtractService;

    @Autowired
    GraduationRequirementService graduationRequirementService;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() {
        //과목추가
        Course course1 = new Course("AIE3001", "기계학습", 3);
        Course course2 = new Course("AIE3002", "알고리즘", 3);
        Course course3 = new Course("GED3001", "대중문화의 이해", 3);
        Course course4 = new Course("GED2010", "축제와 인간사회", 3);

        courseService.addCourse(course1);
        courseService.addCourse(course2);
        courseService.addCourse(course3);
        courseService.addCourse(course4);

        em.flush();
        em.clear();

        //졸업요건 추가
        GraduationRequirements gr = new GraduationRequirements(AI_ENGINEERING, 130, 65, 65, 3.0F, 22);

        gr.setRequiredMajorCreditsEarned(30); //전필 30학점
        gr.setElectiveMajorCreditsEarned(35); //전선 35학점
        gr.setRequiredGeneralEducationCreditsEarned(25); //교필 25
        gr.setElectiveGeneralEducationCreditsEarned(40); //교선 40
        gr.validateCreditsConsistency();

        Long saveId = grService.addGR(gr, 22);

        //졸업 요건에 과목추가
        grService.addCourseToGraduationRequirement(saveId, 22, course1, CourseType.MAJOR_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course2, CourseType.MAJOR_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course3, CourseType.GENERAL_REQUIRED);
        grService.addCourseToGraduationRequirement(saveId, 22, course4, CourseType.GENERAL_REQUIRED);

        //졸업 요건에 핵심교양 조건 추가
        grService.addCoreSubjectTypes(gr.getId(), gr.getGraduationRequirementsYear(), CORE_1);
        grService.addCoreSubjectTypes(gr.getId(), gr.getGraduationRequirementsYear(), CORE_2);
        grService.addCoreSubjectTypes(gr.getId(), gr.getGraduationRequirementsYear(), CORE_4);
        grService.addCoreSubjectTypes(gr.getId(), gr.getGraduationRequirementsYear(), CORE_5);

        em.flush();
        em.clear();
        log.info("========== 셋업 종료 ===========");
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

    @Test
    void 과목으로찾기() {
        //given
        Course findCourse = courseService.findByCourseNumber("GED2010").get();

        CoreSubjectCurriculum coreSubjectCurriculum = new CoreSubjectCurriculum();
        coreSubjectCurriculum.setCurriculumYear(22);
        coreSubjectCurriculum.setCoreType(CORE_1);
        coreSubjectCurriculum.assignCourse(findCourse);

        Long saveId = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum);

        em.flush();
        em.clear();

        //when
        Optional<CoreSubjectCurriculum> byCourse = coreSubjectService.findByCourse(findCourse, 22);
        CoreSubjectCurriculum coreSubjectCurriculum1 = byCourse.get();

        //then
        assertThat(coreSubjectCurriculum1.getId()).isEqualTo(saveId);
        log.info("타입: " + coreSubjectCurriculum1.getCoreType());
        log.info("연도: " + coreSubjectCurriculum1.getCurriculumYear());
        log.info("과목: " + coreSubjectCurriculum1.getCourse().getCourseTitle());
    }

    @Test
    void 과목타입으로찾기() {
        //given
        Course findCourse = courseService.findByCourseNumber("GED2010").get();
        Course findCourse2 = courseService.findByCourseNumber("GED3001").get();

        CoreSubjectCurriculum coreSubjectCurriculum = new CoreSubjectCurriculum();
        coreSubjectCurriculum.setCurriculumYear(22);
        coreSubjectCurriculum.setCoreType(CORE_1);
        coreSubjectCurriculum.assignCourse(findCourse);

        CoreSubjectCurriculum coreSubjectCurriculum2 = new CoreSubjectCurriculum();
        coreSubjectCurriculum2.setCurriculumYear(22);
        coreSubjectCurriculum2.setCoreType(CORE_1);
        coreSubjectCurriculum2.assignCourse(findCourse2);

        Long saveId = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum);
        Long saveId2 = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum2);

        //when
        List<CoreSubjectCurriculum> byCoreType = coreSubjectService.findByCoreType(CORE_1, 22);

        for (CoreSubjectCurriculum subjectCurriculum : byCoreType) {
            log.info("과목명: " + subjectCurriculum.getCourse().getCourseTitle());
            log.info("타입: " + subjectCurriculum.getCoreType());
            log.info("연도: " + subjectCurriculum.getCurriculumYear());
            log.info("-------------------------------");
        }

        assertThat(byCoreType.size()).isEqualTo(2);
    }

    @Test
    void 핵심교양_이수_체크() throws IOException {
        //given
        Course findCourse = courseService.findByCourseNumber("GED2010").get();
        Course findCourse2 = courseService.findByCourseNumber("GED3001").get();

        CoreSubjectCurriculum coreSubjectCurriculum = new CoreSubjectCurriculum();
        coreSubjectCurriculum.setCurriculumYear(22);
        coreSubjectCurriculum.setCoreType(CORE_1);
        coreSubjectCurriculum.assignCourse(findCourse);

        CoreSubjectCurriculum coreSubjectCurriculum2 = new CoreSubjectCurriculum();
        coreSubjectCurriculum2.setCurriculumYear(22);
        coreSubjectCurriculum2.setCoreType(CORE_2);
        coreSubjectCurriculum2.assignCourse(findCourse2);

        Long saveId = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum);
        Long saveId2 = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCurriculum2);

        File file = new File("src/test/resources/sample-transcript.pdf");
        FileInputStream input = new FileInputStream(file);

        //성적표 파일
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",                   // 파라미터 이름 (폼 필드 이름)
                file.getName(),                 // 파일 이름
                "application/pdf",             // Content-Type
                input                          // 파일 데이터 스트림
        );


        Transcript transcript = transcriptExtractService.extract(multipartFile);
        Set<String> completedCourseNumbers = transcript.getCompletedCourseNumbers();

        GraduationRequirements graduationRequirements = graduationRequirementService.findByGRDepartment(AI_ENGINEERING, 22).get();

        //when
        List<CoreType> coreTypes = coreSubjectService.checkCoreSubject(completedCourseNumbers, graduationRequirements);


        //then
        for (CoreType coreType : coreTypes) {
            log.info("남은 핵심교양: " + coreType);
        }

    }


}