package graduation_service.graduation.serviceV1;

import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.requestDto.courseDto.CourseRequest;
import graduation_service.graduation.dto.requestDto.graduationRequirementDto.GraduationRequirementCreateRequest;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static graduation_service.graduation.domain.enums.CourseType.MAJOR_REQUIRED;
import static graduation_service.graduation.domain.enums.Department.AI_ENGINEERING;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class GraduationRequirementCoursesServiceV1Test {

    @Autowired
    GraduationRequirementCoursesServiceV1 grcService;
    @Autowired
    CourseServiceV1 courseService;
    @Autowired
    GraduationRequirementServiceV1 grService;

    @Test
    void 졸업요건과목_저장_확인() {
        //given
        CourseCreateRequest courseCreateRequest = new CourseCreateRequest("AIE3001", "알고리즘", 3);

        CourseResponse courseResponse = courseService.addCourse(courseCreateRequest);

        GraduationRequirementCreateRequest graduationRequirementCreateRequest = GraduationRequirementCreateRequest.builder()
                        .department(AI_ENGINEERING)
                        .totalCredits(130)
                        .majorCredits(65)
                        .requiredMajorCredits(30)
                        .generalEducationCredits(65)
                        .requiredGeneralEducationCredits(30)
                        .gpa(3.5F)
                        .graduationRequirementsYear(22)
                        .build();

        GraduationRequirementResponse graduationRequirementResponse = grService.addGR(graduationRequirementCreateRequest);

        CourseRequest courseRequest = new CourseRequest(courseResponse.getId(), MAJOR_REQUIRED);

        grService.addCourseToGraduationRequirement(graduationRequirementResponse.getId(), graduationRequirementResponse.getGraduationRequirementsYear(), courseRequest);

        //when
        GraduationCourseResponse allGrc = grcService.findAllGrc(AI_ENGINEERING, 22);
        GraduationCourseResponse grcByCourseType = grcService.findGrcByCourseType(AI_ENGINEERING, MAJOR_REQUIRED, 22);

        //then
        log.info("졸업요건 확인: " + allGrc.getDepartment());
        for (CourseResponse grc : allGrc.getCourses()) {
            log.info("과목 확인: " + grc.getCourseNumber());
        }


        log.info("졸업요건 확인: " + grcByCourseType.getDepartment());
        for (CourseResponse grc : grcByCourseType.getCourses()) {
            log.info("과목 확인: " + grc.getCourseNumber());
        }

        //assert 검증
    }


}