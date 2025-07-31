package graduation_service.graduation.dto.requestDto.courseDto;

import graduation_service.graduation.domain.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateRequest {

    private String courseNumber;
    private String courseTitle;
    private int credits;

    public Course toEntity() {
        return new Course(courseNumber, courseTitle, credits);
    }

}
