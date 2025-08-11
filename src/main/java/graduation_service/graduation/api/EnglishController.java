package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.EnglishTypeExample;
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


@Tag(name = "English-Controller", description = "영어 시험 타입 목록 조회 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class EnglishController {

    @GetMapping("/english-types")
    @Operation(summary = "영어 시험 타입 목록 조회")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "영어 시험 타입 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "영어 시험 타입 목록 조회 성공 예시",
                            value = EnglishTypeExample.ENGLISH_TYPE_SUCCESS
                    )
            ))
    public ApiResponse<TestType[]> getEnglishTypes() {
        TestType[] values = TestType.values();
        return ApiResponse.success("영어 시험 타입 목록 조회 성공", values);
    }
}
