package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.CoreSubjectCreateExample;
import graduation_service.graduation.api.exampleConst.CoreSubjectFindExample;
import graduation_service.graduation.api.exampleConst.CoreSubjectListFindByYearExample;
import graduation_service.graduation.api.exampleConst.CoreSubjectListFindExample;
import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.requestDto.coreSubjectDto.CoreSubjectCreateRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.coreResponse.CoreSubjectResponse;
import graduation_service.graduation.serviceV1.graduationComparisonServiceV1.CoreSubjectServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Core-Subject-Controller", description = "핵심교양 관리 API 엔드포인트")
@RestController
@RequestMapping("/core-subject")
@RequiredArgsConstructor
public class CoreSubjectController {

    private final CoreSubjectServiceV1 coreSubjectService;

    @PostMapping()
    @Operation(summary = "핵심교양 등록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "핵심교양 등록 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "핵심교양 등록 성공 예시",
                            value = CoreSubjectCreateExample.CORE_SUBJECT_CREATE_SUCCESS
                    )
            ))
    public ApiResponse<CoreSubjectResponse> saveCoreSubject(@RequestBody @Valid @Schema(implementation = CoreSubjectCreateRequest.class)
                                                                CoreSubjectCreateRequest coreSubjectCreateRequest) {
        CoreSubjectResponse coreSubjectResponse = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCreateRequest);
        return ApiResponse.success("핵심교양 등록 성공", coreSubjectResponse);
    }



    @GetMapping("/id/{id}")
    @Operation(summary = "핵심교양 id로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "핵심교양 id로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "핵심교양 id로 조회 성공 예시",
                            value = CoreSubjectFindExample.CORE_SUBJECT_FIND_SUCCESS
                    )
            ))
    public ApiResponse<CoreSubjectResponse> findCoreSubject(@Parameter(description = "핵심교양 id") @PathVariable("id") Long id) {
        CoreSubjectResponse byId = coreSubjectService.findById(id);
        return ApiResponse.success("핵심교양 조회 성공", byId);
    }



    @GetMapping("/course")
    @Operation(summary = "핵심교양 과목 id로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "핵심교양 과목 id로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "핵심교양 과목 id로 조회 성공 예시",
                            value = CoreSubjectFindExample.CORE_SUBJECT_FIND_SUCCESS
                    )
            ))
    public ApiResponse<CoreSubjectResponse> findByCourse(@Parameter(description = "과목 id") @RequestParam("courseId") Long courseId,
                                                         @Parameter(description = "연도") @RequestParam("curriculumYear") int curriculumYear) {

        CoreSubjectResponse byCourse = coreSubjectService.findByCourse(courseId, curriculumYear);
        return ApiResponse.success("핵심교양 조회 성공", byCourse);
    }




    @GetMapping("/coreType")
    @Operation(summary = "핵심교양 타입으로 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "핵심교양 타입으로 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "핵심교양 타입으로 조회 성공 예시",
                            value = CoreSubjectFindExample.CORE_SUBJECT_FIND_SUCCESS
                    )
            ))
    public ApiResponse<List<CoreSubjectResponse>> findByCoreType(@Parameter(description = "핵심교양 타입") @RequestParam("coreType") String coreType,
                                                                 @Parameter(description = "연도") @RequestParam("curriculumYear") int curriculumYear) {

        CoreType enumCoreType = CoreType.fromUrlName(coreType);
        List<CoreSubjectResponse> byCoreType = coreSubjectService.findByCoreType(enumCoreType, curriculumYear);

        return ApiResponse.success("핵심교양 조회 성공", byCoreType);
    }



    //모든 핵심교양 데이터 조회
    @GetMapping("/core-subjects")
    @Operation(summary = "핵심교양 전체 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "핵심교양 전체 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "핵심교양 전체 리스트 조회 성공 예시",
                            value = CoreSubjectListFindExample.CORE_SUBJECT_LIST_FIND_SUCCESS
                    )
            ))
    public ApiResponse<List<CoreSubjectResponse>> findAll() {
        List<CoreSubjectResponse> all = coreSubjectService.findAll();
        return ApiResponse.success("모든 핵심교양 리스트 조회 성공", all);
    }




    @GetMapping("/curriculumYear")
    @Operation(summary = "특정 연도 핵심교양 리스트 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 연도 핵심교양 리스트 조회 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "특정 연도 핵심교양 리스트 조회 성공 예시",
                            value = CoreSubjectListFindByYearExample.CORE_SUBJECT_LIST_FIND_BY_YEAR_SUCCESS
                    )
            ))
    public ApiResponse<List<CoreSubjectResponse>> findByYear(@Parameter(description = "연도")
                                                                 @RequestParam("curriculumYear")
                                                                 int curriculumYear) {

        List<CoreSubjectResponse> byYear = coreSubjectService.findByYear(curriculumYear);
        return ApiResponse.success("해당년도 핵심교양 리스트 조회 성공", byYear);
    }


}
