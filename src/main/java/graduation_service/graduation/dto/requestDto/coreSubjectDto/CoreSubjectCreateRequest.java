package graduation_service.graduation.dto.requestDto.coreSubjectDto;

import graduation_service.graduation.domain.entity.CoreSubjectCurriculum;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.enums.CoreType;
import lombok.Data;

@Data
public class CoreSubjectCreateRequest {
    private Long courseId;
    private CoreType coreType;
    private int curriculumYear;

    public CoreSubjectCurriculum toEntity(Course course) {
        CoreSubjectCurriculum coreSubjectCurriculum = new CoreSubjectCurriculum();
        coreSubjectCurriculum.setCoreType(coreType);
        coreSubjectCurriculum.setCurriculumYear(curriculumYear);
        coreSubjectCurriculum.assignCourse(course);

        return coreSubjectCurriculum;
    }
}
