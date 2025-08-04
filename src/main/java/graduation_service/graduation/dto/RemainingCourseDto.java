package graduation_service.graduation.dto;

import graduation_service.graduation.domain.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemainingCourseDto {
    private String courseTitle;
    private String courseNumber;
    private int credits;
    private CourseType courseType;
}
