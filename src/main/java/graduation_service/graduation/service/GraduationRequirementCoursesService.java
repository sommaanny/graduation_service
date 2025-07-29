package graduation_service.graduation.service;

import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.repository.GraduationRequirementCoursesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementCoursesService {

    private final GraduationRequirementCoursesRepository grcRepository;

    //저장 로직은 졸업요건 서비스에 존재함으로 따로 만들 필요가 없다.

    //특정 학과의 졸업요건 과목 전체 조회
    public List<GraduationRequirementsCourses> findAllGrc(Department department, int year) {
        return grcRepository.findAll(department, year);
    }

    //특정 학과의 졸업요건 과목 중 과목 타입(전공, 교양 ..)으로 조회
    public List<GraduationRequirementsCourses> findGrcByCourseType(Department department, CourseType courseType, int year) {
        return grcRepository.findByCourseType(department, courseType, year);
    }

}
