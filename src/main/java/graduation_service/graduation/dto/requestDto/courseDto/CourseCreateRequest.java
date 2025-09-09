package graduation_service.graduation.dto.requestDto.courseDto;

import graduation_service.graduation.domain.entity.Course;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateRequest {

    @NotBlank(message = "학수번호는 필수입니다.")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}$", message = "힉수번호 형식이 올바르지 않습니다.(예: AIE3001)")
    private String courseNumber;

    @NotBlank(message = "과목명은 필수입니다.")
    private String courseTitle;

    @Min(value = 1, message = "최소 학점은 1 이상입니다.")
    private int credits;

    public Course toEntity() {
        return new Course(courseNumber, courseTitle, credits);
    }

}
