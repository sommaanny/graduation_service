package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.DepartmentListExample;
import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.enums.TestType;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Department-Controller", description = "학과 목록 조회 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    @GetMapping("/departments")
    @Operation(summary = "학과 목록 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "학과 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "학과 목록 조회 성공 예시",
                            value = DepartmentListExample.DEPARTMENT_TYPE_SUCCESS
                    )
            ))
    public ApiResponse<Department[]> getEnglishTypes() {
        Department[] values = Department.values();
        return ApiResponse.success("학과 목록 조회 성공", values);
    }
}
