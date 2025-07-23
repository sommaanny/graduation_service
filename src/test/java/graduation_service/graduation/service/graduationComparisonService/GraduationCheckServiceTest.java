package graduation_service.graduation.service.graduationComparisonService;

import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.dto.GraduationResultDto;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.service.CourseService;
import graduation_service.graduation.service.GraduationRequirementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static graduation_service.graduation.domain.enums.Department.*;
import static graduation_service.graduation.domain.enums.TestType.*;

@Slf4j
@SpringBootTest
class GraduationCheckServiceTest {

    @Autowired
    GraduationCheckService graduationCheckService;

    @Autowired
    CourseService courseService;

    @Autowired
    GraduationRequirementService grService;

    @Autowired
    TranscriptExtractService transcriptExtractService;

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
    void 졸업요건_진단() throws IOException {

        File file = new File("src/test/resources/sample-transcript.pdf");
        FileInputStream input = new FileInputStream(file);

        //성적표 파일
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",                   // 파라미터 이름 (폼 필드 이름)
                file.getName(),                 // 파일 이름
                "application/pdf",             // Content-Type
                input                          // 파일 데이터 스트림
        );

        //영어점수
        English english1 = new English(TOEIC, 900); //토익
        English english2 = new English(OPIC, "IM2"); //오픽

        //학부
        Department department = AI_ENGINEERING;

        //성적표 추출
        Transcript transcript = transcriptExtractService.extract(multipartFile);

        //졸업여부 체크
        GraduationResultDto result = graduationCheckService.checkGraduation(transcript, english1, 22, department); //학번

        log.info("졸업요건 만족 여부: " + result.isGraduated());

        log.info("학점 이수 여부: " + result.getCreditStatus().isCreditPassed());
        log.info("모자란 총 학점: " + result.getCreditStatus().getMissingTotalCredits());
        log.info("모자란 전공 학점: " + result.getCreditStatus().getMissingMajorCredits());
        log.info("모자란 교양 학점: " + result.getCreditStatus().getMissingGeneralCredits());

        log.info("필수과목 이수 여부: " + result.isCoursedPassed());
        log.info("영어 성적 여부: " + result.isEnglishPassed());

    }
}