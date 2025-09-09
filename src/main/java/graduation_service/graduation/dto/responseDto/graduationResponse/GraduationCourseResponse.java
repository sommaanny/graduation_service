package graduation_service.graduation.dto.responseDto.graduationResponse;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GraduationCourseResponse {

    private Long grId;
    private int year;
    private Department department;

    @Builder.Default
    private List<CourseResponse> courses = new ArrayList<>();

    public static GraduationCourseResponse fromEntity(GraduationRequirements gr, int year, Department department) {
        return GraduationCourseResponse.builder()
                .grId(gr.getId())
                .year(year)
                .department(department)
                .build();
    }

}
