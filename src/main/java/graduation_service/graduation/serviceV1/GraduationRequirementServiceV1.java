package graduation_service.graduation.serviceV1;

import graduation_service.graduation.domain.entity.Course;
import graduation_service.graduation.domain.entity.GraduationRequirements;
import graduation_service.graduation.domain.entity.GraduationRequirementsCourses;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.dto.requestDto.courseDto.CourseRequest;
import graduation_service.graduation.dto.requestDto.graduationRequirementDto.GraduationRequirementCreateRequest;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCoreSubjectCreateResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseCreateResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import graduation_service.graduation.repository.CourseRepository;
import graduation_service.graduation.repository.GraduationRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GraduationRequirementServiceV1 {

    private final GraduationRequirementsRepository graduationRequirementsRepository;
    private final CourseRepository courseRepository;
    private final CacheManager cacheManager;

    //저장
    @Transactional
    public GraduationRequirementResponse addGR(GraduationRequirementCreateRequest graduationRequirementCreateRequest) {
        GraduationRequirements graduationRequirement = graduationRequirementCreateRequest.toEntity();
        int year = graduationRequirement.getGraduationRequirementsYear();

        validateDuplicateGR(graduationRequirement, year); //중복 검증
        graduationRequirementsRepository.save(graduationRequirement);

        return GraduationRequirementResponse.fromEntity(graduationRequirement);
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
    public GraduationCourseCreateResponse addCourseToGraduationRequirement(Long grId, CourseRequest courseRequest) {
        Course course = courseRepository.findOne(courseRequest.getCourseId());

        if (course == null) {
            throw new NoSuchElementException("해당 ID의 과목이 존재하지 않습니다");
        }

        GraduationRequirementsCourses grc = new GraduationRequirementsCourses();
        grc.setCourse(course);
        grc.setCourseType(courseRequest.getCourseType());

        GraduationRequirements gr = graduationRequirementsRepository.findOne(grId).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));
        gr.addGraduationRequirementsCourses(grc);

        return new GraduationCourseCreateResponse(grId, gr.getGraduationRequirementsYear(), courseRequest.getCourseId(), courseRequest.getCourseType());
    }

    //졸업요건에 핵심교양 조건 추가
    @Transactional
    public GraduationCoreSubjectCreateResponse addCoreSubjectTypes(Long grId, int year, CoreType coreType) {
        GraduationRequirements graduationRequirements = graduationRequirementsRepository.findOne(grId).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));
        graduationRequirements.addCoreType(coreType);

        return new GraduationCoreSubjectCreateResponse(grId, year, coreType);
    }

    //졸업요건에 핵심교양 조건 삭제
    @Transactional
    public void deleteCoreSubjectTypes(Long grId, CoreType coreType) {
        GraduationRequirements graduationRequirements = graduationRequirementsRepository.findOne(grId).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));
        graduationRequirements.deleteCoreType(coreType);
    }

    //조회
    public GraduationRequirementResponse findGR(Long id) {
        GraduationRequirements graduationRequirements = graduationRequirementsRepository
                .findOne(id).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));

        return GraduationRequirementResponse.fromEntity(graduationRequirements);
    }

    //학과로 조회
    @Cacheable(value = "graduationCache", key = "#department.name() + '_' + #year")
    public GraduationRequirementResponse findByGRDepartment(Department department, int year) {
        GraduationRequirements graduationRequirements = graduationRequirementsRepository.findByDepartment(department, year)
                .orElseThrow(() -> new NoSuchElementException("해당 년도 해당 학과의 졸업 요건을 찾을 수 없습니다."));

        return GraduationRequirementResponse.fromEntity(graduationRequirements);
    }

    //전체 조회
    public List<GraduationRequirementResponse> findAllGR(int year) {
        return graduationRequirementsRepository.findAll(year).stream()
                .map(GraduationRequirementResponse::fromEntity)
                .toList();
    }

    //삭제
    @Transactional
    public void deleteGR(Long id) {
        GraduationRequirements gr = graduationRequirementsRepository
                .findOne(id).orElseThrow(() -> new NoSuchElementException("삭제할 졸업 요건을 찾을 수 없습니다."));

        graduationRequirementsRepository.delete(gr);

        // 캐시 삭제
        String key = gr.getDepartment().name() + "_" + gr.getGraduationRequirementsYear();
        cacheManager.getCache("graduationCache").evict(key);
    }

    //변경
    @Transactional
    public void updateGR(Long id, GraduationRequirementUpdateDto updateDto) {
        GraduationRequirements gr = graduationRequirementsRepository
                .findOne(id).orElseThrow(() -> new NoSuchElementException("졸업 요건을 찾을 수 없습니다."));
        gr.updateGraduationRequirement(updateDto);
        gr.validateCreditsConsistency();

        // 캐시 삭제
        String key = gr.getDepartment().name() + "_" + gr.getGraduationRequirementsYear();
        cacheManager.getCache("graduationCache").evict(key);
    }


}
