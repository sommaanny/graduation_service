package graduation_service.graduation;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.requestDto.courseDto.CourseRequest;
import graduation_service.graduation.dto.requestDto.graduationRequirementDto.GraduationRequirementCreateRequest;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import graduation_service.graduation.serviceV1.CourseServiceV1;
import graduation_service.graduation.serviceV1.GraduationRequirementCoursesServiceV1;
import graduation_service.graduation.serviceV1.GraduationRequirementServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CourseServiceV1 courseService;
    private final GraduationRequirementServiceV1 graduationRequirementService;


    @Override
    public void run(String... args) throws Exception {
        // 과목 추가
        CourseResponse courseResponse = courseService.addCourse(new CourseCreateRequest("CSE1001", "자료구조", 4));
        log.info("초기 과목 등록1");
        CourseResponse courseResponse1 = courseService.addCourse(new CourseCreateRequest("CSE1002", "알고리즘", 3));
        log.info("초기 과목 등록2");
        CourseResponse courseResponse2 = courseService.addCourse(new CourseCreateRequest("CSE1003", "운영체제", 3));
        log.info("초기 과목 등록3");


        // 졸업요건 추가
        GraduationRequirementResponse graduationRequirementResponse = graduationRequirementService.addGR(GraduationRequirementCreateRequest.builder()
                .department(Department.AI_ENGINEERING)
                .totalCredits(130)
                .majorCredits(65)
                .requiredMajorCredits(35)
                .generalEducationCredits(65)
                .requiredGeneralEducationCredits(30)
                .gpa(3.5F)
                .graduationRequirementsYear(2022)
                .build());

        log.info("초기 졸업요건 등록");

        //졸업요건에 과목 추가
        CourseRequest courseRequest = new CourseRequest(courseResponse.getId(), CourseType.MAJOR_REQUIRED);
        CourseRequest courseRequest1 = new CourseRequest(courseResponse1.getId(), CourseType.MAJOR_REQUIRED);
        CourseRequest courseRequest2 = new CourseRequest(courseResponse2.getId(), CourseType.MAJOR_ELECTIVE);

        graduationRequirementService.addCourseToGraduationRequirement(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                courseRequest
                );

        graduationRequirementService.addCourseToGraduationRequirement(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                courseRequest1
        );

        graduationRequirementService.addCourseToGraduationRequirement(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                courseRequest2
        );

        log.info("초기 졸업요건 과목 등록 완료");

        //졸업 요건에 핵심교양 추가
        graduationRequirementService.addCoreSubjectTypes(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                CoreType.CORE_1);

        graduationRequirementService.addCoreSubjectTypes(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                CoreType.CORE_2);

        graduationRequirementService.addCoreSubjectTypes(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                CoreType.CORE_3);

        graduationRequirementService.addCoreSubjectTypes(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                CoreType.CORE_5);

        graduationRequirementService.addCoreSubjectTypes(
                graduationRequirementResponse.getId(),
                graduationRequirementResponse.getGraduationRequirementsYear(),
                CoreType.CORE_6);
    }


}
