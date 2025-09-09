package graduation_service.graduation.dto.requestDto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "로그인 ID", example = "somany")
    @NotBlank(message = "로그인 ID는 필수입니다.")
    private String loginId;

    @Schema(description = "로그인 PW", example = "12345abcd")
    @NotBlank(message = "로그인 PW는 필수입니다.")
    private String loginPw;

}
