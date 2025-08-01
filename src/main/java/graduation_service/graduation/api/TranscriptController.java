package graduation_service.graduation.api;

import graduation_service.graduation.domain.pojo.Transcript;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import graduation_service.graduation.serviceV0.graduationComparisonService.TranscriptExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptExtractService transcriptExtractService;

    @PostMapping("/transcript")
    public ApiResponse<Transcript> parseTranscript(MultipartFile multipartFile) throws IOException {
        Transcript transcript = transcriptExtractService.extract(multipartFile);
        return ApiResponse.success("성적표 등록 완료", transcript);
    }


}
