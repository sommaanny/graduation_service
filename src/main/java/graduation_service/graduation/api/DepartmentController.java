package graduation_service.graduation.api;

import graduation_service.graduation.domain.enums.Department;
import graduation_service.graduation.domain.enums.TestType;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DepartmentController {

    @GetMapping("/departments")
    public ApiResponse<Department[]> getEnglishTypes() {
        Department[] values = Department.values();
        return ApiResponse.success("학과 목록 조회 성공", values);
    }
}
