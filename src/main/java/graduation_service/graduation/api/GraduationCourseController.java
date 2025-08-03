package graduation_service.graduation.api;

import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseResponse;
import graduation_service.graduation.serviceV1.GraduationRequirementCoursesServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraduationCourseController {

    private final GraduationRequirementCoursesServiceV1 graduationRequirementCoursesService;

    @GetMapping("/graduation-requirements/courses")
    public ApiResponse<GraduationCourseResponse> findCourses(@RequestParam("department") String urlDepartment,
                                                             @RequestParam("year") int year,
                                                             @RequestParam(value = "courseType", required = false) String urlCourseType) {

        Department department = Department.fromUrl(urlDepartment);

        if (urlCourseType != null) {
            CourseType courseType = CourseType.fromUrlName(urlCourseType);
            GraduationCourseResponse filtered = graduationRequirementCoursesService.findGrcByCourseType(department, courseType, year);
            return ApiResponse.success("해당 년도 해당 학과의 특정 과목타입 리스트 조회 성공", filtered);
        } else {
            GraduationCourseResponse all = graduationRequirementCoursesService.findAllGrc(department, year);
            return ApiResponse.success("해당 년도 해당 학과의 전체 과목 리스트 조회 성공", all);
        }
    }
}
