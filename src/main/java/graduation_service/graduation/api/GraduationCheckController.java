package graduation_service.graduation.api;

import graduation_service.graduation.dto.GraduationResultDto;
import graduation_service.graduation.dto.requestDto.graduationCheckDto.GraduationCheckRequest;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.serviceV1.graduationComparisonServiceV1.GraduationCheckServiceV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GraduationCheckController {

    private final GraduationCheckServiceV1 graduationCheckService;

    @PostMapping("/graduation-check")
    public ApiResponse<GraduationResultDto> graduationCheck(@RequestBody @Valid GraduationCheckRequest graduationCheckRequest) {
        GraduationResultDto graduationResultDto = graduationCheckService.checkGraduation(graduationCheckRequest);
        return ApiResponse.success("졸업요건 진단 성공", graduationResultDto);
    }

}
