package graduation_service.graduation.dto.responseDto.courseReponse;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private Long id;
    private String courseNumber;
    private String courseTitle;
    private int credits;
    private CourseType courseType;

    public CourseResponse(Long id, String courseNumber, String courseTitle, int credits) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        this.credits = credits;
    }

    public static CourseResponse fromEntity(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseNumber(),
                course.getCourseTitle(),
                course.getCredits()
        );
    }
}
