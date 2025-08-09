package graduation_service.graduation.dto.requestDto.loginDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "로그인 ID는 필수입니다.")
    private String loginId;

    @NotBlank(message = "로그인 PW는 필수입니다.")
    private String loginPw;

}
