package graduation_service.graduation.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String status;
    private String message;
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
