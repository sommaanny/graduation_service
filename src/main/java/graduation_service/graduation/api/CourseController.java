package graduation_service.graduation.api;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.serviceV0.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/v1/course")
    public Long saveCourse(@RequestBody @Valid Course course) {
        Long id = courseService.addCourse(course);
        return id;
    }

    @GetMapping("/v1/course/{id}")
    public Course course(@PathVariable("id") Long id) {
        return courseService.findCourse(id);
    }

}
