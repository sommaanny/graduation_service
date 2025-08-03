package graduation_service.graduation.api;

import graduation_service.graduation.domain.enums.CoreType;
import graduation_service.graduation.dto.requestDto.coreSubjectDto.CoreSubjectCreateRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.dto.responseDto.coreResponse.CoreSubjectResponse;
import graduation_service.graduation.serviceV1.graduationComparisonServiceV1.CoreSubjectServiceV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core-subject")
@RequiredArgsConstructor
public class CoreSubjectController {

    private final CoreSubjectServiceV1 coreSubjectService;

    @PostMapping()
    public ApiResponse<CoreSubjectResponse> saveCoreSubject(@RequestBody @Valid CoreSubjectCreateRequest coreSubjectCreateRequest) {
        CoreSubjectResponse coreSubjectResponse = coreSubjectService.addCoreSubjectCurriculum(coreSubjectCreateRequest);
        return ApiResponse.success("핵심교양 등록 성공", coreSubjectResponse);
    }

    @GetMapping("/id/{id}")
    public ApiResponse<CoreSubjectResponse> findCoreSubject(@PathVariable("id") Long id) {
        CoreSubjectResponse byId = coreSubjectService.findById(id);
        return ApiResponse.success("핵심교양 조회 성공", byId);
    }

    @GetMapping("/course")
    public ApiResponse<CoreSubjectResponse> findByCourse(@RequestParam("courseId") Long courseId,
                                                            @RequestParam("curriculumYear") int curriculumYear) {

        CoreSubjectResponse byCourse = coreSubjectService.findByCourse(courseId, curriculumYear);
        return ApiResponse.success("핵심교양 조회 성공", byCourse);
    }

    @GetMapping("/coreType")
    public ApiResponse<List<CoreSubjectResponse>> findByCoreType(@RequestParam("coreType") String coreType,
                                                           @RequestParam("curriculumYear") int curriculumYear) {

        CoreType enumCoreType = CoreType.fromUrlName(coreType);
        List<CoreSubjectResponse> byCoreType = coreSubjectService.findByCoreType(enumCoreType, curriculumYear);

        return ApiResponse.success("핵심교양 조회 성공", byCoreType);
    }

    //모든 핵심교양 데이터 조회
    @GetMapping("/core-subjects")
    public ApiResponse<List<CoreSubjectResponse>> findAll() {
        List<CoreSubjectResponse> all = coreSubjectService.findAll();
        return ApiResponse.success("모든 핵심교양 리스트 조회 성공", all);
    }

    @GetMapping("/curriculumYear")
    public ApiResponse<List<CoreSubjectResponse>> findByYear(@RequestParam("curriculumYear") int curriculumYear) {

        List<CoreSubjectResponse> byYear = coreSubjectService.findByYear(curriculumYear);
        return ApiResponse.success("해당년도 핵심교양 리스트 조회 성공", byYear);
    }


}
