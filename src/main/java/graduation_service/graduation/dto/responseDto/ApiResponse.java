package graduation_service.graduation.dto.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "api 응답 공통 포맷")
public class ApiResponse<T> {

    @Schema(description = "상태 코드", example = "200")
    private String status;
    @Schema(description = "응답 메시지")
    private String message;
    @Schema(description = "구체적인 응답 데이터")
    private T data;

    //성공시 응답
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    //실패시 응답
    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>("FAIL", message, data);
    }

}
