package graduation_service.graduation.service;

import graduation_service.graduation.domain.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CourseType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementService {

    private final GraduationRequirementsRepository graduationRequirementsRepository;

    //저장
    @Transactional
    public Long addGR(GraduationRequirements graduationRequirement) {
        validateDuplicateGR(graduationRequirement); //중복 검증
        graduationRequirementsRepository.save(graduationRequirement);
        return graduationRequirement.getId();
    }

    //중복 저장 검증
    private void validateDuplicateGR(GraduationRequirements graduationRequirement) {
        Optional<GraduationRequirements> findGR = graduationRequirementsRepository.findByDepartment(graduationRequirement.getDepartment());
        if (findGR.isPresent()) {
            throw new IllegalStateException("이미 존재하는 학과의 졸업요건입니다. 학과 : " + graduationRequirement.getDepartment());
        }
    }

    //졸업요건 과목 추가
    @Transactional
    public void addCourseToGraduationRequirement(Long grId, Course course, CourseType courseType) {
        GraduationRequirementsCourses grc = new GraduationRequirementsCourses();
        grc.setCourse(course);
        grc.setCourseType(courseType);

        GraduationRequirements gr = findGR(grId);
        gr.addGraduationRequirementsCourses(grc);
    }

    //조회
    public GraduationRequirements findGR(Long id) {
        return graduationRequirementsRepository.findOne(id);
    }

    //학과로 조회
    public Optional<GraduationRequirements> findByGRDepartment(Department department) {
        return graduationRequirementsRepository.findByDepartment(department);
    }

    //전체 조회
    public List<GraduationRequirements> findAllGR() {
        return graduationRequirementsRepository.findAll();
    }

    //삭제
    @Transactional
    public void deleteGR(Long id) {
        GraduationRequirements gr = findGR(id);
        if (gr == null) {
            throw new IllegalStateException("삭제할 졸업요건이 없습니다");
        }
        graduationRequirementsRepository.delete(gr);
    }

    //변경
    @Transactional
    public void updateGR(Long id, GraduationRequirementUpdateDto updateDto) {
        GraduationRequirements gr = findGR(id);
        gr.updateGraduationRequirement(updateDto);
    }


}
