package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.GraduationCheckExample;
import graduation_service.graduation.dto.GraduationResultDto;
import graduation_service.graduation.dto.requestDto.graduationCheckDto.GraduationCheckRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.serviceV1.graduationComparisonServiceV1.GraduationCheckServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Graduation-Check-Controller", description = "졸업요건 자가진단 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class GraduationCheckController {

    private final GraduationCheckServiceV1 graduationCheckService;

    @PostMapping("/graduation-check")
    @Operation(summary = "졸업요건 진단")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "졸업요건 진단 성공",
            content = @Content(
                    mediaType = "application/jason",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(
                            name = "졸업요건 진단 성공 예시",
                            value = GraduationCheckExample.GRADUATION_CHECK_SUCCESS
                    )
            ))
    public ApiResponse<GraduationResultDto> graduationCheck(@RequestBody @Valid @Schema(implementation = GraduationCheckRequest.class)
                                                                GraduationCheckRequest graduationCheckRequest) {
        GraduationResultDto graduationResultDto = graduationCheckService.checkGraduation(graduationCheckRequest);
        return ApiResponse.success("졸업요건 진단 성공", graduationResultDto);
    }

}
