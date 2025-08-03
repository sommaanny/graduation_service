package graduation_service.graduation.serviceV1;

import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.responseDto.courseReponse.CourseResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseResponse;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementCoursesServiceV1 {

    private final GraduationRequirementCoursesRepository grcRepository;
    private final GraduationRequirementsRepository graduationRequirementsRepository;

    //저장 로직은 졸업요건 서비스에 존재함으로 따로 만들 필요가 없다.

    //특정 학과의 졸업요건 과목 전체 조회
    public GraduationCourseResponse findAllGrc(Department department, int year) {

        GraduationRequirements findGr = graduationRequirementsRepository.findByDepartment(department, year)
                .orElseThrow(() -> new IllegalStateException("해당 학과의 해당하는 졸업요건을 찾을 수 없습니다."));

        GraduationCourseResponse graduationCourseResponse = GraduationCourseResponse.fromEntity(findGr, year, department);

        List<GraduationRequirementsCourses> all = grcRepository.findAll(department, year); //졸업요건 과목 조회
        //Dto로 변환
        for (GraduationRequirementsCourses graduationRequirementsCourses : all) {

            CourseResponse courseResponse = CourseResponse.fromEntity(graduationRequirementsCourses.getCourse());
            courseResponse.setCourseType(graduationRequirementsCourses.getCourseType());

            graduationCourseResponse.getCourses().add(courseResponse);
        }

        return graduationCourseResponse;
    }

    //특정 학과의 졸업요건 과목 중 과목 타입(전공, 교양 ..)으로 조회
    public GraduationCourseResponse findGrcByCourseType(Department department, CourseType courseType, int year) {
        GraduationRequirements findGr = graduationRequirementsRepository.findByDepartment(department, year)
                .orElseThrow(() -> new IllegalStateException("해당 학과의 해당하는 졸업요건을 찾을 수 없습니다."));

        GraduationCourseResponse graduationCourseResponse = GraduationCourseResponse.fromEntity(findGr, year, department);

        List<GraduationRequirementsCourses> all = grcRepository.findByCourseType(department, courseType, year);
        for (GraduationRequirementsCourses graduationRequirementsCourses : all) {

            CourseResponse courseResponse = CourseResponse.fromEntity(graduationRequirementsCourses.getCourse());
            courseResponse.setCourseType(graduationRequirementsCourses.getCourseType());

            graduationCourseResponse.getCourses().add(courseResponse);
        }

        return graduationCourseResponse;
    }


}
