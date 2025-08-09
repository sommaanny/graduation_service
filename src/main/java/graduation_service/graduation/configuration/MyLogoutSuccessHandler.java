package graduation_service.graduation.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import graduation_service.graduation.dto.responseDto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ApiResponse<Void> apiResponse = ApiResponse.success("로그아웃 성공", null);

        // HTTP 응답 상태 설정
        response.setStatus(HttpStatus.OK.value());
        // 응답의 Content-Type 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 문자 인코딩 설정
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());


        // 3. ObjectMapper를 사용하여 ApiResponse 객체를 JSON 문자열로 변환하고,
        //    HTTP 응답 본문에 씁니다.
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
