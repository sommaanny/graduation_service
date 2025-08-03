package graduation_service.graduation.dto.responseDto.graduationResponse;

import graduation_service.graduation.domain.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraduationCourseCreateResponse {
    private Long grId;
    private int year;
    private Long courseId;
    private CourseType courseType;
}
