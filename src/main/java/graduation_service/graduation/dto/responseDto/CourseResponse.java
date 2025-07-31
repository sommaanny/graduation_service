package graduation_service.graduation.dto.responseDto;

import graduation_service.graduation.domain.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private String courseNumber;
    private String courseTitle;
    private int credits;

    public static CourseResponse fromEntity(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseNumber(),
                course.getCourseTitle(),
                course.getCredits()
        );
    }
}
