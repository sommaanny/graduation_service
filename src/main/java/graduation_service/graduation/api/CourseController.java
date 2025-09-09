package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.*;
import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.serviceV1.CourseServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Course-Controller", description = "과목 관리 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceV1 courseService;

    @PostMapping("/course")
    @Operation(summary = "과목 등록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "과목 등록 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "과목 등록 성공 예시",
                            value = CourseCreateExample.COURSE_CREATE_SUCCESS
                    )
            ))
    public ApiResponse<CourseResponse> saveCourse(@RequestBody @Valid @Schema(implementation = CourseCreateRequest.class)
                                                      CourseCreateRequest courseCreateRequest) {
        CourseResponse courseResponse = courseService.addCourse(courseCreateRequest);
        return ApiResponse.success("과목 등록 성공", courseResponse);
    }



    @GetMapping("/course/id/{id}")
    @Operation(summary = "과목 id로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "과목 id로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "과목 id로 조회 성공 예시",
                            value = CourseFindExample.COURSE_FIND_SUCCESS
                    )
            ))
    public ApiResponse<CourseResponse> findCourse(@Parameter(description = "과목 id") @PathVariable("id") Long id) {
        CourseResponse courseResponse = courseService.findCourse(id);
        return ApiResponse.success("과목 조회 성공", courseResponse);
    }



    @GetMapping("/courses")
    @Operation(summary = "전체 과목 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 과목 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "전체 과목 리스트 조회 성공 예시",
                            value = CourseListFindExample.COURSE_LIST_FIND_SUCCESS
                    )
            ))
    public ApiResponse<List<CourseResponse>> findAllCourses() {
        List<CourseResponse> allCourse = courseService.findAllCourse();
        return ApiResponse.success("전체 과목 리스트 조회 성공", allCourse);
    }



    @GetMapping("/course/title")
    @Operation(summary = "특정 제목 과목 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 제목 과목 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "특정 제목 과목 리스트 조회 성공 예시",
                            value = CourseListFindByTitleExample.COURSE_LIST_FIND_BY_ID_SUCCESS
                    )
            ))
    public ApiResponse<List<CourseResponse>> findByCourseTitle(@Parameter(description = "과목명") @RequestParam("title") String courseTitle) {
        List<CourseResponse> byCourseTitle = courseService.findByCourseTitle(courseTitle);
        return ApiResponse.success("해당 제목의 과목 리스트 조회 성공", byCourseTitle);
    }




    @GetMapping("/course/credits")
    @Operation(summary = "특정 학점에 과목 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 학점 과목 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "특정 학점 과목 리스트 조회 성공 예시",
                            value = CourseListFindExample.COURSE_LIST_FIND_SUCCESS
                    )
            ))
    public ApiResponse<List<CourseResponse>> findByCredits(@Parameter(description = "학점") @RequestParam("credits") int credits) {
        List<CourseResponse> byCourseCredit = courseService.findByCourseCredit(credits);
        return ApiResponse.success("해당 학점에 과목 리스트 조회 성공", byCourseCredit);
    }



    @GetMapping("/course/courseNumber/{courseNumber}")
    @Operation(summary = "특정 학수번호 과목 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 학수번호 과목 조회",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "특정 학수번호 과목 조회 성공 예시",
                            value = CourseFindExample.COURSE_FIND_SUCCESS
                    )
            ))
    public ApiResponse<CourseResponse> findByCourseNumber(@Parameter(description = "학수번호") @PathVariable("courseNumber") String courseNumber) {
        CourseResponse byCourseNumber = courseService.findByCourseNumber(courseNumber);
        return ApiResponse.success("해당 학수번호의 과목 조회 성공", byCourseNumber);
    }




    @DeleteMapping("/course/{id}")
    @Operation(summary = "과목 삭제")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "과목 삭제",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "과목 삭제 성공 예시",
                            value = CourseDeleteExample.COURSE_DELETE_SUCCESS
                    )
            ))
    public ApiResponse<Void> deleteCourse(@Parameter(description = "과목 id") @PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("과목 삭제 성공, 과목 id: " + id, null);
    }
}
