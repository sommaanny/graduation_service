package graduation_service.graduation.serviceV0;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementService {

    private final GraduationRequirementsRepository graduationRequirementsRepository;

    //저장
    @Transactional
    public Long addGR(GraduationRequirements graduationRequirement, int year) {
        validateDuplicateGR(graduationRequirement, year); //중복 검증
        graduationRequirementsRepository.save(graduationRequirement);
        return graduationRequirement.getId();
    }

    //중복 저장 검증
    private void validateDuplicateGR(GraduationRequirements graduationRequirement, int year) {
        Optional<GraduationRequirements> findGR = graduationRequirementsRepository.findByDepartment(graduationRequirement.getDepartment(), year);
        if (findGR.isPresent()) {
            throw new IllegalStateException("이미 존재하는 학과의 졸업요건입니다. 학과 : " + graduationRequirement.getDepartment());
        }
    }

    //졸업요건 과목 추가
    @Transactional
    public void addCourseToGraduationRequirement(Long grId, int year, Course course, CourseType courseType) {
        GraduationRequirementsCourses grc = new GraduationRequirementsCourses();
        grc.setCourse(course);
        grc.setCourseType(courseType);

        GraduationRequirements gr = findGR(grId, year);
        gr.addGraduationRequirementsCourses(grc);
    }

    //졸업요건에 핵심교양 조건 추가
    @Transactional
    public void addCoreSubjectTypes(Long grId, int year, CoreType coreType) {
        GraduationRequirements graduationRequirements = findGR(grId, year);
        graduationRequirements.addCoreType(coreType);
    }

    //조회
    public GraduationRequirements findGR(Long id, int year) {
        return graduationRequirementsRepository.findOne(id, year).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));
    }

    //학과로 조회
    public Optional<GraduationRequirements> findByGRDepartment(Department department, int year) {
        return graduationRequirementsRepository.findByDepartment(department, year);
    }

    //전체 조회
    public List<GraduationRequirements> findAllGR(int year) {
        return graduationRequirementsRepository.findAll(year);
    }

    //삭제
    @Transactional
    public void deleteGR(Long id, int year) {
        GraduationRequirements gr = findGR(id, year);
        if (gr == null) {
            throw new IllegalStateException("삭제할 졸업요건이 없습니다");
        }
        graduationRequirementsRepository.delete(gr);
    }

    //변경
    @Transactional
    public void updateGR(Long id, int year, GraduationRequirementUpdateDto updateDto) {
        GraduationRequirements gr = findGR(id, year);
        gr.updateGraduationRequirement(updateDto);
    }



}
