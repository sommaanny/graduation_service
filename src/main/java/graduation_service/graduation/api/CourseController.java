package graduation_service.graduation.api;

import graduation_service.graduation.dto.requestDto.courseDto.CourseCreateRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.serviceV1.CourseServiceV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceV1 courseService;

    @PostMapping("/course")
    public ApiResponse<CourseResponse> saveCourse(@RequestBody @Valid CourseCreateRequest courseCreateRequest) {
        CourseResponse courseResponse = courseService.addCourse(courseCreateRequest);
        return ApiResponse.success("과목 등록 성공", courseResponse);
    }

    @GetMapping("/course/id/{id}")
    public ApiResponse<CourseResponse> findCourse(@PathVariable("id") Long id) {
        CourseResponse courseResponse = courseService.findCourse(id);
        return ApiResponse.success("과목 조회 성공", courseResponse);
    }

    @GetMapping("/courses")
    public ApiResponse<List<CourseResponse>> findAllCourses() {
        List<CourseResponse> allCourse = courseService.findAllCourse();
        return ApiResponse.success("전체 과목 리스트 조회 성공", allCourse);
    }

    @GetMapping("/course/title")
    public ApiResponse<List<CourseResponse>> findByCourseTitle(@RequestParam("title") String courseTitle) {
        List<CourseResponse> byCourseTitle = courseService.findByCourseTitle(courseTitle);
        return ApiResponse.success("해당 제목의 과목 리스트 조회 성공", byCourseTitle);
    }

    @GetMapping("/course/credits")
    public ApiResponse<List<CourseResponse>> findByCredits(@RequestParam("credits") int credits) {
        List<CourseResponse> byCourseCredit = courseService.findByCourseCredit(credits);
        return ApiResponse.success("해당 학점에 과목 리스트 조회 성공", byCourseCredit);
    }

    @GetMapping("/course/courseNumber/{courseNumber}")
    public ApiResponse<CourseResponse> findByCourseNumber(@PathVariable("courseNumber") String courseNumber) {
        CourseResponse byCourseNumber = courseService.findByCourseNumber(courseNumber);
        return ApiResponse.success("해당 학수번호의 과목 조회 성공", byCourseNumber);
    }


    @DeleteMapping("/course/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("과목 삭제 성공, 과목 id: " + id, null);
    }
}
