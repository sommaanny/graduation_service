package graduation_service.graduation.dto.responseDto.coreResponse;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.enums.CoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreSubjectResponse {

    private Long coreId;
    private Long courseId;
    private int curriculumYear;
    private CoreType coreType;

    public static CoreSubjectResponse fromEntity(CoreSubjectCurriculum coreSubjectCurriculum) {
        return new CoreSubjectResponse(
                coreSubjectCurriculum.getId(),
                coreSubjectCurriculum.getCourse().getId(),
                coreSubjectCurriculum.getCurriculumYear(),
                coreSubjectCurriculum.getCoreType()
        );
    }
}
