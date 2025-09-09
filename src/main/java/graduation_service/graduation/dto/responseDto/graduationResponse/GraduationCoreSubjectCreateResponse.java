package graduation_service.graduation.dto.responseDto.graduationResponse;

import graduation_service.graduation.domain.enums.CoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraduationCoreSubjectCreateResponse {

    private Long grId;
    private int year;
    private CoreType coreType;
}
