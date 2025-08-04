package graduation_service.graduation.dto.requestDto.graduationCheckDto;

import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.pojo.English;
import graduation_service.graduation.domain.pojo.Transcript;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraduationCheckRequest {

    @Valid
    @NotNull(message = "성적표 정보는 필수입니다.")
    private Transcript transcript;

    @NotBlank(message = "학번은 필수입니다.")
    private String studentId;

    @NotNull(message = "학과는 필수입니다.")
    private Department department;

    @NotNull(message = "영어 점수는 필수입니다.")
    private English english;

}
