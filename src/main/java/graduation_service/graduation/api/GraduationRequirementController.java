package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.*;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Graduation-Requirement-Controller", description = "학과별 졸업요건 관리 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class GraduationRequirementController {

    private final GraduationRequirementServiceV1 graduationRequirementService;

    @PostMapping("/graduation-requirement")
    @Operation(summary = "졸업요건 등록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 등록 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 등록 성공 예시",
                            value = GraduationRequirementSaveExample.GRADUATION_REQUIREMENT_SAVE_SUCCESS
                    )
            ))
    public ApiResponse<GraduationRequirementResponse> saveGraduationRequirement(@RequestBody @Valid @Schema(implementation = GraduationRequirementCreateRequest.class)
                                                                                GraduationRequirementCreateRequest graduationRequirementCreateRequest) {
        GraduationRequirementResponse graduationRequirementResponse = graduationRequirementService.addGR(graduationRequirementCreateRequest);
        return ApiResponse.success("졸업요건 등록 성공", graduationRequirementResponse);
    }


    @GetMapping("/graduation-requirement/id/{id}")
    @Operation(summary = "졸업요건 id로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 id로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 id로 조회 성공 예시",
                            value = GraduationRequirementFindByIdExample.GRADUATION_REQUIREMENT_FIND_BY_ID_SUCCESS
                    )
            ))
    public ApiResponse<GraduationRequirementResponse> findGraduationRequirement(@Parameter(description = "졸업요건 id") @PathVariable("id") Long id) {
        GraduationRequirementResponse gr = graduationRequirementService.findGR(id);
        return ApiResponse.success("졸업요건 조회 성공", gr);
    }


    @GetMapping("/graduation-requirement/department/{department}")
    @Operation(summary = "졸업요건 학과로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 학과로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 학과로 조회 성공 예시",
                            value = GraduationRequirementFindByDepartmentExample.GRADUATION_REQUIREMENT_FIND_BY_DEPARTMENT_SUCCESS
                    )
            ))
    public ApiResponse<GraduationRequirementResponse> findByDepartment(@Parameter(description = "학과명") @PathVariable("department") String departmentName,
                                                                       @Parameter(description = "연도") @RequestParam("year") int year) {
        Department department = Department.fromUrl(departmentName);
        GraduationRequirementResponse byGRDepartment = graduationRequirementService.findByGRDepartment(department, year);
        return ApiResponse.success("해당 년도, 해당 학과로 졸업요건 조회 성공", byGRDepartment);
    }


    @GetMapping("/graduation-requirements")
    @Operation(summary = "특정 년도 졸업요건 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 년도 졸업요건 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "특정 년도 졸업요건 리스트 조회 성공 예시",
                            value = GraduationRequirementsFindExample.GRADUATION_REQUIREMENTS_FIND_SUCCESS
                    )
            ))
    public ApiResponse<List<GraduationRequirementResponse>> findAllGraduationRequirements(@Parameter(description = "연도") @RequestParam("year") int year) {
        List<GraduationRequirementResponse> allGR = graduationRequirementService.findAllGR(year);
        return ApiResponse.success("해당 년도 모든 학과 졸업요건 조회 성공", allGR);
    }


    @DeleteMapping("/graduation-requirement/{id}")
    @Operation(summary = "졸업요건 삭제")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 삭제 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 삭제 성공 예시",
                            value = GraduationRequirementDeleteExample.GRADUATION_REQUIREMENT_DELETE_SUCCESS
                    )
            ))
    public ApiResponse<Void> deleteGraduationRequirement(@Parameter(description = "졸업요건 id") @PathVariable("id") Long id,
                                                         @Parameter(description = "연도") @RequestParam("year") int year) {
        graduationRequirementService.deleteGR(id);
        return ApiResponse.success("졸업요건 삭제 성공, 졸업요건 id: " + id, null);
    }


    @PutMapping("/graduation-requirement/{id}")
    @Operation(summary = "졸업요건 업데이트")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 업데이트 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 업데이트 성공 예시",
                            value = GraduationRequirementUpdateExample.GRADUATION_REQUIREMENT_DELETE_SUCCESS
                    )
            ))
    public ApiResponse<Void> updateGraduationRequirement(
            @Parameter(description = "졸업요건 id") @PathVariable("id") Long id,
            @Parameter(description = "연도") @RequestParam("year") int year,
            @RequestBody @Valid @Schema(implementation = GraduationRequirementUpdateDto.class) GraduationRequirementUpdateDto updateDto) {

        graduationRequirementService.updateGR(id, updateDto);
        return ApiResponse.success("졸업 요건 수정 성공, 졸업요건 id: " + id, null);
    }


    @PostMapping("/graduation-requirement/{grId}/courses")
    @Operation(summary = "졸업요건 과목 등록", description = "졸업요건에 필요한 과목을 추가하는 API")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 과목 등록 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 과목 등록 성공 예시",
                            value = GraduationRequirementAddCourseExample.GRADUATION_REQUIREMENT_ADD_COURSE_SUCCESS
                    )
            ))
    public ApiResponse<GraduationCourseCreateResponse> addGraduationCourse(@Parameter(description = "졸업요건 id") @PathVariable("grId") Long id,
                                                                           @RequestBody @Valid @Schema(implementation = CourseRequest.class) CourseRequest courseRequest) {

        GraduationCourseCreateResponse graduationCourseCreateResponse = graduationRequirementService.addCourseToGraduationRequirement(id, courseRequest);

        return ApiResponse.success("졸업 요건 과목 등록 성공", graduationCourseCreateResponse);
    }


    @PostMapping("/graduation-requirement/{grId}/core-subject")
    @Operation(summary = "졸업요건 핵심교양 등록", description = "졸업요건에 필요한 핵심교양 타입을 추가하는 API")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 핵심교양 등록 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 핵심교양 등록 성공 예시",
                            value = GraduationRequirementAddCoreSubjectExample.GRADUATION_REQUIREMENT_ADD_COURSE_SUCCESS
                    )
            ))
    public ApiResponse<GraduationCoreSubjectCreateResponse> addGraduationCoreSubject(@Parameter(description = "졸업요건 id") @PathVariable("grId") Long id,
                                                                                     @Parameter(description = "연도") @RequestParam("year") int year,
                                                                                     @Parameter(description = "핵심교양 타입") @RequestParam("urlCoreType") String urlCoreType) {
        CoreType coreType = CoreType.fromUrlName(urlCoreType);

        GraduationCoreSubjectCreateResponse graduationCoreSubjectCreateResponse = graduationRequirementService.addCoreSubjectTypes(id, year, coreType);

        return ApiResponse.success("졸업요건 핵심교양 등록 성공", graduationCoreSubjectCreateResponse);
    }

    @DeleteMapping("/graduation-requirement/{grId}/core-subject")
    public ApiResponse<Void> deleteGraduationCoreSubject(@PathVariable Long grId,
                                                         @RequestParam("urlCoreType") String urlCoreType) {

        CoreType coreType = CoreType.fromUrlName(urlCoreType);

        graduationRequirementService.deleteCoreSubjectTypes(grId, coreType);

        return ApiResponse.success("졸업요건 id=" + grId + "에서 핵심교양 타입 " + urlCoreType + " 삭제 성공", null);
    }
}
