package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.GraduationCourseExample;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseResponse;
import graduation_service.graduation.serviceV1.GraduationRequirementCoursesServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Graduation-Course-Controller", description = "졸업요건 과목 조회 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class GraduationCourseController {

    private final GraduationRequirementCoursesServiceV1 graduationRequirementCoursesService;

    @GetMapping("/graduation-requirements/courses")
    @Operation(summary = "졸업요건 과목 조회", description = "특정 학과 특정 년도의 졸업요건 속 존재하는 과목 리스트 조회하는 API")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 과목 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 과목조회 성공 예시",
                            value = GraduationCourseExample.GRADUATION_COURSES_SUCCESS
                    )
            ))
    public ApiResponse<GraduationCourseResponse> findCourses(@Parameter(description = "학과") @RequestParam("department") String urlDepartment,
                                                             @Parameter(description = "연도") @RequestParam("year") int year,
                                                             @Parameter(description = "과목 타입") @RequestParam(value = "courseType", required = false) String urlCourseType) {

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
