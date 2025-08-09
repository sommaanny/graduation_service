package graduation_service.graduation.api;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.dto.GraduationRequirementUpdateDto;
import graduation_service.graduation.dto.requestDto.courseDto.CourseRequest;
import graduation_service.graduation.dto.requestDto.graduationRequirementDto.GraduationRequirementCreateRequest;
import graduation_service.graduation.dto.responseDto.*;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCoreSubjectCreateResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationCourseCreateResponse;
import graduation_service.graduation.dto.responseDto.graduationResponse.GraduationRequirementResponse;
import graduation_service.graduation.serviceV1.GraduationRequirementServiceV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GraduationRequirementController {

    private final GraduationRequirementServiceV1 graduationRequirementService;

    @PostMapping("/graduation-requirement")
    public ApiResponse<GraduationRequirementResponse> saveGraduationRequirement(@RequestBody @Valid GraduationRequirementCreateRequest graduationRequirementCreateRequest) {
        GraduationRequirementResponse graduationRequirementResponse = graduationRequirementService.addGR(graduationRequirementCreateRequest);
        return ApiResponse.success("졸업요건 등록 성공", graduationRequirementResponse);
    }

    @GetMapping("/graduation-requirement/id/{id}")
    public ApiResponse<GraduationRequirementResponse> findGraduationRequirement(@PathVariable("id") Long id, @RequestParam("year") int year) {
        GraduationRequirementResponse gr = graduationRequirementService.findGR(id, year);
        return ApiResponse.success("졸업요건 조회 성공", gr);
    }

    @GetMapping("/graduation-requirement/department/{department}")
    public ApiResponse<GraduationRequirementResponse> findByDepartment(@PathVariable("department") String departmentName, @RequestParam("year") int year) {
        Department department = Department.fromUrl(departmentName);
        GraduationRequirementResponse byGRDepartment = graduationRequirementService.findByGRDepartment(department, year);
        return ApiResponse.success("해당 년도, 해당 학과로 졸업요건 조회 성공", byGRDepartment);
    }

    @GetMapping("/graduation-requirements")
    public ApiResponse<List<GraduationRequirementResponse>> findAllGraduationRequirements(@RequestParam("year") int year) {
        List<GraduationRequirementResponse> allGR = graduationRequirementService.findAllGR(year);
        return ApiResponse.success("해당 년도 모든 학과 졸업요건 조회 성공", allGR);
    }

    @DeleteMapping("/graduation-requirement/{id}")
    public ApiResponse<Void> deleteGraduationRequirement(@PathVariable("id") Long id, @RequestParam("year") int year) {
        graduationRequirementService.deleteGR(id, year);
        return ApiResponse.success("졸업요건 삭제 성공, 졸업요건 id: " + id, null);
    }

    @PutMapping("/graduation-requirement/{id}")
    public ApiResponse<Void> updateGraduationRequirement(
            @PathVariable("id") Long id,
            @RequestParam("year") int year,
            @RequestBody @Valid GraduationRequirementUpdateDto updateDto) {

        graduationRequirementService.updateGR(id, year, updateDto);
        return ApiResponse.success("졸업 요건 수정 성공, 졸업요건 id: " + id, null);
    }


    @PostMapping("/graduation-requirement/{grId}/courses")
    public ApiResponse<GraduationCourseCreateResponse> addGraduationCourse(@PathVariable("grId") Long id,
                                                                           @RequestParam("year") int year,
                                                                           @RequestBody @Valid CourseRequest courseRequest) {

        GraduationCourseCreateResponse graduationCourseCreateResponse = graduationRequirementService.addCourseToGraduationRequirement(id, year, courseRequest);

        return ApiResponse.success("졸업 요건 과목 등록 성공", graduationCourseCreateResponse);
    }

    @PostMapping("/graduation-requirement/{grId}/core-subject")
    public ApiResponse<GraduationCoreSubjectCreateResponse> addGraduationCoreSubject(@PathVariable("grId") Long id,
                                                                                     @RequestParam("year") int year,
                                                                                     @RequestParam("urlCoreType") String urlCoreType) {
        CoreType coreType = CoreType.fromUrlName(urlCoreType);

        GraduationCoreSubjectCreateResponse graduationCoreSubjectCreateResponse = graduationRequirementService.addCoreSubjectTypes(id, year, coreType);

        return ApiResponse.success("졸업요건 핵심교양 등록 성공", graduationCoreSubjectCreateResponse);
    }
}
