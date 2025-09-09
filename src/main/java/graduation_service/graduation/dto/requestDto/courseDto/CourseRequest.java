package graduation_service.graduation.dto.requestDto.courseDto;

import graduation_service.graduation.domain.enums.CourseType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    @NotNull(message = "과목 id는 필수입니다.")
    private Long courseId;

    @NotNull(message = "과목 타입은 필수입니다.")
    private CourseType courseType;
}
