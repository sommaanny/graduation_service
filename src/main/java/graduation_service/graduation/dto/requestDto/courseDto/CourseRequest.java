package graduation_service.graduation.dto.requestDto.courseDto;

import graduation_service.graduation.domain.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    private Long courseId;
    private CourseType courseType;
}
