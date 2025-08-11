package graduation_service.graduation.api;

import graduation_service.graduation.api.exampleConst.TranscriptExample;
import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.serviceV0.graduationComparisonService.TranscriptExtractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@Tag(name = "Transcript-Controller", description = "성적표 파싱 API 엔드포인트")
@RestController
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptExtractService transcriptExtractService;

    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "성적표 파싱", description = "성적표 PDF 파일에서 졸업요건 진단에 필요한 부분들을 파싱하는 API")
    @io.swagger.v3.oas.annotations.responses.ApiResponse
            (responseCode = "200", description = "성적표 파싱 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "성공예시", value = TranscriptExample.TRANSCRIPT_SUCCESS)
                    )
    )
    public ApiResponse<Transcript> parseTranscript(@Parameter(description = "성적표 PDF 파일")
                                                       MultipartFile multipartFile) {
        Transcript transcript = transcriptExtractService.extract(multipartFile);
        return ApiResponse.success("성적표 등록 완료", transcript);
    }


}
