package graduation_service.graduation.api;

import graduation_service.graduation.domain.enums.TestType;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnglishController {

    @GetMapping("/english-types")
    public ApiResponse<TestType[]> getEnglishTypes() {
        TestType[] values = TestType.values();
        return ApiResponse.success("영어 시험 타입 목록 조회 성공", values);
    }
}
